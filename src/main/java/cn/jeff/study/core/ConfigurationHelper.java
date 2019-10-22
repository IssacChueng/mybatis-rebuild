package cn.jeff.study.core;

import cn.jeff.study.util.ReflectUtlis;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMap;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.session.Configuration;

import java.util.Map;
import java.util.Set;

/**
 * @author swzhang
 * @date 2019/09/27
 */
public class ConfigurationHelper {
    private Configuration configuration;

    public ConfigurationHelper(Configuration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("配置不能为空");
        }
        this.configuration = configuration;
    }

    public ConfigurationHelper(ConfigurationHelper configurationHelper) {
        this.configuration = configurationHelper.configuration;
    }

    public Set<String> getLoadedResources() {
        return ReflectUtlis.getFieldObject("loadedResources", configuration);
    }

    public Map<String, MappedStatement> getMappedStatementMap() {
        return ReflectUtlis.getFieldObject("mappedStatements", configuration);
    }

    public Map<String, ParameterMap> getParameterMap() {
        return ReflectUtlis.getFieldObject("parameterMaps", configuration);
    }

    public Map<String, ResultMap> getResultMap() {
        return ReflectUtlis.getFieldObject("resultMaps", configuration);
    }

    public Map<String, Cache> getCaches() {
        return ReflectUtlis.getFieldObject("caches", configuration);
    }

    public Map<String, String> getCacheRefMap() {
        return ReflectUtlis.getFieldObject("cacheRefMap", configuration);
    }

    public <T> Class<? extends T> resolveClass(String alias) {
        if (alias == null) {
            return null;
        }
        try {
            return resolveAlias(alias);
        } catch (Exception e) {
            throw new BuilderException("Error resolving class. Cause: " + e, e);
        }
    }

    public <T> Class<? extends T> resolveAlias(String alias) {
        if (alias == null) {
            return null;
        }
        try {
            return configuration.getTypeAliasRegistry().resolveAlias(alias);
        } catch (Exception e) {
            throw new BuilderException("Error resolving class. Cause: " + e, e);
        }
    }
    public String getShortName(String key) {
        final String[] keyParts = key.split("\\.");
        return keyParts[keyParts.length - 1];
    }

}
