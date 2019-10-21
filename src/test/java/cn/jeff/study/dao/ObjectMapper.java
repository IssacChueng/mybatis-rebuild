package cn.jeff.study.dao;

import cn.jeff.study.model.Condition;
import cn.jeff.study.model.Obj;

/**
 * @author swzhang
 * @date 2019/10/08
 */

public interface ObjectMapper {
    Integer selectOne();

    Integer selectWithInclude();

    Obj selectObj();

    Obj selectObjCondition(Condition condition);
}
