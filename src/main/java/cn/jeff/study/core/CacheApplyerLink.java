package cn.jeff.study.core;

import cn.jeff.study.cache.CacheContext;
import cn.jeff.study.util.ReflectUtlis;
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
            }
        });
    }

    @Override
    public void callApply() {
        super.callApply();

    }

    private void addPendingCaches() {
        Map<String, Cache> configurationHelperCaches = configurationHelper.getCaches();
        caches.forEach((key, value) -> {
            try {
                configurationHelperCaches.put(key, value);
            } catch (IllegalArgumentException e) {
                configurationHelperCaches.remove(key);
                //configurationHelperCaches.computeIfPresent()
                configurationHelperCaches.put(key, value);
            }
        });
    }
}
