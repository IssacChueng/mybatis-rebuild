package cn.jeff.study.core;

import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

/**
 * @author swzhang
 * @date 2019/10/10
 */
public class MapperApplyer extends BaseApplyer{

    private List<BaseApplyer> delegate = new LinkedList<>();

    Class<? extends BaseApplyer>[] applyerClasses = new Class[]{SqlApplyer.class, StatementApplyer.class};

    public MapperApplyer(Configuration configuration, XNode mapperNode, String namespace, String resource) {
        super(configuration, mapperNode, namespace, resource);
        System.out.println("initApplyers starting" + Instant.now());
        initApplyers();
        System.out.println("initApplyers end" + Instant.now());
    }

    private void initApplyers() {
        ApplyerFactory applyerFactory = new ApplyerFactory(configuration, mapperNode, namespace, resource);
        for (int i = 0; i < applyerClasses.length; i++) {
            Class<? extends BaseApplyer> applyerClass = applyerClasses[i];
            if (applyerClass == null) {
                continue;
            }

            BaseApplyer baseApplyer = applyerFactory.newApplyer(applyerClass);
            delegate.add(baseApplyer);
        }
    }

    @Override
    public void apply() {
        for (BaseApplyer baseApplyer : delegate) {
            System.out.println("apply starting" + Instant.now());
            baseApplyer.apply();
            System.out.println("apply starting" + Instant.now());
        }
    }
}
