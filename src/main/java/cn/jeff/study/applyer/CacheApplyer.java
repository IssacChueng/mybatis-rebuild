package cn.jeff.study.applyer;

import cn.jeff.study.cache.CacheContext;
import cn.jeff.study.core.MyCacheBuilderAssistant;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;

import java.util.List;
import java.util.Properties;

/**
 * @author swzhang
 * @date 2019/10/21
 */
public class CacheApplyer extends BaseCacheApplyer {

    public CacheApplyer(Configuration configuration, XNode mapperNode, String namespace) {
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
        //如果原先有其他mapper指向这个cache，并且当前cache被删除，则原先cache不受影响
        //如果当前cache没有被删除，则需要将原来的cache重新指向这个新的cache
        cacheContexts.forEach(cacheContext -> {
            XNode mapperNode = cacheContext.getMapperNode();
            builderAssistant = new MyCacheBuilderAssistant(configuration, cacheContext.getResource());
            setBuildAssistantNamespace(mapperNode);
            XNode cacheNode = mapperNode.evalNode("cache");
            cacheElement(cacheNode);
        });

    }

    private void cacheElement(XNode context) {
        if (context != null) {
            String type = context.getStringAttribute("type", "PERPETUAL");
            Class<? extends Cache> typeClass = configurationHelper.resolveAlias(type);
            String eviction = context.getStringAttribute("eviction", "LRU");
            Class<? extends Cache> evictionClass = configurationHelper.resolveAlias(eviction);
            Long flushInterval = context.getLongAttribute("flushInterval");
            Integer size = context.getIntAttribute("size");
            boolean readWrite = !context.getBooleanAttribute("readOnly", false);
            boolean blocking = context.getBooleanAttribute("blocking", false);
            Properties props = context.getChildrenAsProperties();

            Cache cache = builderAssistant.useNewCache(typeClass, evictionClass, flushInterval, size, readWrite, blocking, props);
            caches.put(cache.getId(), cache);
        }
    }

}
