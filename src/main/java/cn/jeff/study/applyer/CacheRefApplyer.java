package cn.jeff.study.applyer;

import cn.jeff.study.core.MyCacheBuilderAssistant;
import org.apache.ibatis.builder.CacheRefResolver;
import org.apache.ibatis.builder.IncompleteElementException;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;

import java.util.Map;

/**
 * @author swzhang
 * @date 2019/10/21
 */
public class CacheRefApplyer extends BaseCacheApplyer {

    public CacheRefApplyer(Configuration configuration, XNode mapperNode, String namespace) {
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
        cacheContexts.forEach(cacheContext -> {
            XNode mapperNode = cacheContext.getMapperNode();
            builderAssistant = new MyCacheBuilderAssistant(configuration, cacheContext.getResource());
            setBuildAssistantNamespace(mapperNode);
            XNode cacheRefNode = mapperNode.evalNode("cache-ref");
            cacheRefElement(cacheRefNode);
        });

    }

    private void cacheRefElement(XNode context) {
        if (context != null) {
            String cacheRefKey = builderAssistant.getCurrentNamespace();
            String cacheRefValue = context.getStringAttribute("namespace");
            cacheRefs.put(cacheRefKey, cacheRefValue);
        }
    }
}
