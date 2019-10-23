package cn.jeff.study.applyer;

import cn.jeff.study.cache.CacheContext;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author swzhang
 * @date 2019/10/22
 */
public class CacheApplyerLink extends BaseApplyerLink {

    private List<CacheContext> cacheContexts;

    private Map<String, String> cacheRefs = new HashMap<>();

    private Map<String, Cache> caches = new HashMap<>();


    public CacheApplyerLink(Configuration configuration, XNode mapperNode, String resource, List<CacheContext> cacheContexts) {
        super(configuration, mapperNode,  resource);
        this.cacheContexts = cacheContexts;
    }



    @Override
    protected void setApplyerClass() {
        applyerClasses = Arrays.asList(CacheApplyer.class, CacheRefApplyer.class);
    }

    @Override
    protected void initApplyers() {
        super.initApplyers();
        List<BaseApplyer> delegate = getDelegate();
        delegate.forEach(applyer -> {
            if (applyer instanceof BaseCacheApplyer) {
                BaseCacheApplyer baseCacheApplyer = (BaseCacheApplyer) applyer;
                baseCacheApplyer.setCacheContexts(cacheContexts);
                baseCacheApplyer.setCaches(caches);
                baseCacheApplyer.setCacheRefs(cacheRefs);
                //每解析一个resource，都需要重新设置
                baseCacheApplyer.builderAssistant = null;
            }
        });
    }

    @Override
    public void callApply() {
        super.callApply();

        addPendingCaches();
    }

    private void addPendingCaches() {
        Map<String, Cache> configurationHelperCaches = configurationHelper.getCaches();
        caches.forEach((key, value) -> {
            try {
                configurationHelperCaches.put(key, value);
            } catch (IllegalArgumentException e) {
                configurationHelperCaches.computeIfPresent(key, (k, v) -> value);
            }
        });

        //此时所有cache都咦经在caches中，再将指向cache-ref重新指向cache
        //如果原先已经有其他cache-ref指向被更新掉的cache，并且没有更新statement，则在statement中的cache还是更新之前的cache
        Map<String, String> configurationHelperCacheRefMap = configurationHelper.getCacheRefMap();
        cacheRefs.forEach((key, value) -> {
            try {
                configurationHelperCacheRefMap.put(key, value);
            } catch (IllegalArgumentException e) {
                configurationHelperCacheRefMap.computeIfPresent(key, (k, v) -> value);
            }
            Cache cache = configurationHelperCaches.get(value);
            try {
                configurationHelperCaches.put(key, cache);
            } catch (IllegalArgumentException e) {
                configurationHelperCaches.computeIfPresent(key, (k, v) -> cache);
            }
        });

    }
}
