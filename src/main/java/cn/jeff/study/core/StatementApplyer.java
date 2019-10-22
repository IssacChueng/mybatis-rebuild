package cn.jeff.study.core;

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
public class StatementApplyer extends BaseApplyer {

    public StatementApplyer(Configuration configuration, XNode mapperNode, String namespace) {
        super(configuration, mapperNode, namespace);
    }

    @Override
    protected void preApply() {

    }

    @Override
    protected void postApply() {

    }

    @Override
    protected void doApply() {
        applyNewStatement();
    }


    public void applyNewStatement() {

        List<XNode> nodeList = mapperNode.evalNodes("select|insert|update|delete");
        if (nodeList == null || nodeList.isEmpty()) {
            return;
        }
        Map<String, MappedStatement> mappedStatementMap = configurationHelper.getMappedStatementMap();
        nodeList.forEach(node -> {
            XMLStatementBuilder xmlStatementBuilder = null;
            if (configuration.getDatabaseId() != null) {
                xmlStatementBuilder = new XMLStatementBuilder(configuration, builderAssistant, node, configuration.getDatabaseId());
            } else {
                xmlStatementBuilder = new XMLStatementBuilder(configuration, builderAssistant, node);
            }

            String mapperId = node.getStringAttribute("id");

            mapperId = builderAssistant.applyCurrentNamespace(mapperId, false);
            mappedStatementMap.remove(mapperId);
            String shortId = configurationHelper.getShortName(mapperId);
            mappedStatementMap.remove(shortId);
            xmlStatementBuilder.parseStatementNode();

        });

    }


}
