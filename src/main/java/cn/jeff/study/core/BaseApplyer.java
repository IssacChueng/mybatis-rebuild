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

    public BaseApplyer(Configuration configuration, XNode mapperNode, String namespace, String resource) {
        this.configuration = configuration;
        this.mapperNode = mapperNode;
        this.namespace = namespace;
        this.resource = resource;
    }

    protected abstract void apply();
}
