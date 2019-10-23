package cn.jeff.study.applyer;

import cn.jeff.study.core.ConfigurationHelper;
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


    protected MapperBuilderAssistant builderAssistant;

    protected ConfigurationHelper configurationHelper;

    public BaseApplyer(Configuration configuration, XNode mapperNode, String namespace) {
        this.configuration = configuration;
        this.mapperNode = mapperNode;
        this.namespace = namespace;
        configurationHelper = new ConfigurationHelper(configuration);
    }

    protected abstract void preApply();

    protected abstract void postApply();

    protected abstract void doApply();

    public void apply() {
        preApply();
        doApply();
        postApply();
    }


}
