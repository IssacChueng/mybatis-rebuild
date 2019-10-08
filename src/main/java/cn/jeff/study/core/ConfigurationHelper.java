package cn.jeff.study.core;

import cn.jeff.study.util.ReflectUtlis;
import org.apache.ibatis.mapping.MappedStatement;
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
}
