package cn.jeff.study.applyer;

import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;

import java.util.Arrays;

/**
 * @author swzhang
 * @date 2019/10/10
 */
public class MapperApplyerLink extends BaseApplyerLink {


    public MapperApplyerLink(Configuration configuration, XNode mapperNode, String resource) {
        super(configuration, mapperNode,  resource);
        setApplyerClass();
    }

    @Override
    protected void setApplyerClass() {
        applyerClasses = Arrays.asList(
                ParameterMapApplyer.class,
                ResultMapApplyer.class,
                SqlApplyer.class,
                StatementApplyer.class);

    }

}
