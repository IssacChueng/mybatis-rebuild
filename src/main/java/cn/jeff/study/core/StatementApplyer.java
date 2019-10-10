package cn.jeff.study.core;

import cn.jeff.study.core.ConfigurationHelper;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.xml.XMLStatementBuilder;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;

import java.util.List;
import java.util.Map;

/**
 * @author swzhang
 * @date 2019/10/10
 */
public class StatementApplyer extends BaseApplyer{

    public StatementApplyer(Configuration configuration, XNode mapperNode, String namespace, String resource) {
        super(configuration, mapperNode, namespace, resource);
    }

    @Override
    protected void apply() {
        applyNewStatement();
    }


    public void applyNewStatement() {
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
