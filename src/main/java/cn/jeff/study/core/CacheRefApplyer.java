package cn.jeff.study.core;

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
        XNode cacheRefNode = mapperNode.evalNode("cache-ref");
        cacheRefElement(cacheRefNode);
    }

    private void cacheRefElement(XNode context) {
        if (context != null) {
            Map<String, String> cacheRefMap = configurationHelper.getCacheRefMap();
            cacheRefMap.remove(builderAssistant.getCurrentNamespace());
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
