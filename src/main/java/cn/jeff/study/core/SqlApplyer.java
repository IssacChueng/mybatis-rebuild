package cn.jeff.study.core;

import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;

/**
 * @author swzhang
 * @date 2019/10/10
 */
public class SqlApplyer extends BaseApplyer {
    public SqlApplyer(Configuration configuration, XNode mapperNode, String namespace, String resource) {
        super(configuration, mapperNode, namespace, resource);
    }

    @Override
    protected void apply() {
        applySql();
    }

    public void applySql() {

    }
}
