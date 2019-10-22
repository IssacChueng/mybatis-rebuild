package cn.jeff.study.core;

import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

/**
 * @author swzhang
 * @date 2019/10/10
 */
public class MapperApplyer extends BaseApplyer {

    Class<? extends BaseApplyer>[] applyerClasses = new Class[]{CacheRefApplyer.class, CacheApplyer.class, ParameterMapApplyer.class, ResultMapApplyer.class,SqlApplyer.class, StatementApplyer.class};
    private List<BaseApplyer> delegate = new LinkedList<>();

    public MapperApplyer(Configuration configuration, XNode mapperNode, String namespace, String resource) {
        super(configuration, mapperNode, namespace, resource);
        initApplyers();
    }

    @Override
    protected void preApply() {

    }

    @Override
    protected void postApply() {

    }

    private void initApplyers() {
        ApplyerFactory applyerFactory = new ApplyerFactory(configuration, mapperNode, namespace, resource);
        if (!configuration.isCacheEnabled()) {
            //不推荐生产环境使用,因为缓存和session有关,存放在executor中, 这里只清理了Mapper中定义的cache,无法清理localCache 并且代价是原来的Cache将会被保留在Executor的tcm里面
            applyerClasses = new Class[]{ParameterMapApplyer.class, ResultMapApplyer.class,SqlApplyer.class, StatementApplyer.class};
        }

        MapperBuilderAssistant builderAssistant = new MyMapperBuilderAssistant(configuration, resource);
        builderAssistant.setCurrentNamespace(namespace);

        for (int i = 0; i < applyerClasses.length; i++) {
            Class<? extends BaseApplyer> applyerClass = applyerClasses[i];
            if (applyerClass == null) {
                continue;
            }

            BaseApplyer baseApplyer = applyerFactory.newApplyer(applyerClass);
            baseApplyer.builderAssistant = builderAssistant;
            delegate.add(baseApplyer);
        }

    }

    @Override
    public void doApply() {
        for (BaseApplyer baseApplyer : delegate) {
            baseApplyer.apply();
        }
    }
}
