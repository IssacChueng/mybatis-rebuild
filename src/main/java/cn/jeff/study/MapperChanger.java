package cn.jeff.study;

import cn.jeff.study.core.ConfigurationHelper;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.builder.xml.XMLStatementBuilder;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.session.Configuration;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @author swzhang
 * @date 2019/09/27
 */
public class MapperChanger {

    public void changeMapperByFile(String filePath, String characterEncode, Configuration configuration, Class<?> mapperClass) throws IOException {

        URI uri = URI.create(filePath);
        String mapperXml = IOUtils.toString(uri, Charset.forName(characterEncode));
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
        nodeList.forEach(node -> {
            XMLStatementBuilder xmlStatementBuilder = null;
            if (configuration.getDatabaseId() != null) {
                xmlStatementBuilder = new XMLStatementBuilder(configuration, mapperBuilderAssistant, node, configuration.getDatabaseId());
            } else {
                xmlStatementBuilder = new XMLStatementBuilder(configuration, mapperBuilderAssistant, node);
            }

            xmlStatementBuilder.parseStatementNode();



        });

    }
}
