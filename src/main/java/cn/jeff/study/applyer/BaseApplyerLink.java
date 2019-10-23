package cn.jeff.study.applyer;

import cn.jeff.study.core.ApplyerFactory;
import cn.jeff.study.core.ConfigurationHelper;
import cn.jeff.study.core.MyMapperBuilderAssistant;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author swzhang
 * @date 2019/10/22
 */
public abstract class BaseApplyerLink {
    protected List<Class<? extends BaseApplyer>> applyerClasses = new ArrayList<>();
    private List<BaseApplyer> delegate = new LinkedList<>();

    protected Configuration configuration;

    protected XNode mapperNode;

    protected String namespace;

    protected String resource;

    protected MapperBuilderAssistant builderAssistant;

    protected ConfigurationHelper configurationHelper;

    public BaseApplyerLink(Configuration configuration, XNode mapperNode, String resource) {
        this.configuration = configuration;
        this.mapperNode = mapperNode;
        this.resource = resource;
        configurationHelper = new ConfigurationHelper(configuration);
        builderAssistant = new MyMapperBuilderAssistant(configuration, resource);
        namespace = mapperNode.getStringAttribute("namespace");
        if (namespace == null || namespace.equals("")) {
            throw new IllegalArgumentException("namespace can not be empty");
        }
        builderAssistant.setCurrentNamespace(namespace);
        //此时已经处理好所有缓存
        if (configuration.isCacheEnabled()) {
            builderAssistant.useCacheRef(namespace);
        }

        setApplyerClass();
        initApplyers();
    }

    protected abstract void setApplyerClass();

    protected void initApplyers() {
        ApplyerFactory applyerFactory = new ApplyerFactory(configuration, mapperNode, namespace);
        MapperBuilderAssistant builderAssistant = new MyMapperBuilderAssistant(configuration, resource);
        builderAssistant.setCurrentNamespace(namespace);
        if (configuration.isCacheEnabled()) {
            //不推荐生产环境使用,因为缓存和session有关,存放在executor中, 这里只清理了Mapper中定义的cache,无法清理localCache 并且代价是原来的Cache将会被保留在Executor的tcm里面
            //设置使用的缓存
            builderAssistant.useCacheRef(namespace);
        }

        for (int i = 0; i < applyerClasses.size(); i++) {
            Class<? extends BaseApplyer> applyerClass = applyerClasses.get(i);
            if (applyerClass == null) {
                continue;
            }

            BaseApplyer baseApplyer = applyerFactory.newApplyer(applyerClass);
            baseApplyer.builderAssistant = builderAssistant;
            delegate.add(baseApplyer);
        }

        delegate = Collections.unmodifiableList(delegate);

    }

    public List<BaseApplyer> getDelegate() {
        return delegate;
    }

    public void callApply() {
        for (BaseApplyer baseApplyer : delegate) {
            baseApplyer.apply();
        }
    }
}
