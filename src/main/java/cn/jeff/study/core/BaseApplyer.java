package cn.jeff.study.core;

import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;

/**
 * @author swzhang
 * @date 2019/10/10
 */
public abstract class BaseApplyer {
    protected Configuration configuration;

    protected XNode mapperNode;

    protected String namespace;

    protected String resource;

    protected MapperBuilderAssistant builderAssistant;

    protected ConfigurationHelper configurationHelper;

    public BaseApplyer(Configuration configuration, XNode mapperNode, String namespace, String resource) {
        this.configuration = configuration;
        this.mapperNode = mapperNode;
        this.namespace = namespace;
        this.resource = resource;
        this.builderAssistant = new MyMapperBuilderAssistant(configuration, resource);
        builderAssistant.setCurrentNamespace(namespace);

        configurationHelper = new ConfigurationHelper(configuration);
    }

    protected  abstract void preApply();

    protected abstract void postApply();

    protected abstract void doApply();

    public void apply() {
        preApply();
        doApply();
        postApply();
    }


}
