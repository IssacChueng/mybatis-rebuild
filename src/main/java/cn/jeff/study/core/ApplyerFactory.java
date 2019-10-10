package cn.jeff.study.core;

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

    private String resource;


    public ApplyerFactory(Configuration configuration, XNode mapperNode, String namespace, String resource) {
        this.configuration = configuration;
        this.mapperNode = mapperNode;
        this.namespace = namespace;
        this.resource = resource;
    }

    public <T extends BaseApplyer> T newApplyer(Class<T> type) {
        try {
            Constructor<T> applyerConstructor = type.getConstructor(Configuration.class, XNode.class, String.class, String.class);
            applyerConstructor.setAccessible(true);
            return applyerConstructor.newInstance(configuration, mapperNode, namespace, resource);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            //ignore
            return null;
        }
    }

}
