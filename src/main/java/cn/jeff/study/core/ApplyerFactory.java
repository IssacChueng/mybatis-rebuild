package cn.jeff.study.core;

import cn.jeff.study.applyer.BaseApplyer;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author swzhang
 * @date 2019/10/10
 */
public class ApplyerFactory {
    private Configuration configuration;

    private XNode mapperNode;

    private String namespace;


    public ApplyerFactory(Configuration configuration, XNode mapperNode, String namespace) {
        this.configuration = configuration;
        this.mapperNode = mapperNode;
        this.namespace = namespace;
    }

    public <T extends BaseApplyer> T newApplyer(Class<T> type) {
        try {
            Constructor<T> applyerConstructor = type.getConstructor(Configuration.class, XNode.class, String.class);
            applyerConstructor.setAccessible(true);
            return applyerConstructor.newInstance(configuration, mapperNode, namespace);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            //ignore
            return null;
        }
    }

}
