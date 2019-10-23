package cn.jeff.study.core;

import cn.jeff.study.util.MixUtils;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.decorators.LruCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.apache.ibatis.mapping.CacheBuilder;
import org.apache.ibatis.session.Configuration;

import java.util.Map;
import java.util.Properties;

/**
 * @author swzhang
 * @date 2019/10/23
 */
public class MyCacheBuilderAssistant extends MapperBuilderAssistant {

    private ConfigurationHelper configurationHelper;

    public MyCacheBuilderAssistant(Configuration configuration, String resource) {
        super(configuration, resource);
        configurationHelper = new ConfigurationHelper(configuration);
    }


    public Cache useNewCache(
            Class<? extends Cache> typeClass,
            Class<? extends Cache> evictionClass,
            Long flushInterval,
            Integer size,
            boolean readWrite,
            boolean blocking,
            Properties props
    ) {
        Map<String, Cache> caches = configurationHelper.getCaches();
        caches.remove(getCurrentNamespace());
        caches.remove(configurationHelper.getShortName(getCurrentNamespace()));
        Cache cache = new CacheBuilder(getCurrentNamespace())
                .implementation(MixUtils.valueOrDefault(typeClass, PerpetualCache.class))
                .addDecorator(MixUtils.valueOrDefault(evictionClass, LruCache.class))
                .clearInterval(flushInterval)
                .size(size)
                .readWrite(readWrite)
                .blocking(blocking)
                .properties(props)
                .build();
        setNewValueMap(caches, getCurrentNamespace(), cache);
        return cache;
    }

    private <K, V> void setNewValueMap(Map<K, V> map, K key, V value) {
        map.computeIfPresent(key, (k, v) -> value);
        String shortKey = configurationHelper.getShortName(getCurrentNamespace());
        map.computeIfPresent((K)shortKey, (k, v) -> value);
    }

}
