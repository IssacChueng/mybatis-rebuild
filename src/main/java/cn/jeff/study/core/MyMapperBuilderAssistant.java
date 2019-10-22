package cn.jeff.study.core;

import org.apache.ibatis.builder.IncompleteElementException;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.cache.Cache;
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

        Map<String, MappedStatement> mappedStatementMap = configurationHelper.getMappedStatementMap();
        removeFromMap(mappedStatementMap, id);
        return super.addMappedStatement(id, sqlSource, statementType, sqlCommandType, fetchSize, timeout, parameterMap, parameterType, resultMap, resultType, resultSetType, flushCache, useCache, resultOrdered, keyGenerator, keyProperty, keyColumn, databaseId, lang, resultSets);
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
        return super.useNewCache(typeClass, evictionClass, flushInterval, size, readWrite, blocking, props);
    }

    private void removeFromMap(Map<String, ?> map, String id) {
        String idWithNamespace = applyCurrentNamespace(id, false);
        map.remove(idWithNamespace);
        map.remove(configurationHelper.getShortName(idWithNamespace));
    }
}
