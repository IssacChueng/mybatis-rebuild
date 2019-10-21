package cn.jeff.study.core;

import org.apache.ibatis.builder.CacheRefResolver;
import org.apache.ibatis.builder.IncompleteElementException;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;

/**
 * @author swzhang
 * @date 2019/10/21
 */
public class CacheRefApplyer extends BaseApplyer {

    public CacheRefApplyer(Configuration configuration, XNode mapperNode, String namespace, String resource) {
        super(configuration, mapperNode, namespace, resource);
    }

    @Override
    protected void preApply() {

    }

    @Override
    protected void postApply() {

    }

    @Override
    protected void doApply() {
        XNode cacheRefNode = mapperNode.evalNode("cache-ref");
        cacheRefElement(cacheRefNode);
    }

    private void cacheRefElement(XNode context) {
        if (context != null) {
            configuration.addCacheRef(builderAssistant.getCurrentNamespace(), context.getStringAttribute("namespace"));
            CacheRefResolver cacheRefResolver = new CacheRefResolver(builderAssistant, context.getStringAttribute("namespace"));
            try {
                cacheRefResolver.resolveCacheRef();
            } catch (IncompleteElementException e) {
                configuration.addIncompleteCacheRef(cacheRefResolver);
            }
        }
    }
}
