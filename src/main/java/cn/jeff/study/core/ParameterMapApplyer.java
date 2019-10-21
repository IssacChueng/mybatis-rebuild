package cn.jeff.study.core;

import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;

/**
 * @author swzhang
 * @date 2019/10/21
 */
public class ParameterMapApplyer extends BaseApplyer{

    public ParameterMapApplyer(Configuration configuration, XNode mapperNode, String namespace, String resource) {
        super(configuration, mapperNode, namespace, resource);
    }

    @Override
    protected void apply() {

    }
}
