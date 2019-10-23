package cn.jeff.study;

import cn.jeff.study.applyer.CacheApplyerLink;
import cn.jeff.study.applyer.MapperApplyerLink;
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

    private CacheApplyerLink cacheApplyerLink;

    private List<MapperApplyerLink> mapperApplyerLinks;


    public MapperConfiguration(@NotNull Configuration configuration) {
        this.configuration = configuration;
        mapperApplyerLinks = new LinkedList<>();
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
        cacheApplyerLink = null;
        if (CollectionUtils.isNotEmpty(mapperApplyerLinks)) {
            mapperApplyerLinks.clear();
        }
        mapperApplyerLinks = null;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void parse() {
        if (configuration.isCacheEnabled()) {
            cacheApplyerLink.callApply();
        }
        if (CollectionUtils.isNotEmpty(mapperApplyerLinks)) {
            mapperApplyerLinks.forEach(MapperApplyerLink::callApply);
        }
    }

    public static class Builder {
        private MapperConfiguration mapperConfiguration;

        public Builder(Configuration configuration) {
            mapperConfiguration = new MapperConfiguration(configuration);
            mapperConfiguration.mapperNodes = new HashMap<>();
        }

        public MapperConfiguration build() {
            Map<String, XNode> mapperNodesLocal = mapperConfiguration.mapperNodes;
            Configuration configurationLocal = mapperConfiguration.configuration;
            List<MapperApplyerLink> mapperApplyerLinkLocal = mapperConfiguration.mapperApplyerLinks = new ArrayList<>(mapperNodesLocal.size());
            if (configurationLocal.isCacheEnabled()) {
                List<CacheContext> cacheContextList = new ArrayList<>(mapperNodesLocal.size());
                mapperNodesLocal.forEach((key, value) -> {
                    CacheContext cacheContext = new CacheContext();
                    cacheContext.setMapperNode(value);
                    cacheContext.setResource(key);

                    MapperApplyerLink mapperApplyerLink = new MapperApplyerLink(configurationLocal, value, key);
                    mapperApplyerLinkLocal.add(mapperApplyerLink);

                });
                mapperConfiguration.cacheApplyerLink = new CacheApplyerLink(configurationLocal, null, null, cacheContextList);
            }
            return mapperConfiguration;
        }
        public Builder addResource(@NotNull String resource, @NotNull XNode mapperNode) {
            mapperConfiguration.mapperNodes.put(resource, mapperNode);
            return this;
        }


    }
}
