package cn.jeff.study;

import cn.jeff.study.core.ApplyerFactory;
import cn.jeff.study.core.BaseApplyer;
import cn.jeff.study.core.SqlApplyer;
import org.junit.Test;

/**
 * @author swzhang
 * @date 2019/10/10
 */
public class FactoryTests {

    @Test
    public void testNew() {
        ApplyerFactory applyerFactory = new ApplyerFactory(null, null, null);
        BaseApplyer baseApplyer = applyerFactory.newApplyer(SqlApplyer.class);
        System.out.println(baseApplyer.getClass());
    }
}
