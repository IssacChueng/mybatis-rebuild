package cn.jeff.study;

import cn.jeff.study.applyer.CacheApplyerLink;
import cn.jeff.study.cache.CacheContext;
import cn.jeff.study.util.CollectionUtils;
import com.sun.istack.internal.NotNull;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;

import java.util.*;

/**
 * @author swzhang
 * @date 2019/10/23
 */
public class MapperConfiguration {

    /**
     * resource -> MapperNode
     */
    private Map<String, XNode> mapperNodes = new HashMap<>();

    private Configuration configuration;

    private CacheApplyerLink cacheReplacer;

    private List<MapperReplacer> mapperReplacers;


    public MapperConfiguration(@NotNull Configuration configuration) {
        this.configuration = configuration;
        mapperReplacers = new LinkedList<>();
    }

    public MapperConfiguration addResource(@NotNull String resource, @NotNull XNode mapperNode) {
        if (mapperNodes.containsKey(resource)) {
            throw new IllegalArgumentException("已经存在该资源");
        }
        mapperNodes.put(resource, mapperNode);
        return this;
    }

    public Map<String, XNode> getMapperNodes() {
        return mapperNodes;
    }

    public void setMapperNodes(Map<String, XNode> mapperNodes) {
        this.mapperNodes = mapperNodes;
    }

    public void clear() {
        mapperNodes.clear();
        cacheReplacer = null;
        if (CollectionUtils.isNotEmpty(mapperReplacers)) {
            mapperReplacers.clear();
        }
        mapperReplacers = null;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public static class Builder {
        private MapperConfiguration mapperConfiguration;

        public Builder(Configuration configuration) {
            mapperConfiguration = new MapperConfiguration(configuration);
            mapperConfiguration.mapperNodes = new HashMap<>();
        }

        public MapperConfiguration build() {
            Map<String, XNode> mapperNodesLocal = mapperConfiguration.mapperNodes;


            mapperConfiguration.mapperReplacers = new ArrayList<>(mapperNodesLocal.size());
            if (mapperConfiguration.configuration.isCacheEnabled()) {
                List<CacheContext> cacheContextList = new ArrayList<>(mapperNodesLocal.size());
                mapperNodesLocal.forEach((key, value) -> {
                    CacheContext cacheContext = new CacheContext();
                    cacheContext.setMapperNode(value);
                    cacheContext.setResource(key);
                });
                mapperConfiguration.cacheReplacer = new CacheApplyerLink(mapperConfiguration.configuration, null, null, cacheContextList);
            }
            return mapperConfiguration;
        }
        public Builder addResource(@NotNull String resource, @NotNull XNode mapperNode) {
            mapperConfiguration.mapperNodes.put(resource, mapperNode);
            return this;
        }


    }
}
