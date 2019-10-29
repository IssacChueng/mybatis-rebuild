package cn.jeff.study;

import cn.jeff.study.core.ApplyerFactory;
import cn.jeff.study.applyer.BaseApplyer;
import cn.jeff.study.applyer.SqlApplyer;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

/**
 * @author swzhang
 * @date 2019/10/10
 */
public class FactoryTests {

    @Test
    public void testNew() {
        Configuration configuration = new Configuration();
        ApplyerFactory applyerFactory = new ApplyerFactory(configuration, null, null);
        BaseApplyer baseApplyer = applyerFactory.newApplyer(SqlApplyer.class);
        System.out.println(baseApplyer.getClass());
    }
}
