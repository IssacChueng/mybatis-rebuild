package cn.jeff.study;

import cn.jeff.study.core.ConfigurationHelper;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.xml.XMLStatementBuilder;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.session.Configuration;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * @author swzhang
 * @date 2019/09/27
 */
public class MapperChanger {

    public void changeMapperByFile(String mapperXml, Configuration configuration, Class<?> mapperClass) throws IOException {

        XPathParser parser = new XPathParser(mapperXml);
        XNode mapperNode = parser.evalNode("/mapper");
        String namespace = mapperNode.getStringAttribute("namespace");
        String resource = mapperClass.getName().replace(".", "/") + ".xml";
        MapperBuilderAssistant mapperBuilderAssistant = new MapperBuilderAssistant(configuration, resource);
        mapperBuilderAssistant.setCurrentNamespace(namespace);
        List<XNode> nodeList = mapperNode.evalNodes("select|insert|update|delete");
        if (nodeList == null || nodeList.isEmpty()) {
            return;
        }
        ConfigurationHelper configurationHelper = new ConfigurationHelper(configuration);
        Map<String, MappedStatement> mappedStatementMap = configurationHelper.getMappedStatementMap();
        nodeList.forEach(node -> {
            XMLStatementBuilder xmlStatementBuilder = null;
            if (configuration.getDatabaseId() != null) {
                xmlStatementBuilder = new XMLStatementBuilder(configuration, mapperBuilderAssistant, node, configuration.getDatabaseId());
            } else {
                xmlStatementBuilder = new XMLStatementBuilder(configuration, mapperBuilderAssistant, node);
            }

            String mapperId = node.getStringAttribute("id");
            mapperId = mapperBuilderAssistant.applyCurrentNamespace(mapperId, false);
            mappedStatementMap.remove(mapperId);
            xmlStatementBuilder.parseStatementNode();

        });

    }
}
