package cn.jeff.study.dao;

import cn.jeff.study.model.Bean;
import cn.jeff.study.model.Condition;

public interface BeanMapper {

    Bean getBeanById(String id);

    Bean selectOneByCondition(Condition condition);

    Bean insert(Bean bean);

    int delete(String id);

    int update(Bean bean);
}
