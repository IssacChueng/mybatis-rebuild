package cn.jeff.study.core;

import cn.jeff.study.util.MixUtils;
import org.apache.ibatis.builder.IncompleteElementException;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.decorators.LruCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.Configuration;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author swzhang
 * @date 2019/10/21
 */
public class MyMapperBuilderAssistant extends MapperBuilderAssistant {

    private ConfigurationHelper configurationHelper;

    private Cache currentCache;

    public MyMapperBuilderAssistant(Configuration configuration, String resource) {
        super(configuration, resource);
        configurationHelper = new ConfigurationHelper(configuration);
    }

    @Override
    public ResultMap addResultMap(String id, Class<?> type, String extend, Discriminator discriminator, List<ResultMapping> resultMappings, Boolean autoMapping) {
        Map<String, ResultMap> configurationHelperResultMap = configurationHelper.getResultMap();
        removeFromMap(configurationHelperResultMap, id);
        return super.addResultMap(id, type, extend, discriminator, resultMappings, autoMapping);
    }

    @Override
    public ParameterMap addParameterMap(String id, Class<?> parameterClass, List<ParameterMapping> parameterMappings) {
        Map<String, ParameterMap> parameterMap = configurationHelper.getParameterMap();
        removeFromMap(parameterMap, id);
        return super.addParameterMap(id, parameterClass, parameterMappings);
    }

    @Override
    public MappedStatement addMappedStatement(
            String id,
            SqlSource sqlSource,
            StatementType statementType,
            SqlCommandType sqlCommandType,
            Integer fetchSize,
            Integer timeout,
            String parameterMap,
            Class<?> parameterType,
            String resultMap,
            Class<?> resultType,
            ResultSetType resultSetType,
            boolean flushCache,
            boolean useCache,
            boolean resultOrdered,
            KeyGenerator keyGenerator,
            String keyProperty,
            String keyColumn,
            String databaseId,
            LanguageDriver lang,
            String resultSets) {

        MappedStatement mappedStatement = null;
        try {
            mappedStatement = super.addMappedStatement(id, sqlSource, statementType, sqlCommandType, fetchSize, timeout, parameterMap, parameterType, resultMap, resultType, resultSetType, flushCache, useCache, resultOrdered, keyGenerator, keyProperty, keyColumn, databaseId, lang, resultSets);
        } catch (Exception e) {
            mappedStatement = configuration.getMappedStatement(applyCurrentNamespace(id, false));

        }
        return mappedStatement;

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
        currentCache = cache;
        return cache;
    }

    private <K, V> void setNewValueMap(Map<K, V> map, K key, V value) {
        map.computeIfPresent(key, (k, v) -> value);
        String shortKey = configurationHelper.getShortName(getCurrentNamespace());
        map.computeIfPresent((K)shortKey, (k, v) -> value);
    }

    private void removeFromMap(Map<String, ?> map, String id) {
        String idWithNamespace = applyCurrentNamespace(id, false);
        map.remove(idWithNamespace);
        map.remove(configurationHelper.getShortName(idWithNamespace));
    }
}
