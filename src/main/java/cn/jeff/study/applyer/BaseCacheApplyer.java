package cn.jeff.study.applyer;

import cn.jeff.study.cache.CacheContext;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;

import java.util.List;
import java.util.Map;

/**
 * @author swzhang
 * @date 2019/10/22
 */
public abstract class BaseCacheApplyer extends BaseApplyer{
    protected List<CacheContext> cacheContexts;

    protected Map<String, String> cacheRefs;

    protected Map<String, Cache> caches;


    public BaseCacheApplyer(Configuration configuration, XNode mapperNode, String namespace) {
        super(configuration, mapperNode, namespace);

    }

    public List<CacheContext> getCacheContexts() {
        return cacheContexts;
    }


    protected void setBuildAssistantNamespace(XNode mapperNode) {
        namespace = mapperNode.getStringAttribute("namespace");
        if (namespace == null || namespace.equals("")) {
            throw new IllegalArgumentException("namespace can not be empty");
        }
        builderAssistant.setCurrentNamespace(namespace);
    }

    public void setCacheContexts(List<CacheContext> cacheContexts) {
        this.cacheContexts = cacheContexts;
    }

    public Map<String, String> getCacheRefs() {
        return cacheRefs;
    }

    public void setCacheRefs(Map<String, String> cacheRefs) {
        this.cacheRefs = cacheRefs;
    }

    public Map<String, Cache> getCaches() {
        return caches;
    }

    public void setCaches(Map<String, Cache> caches) {
        this.caches = caches;
    }
}
