package cn.jeff.study;

import cn.jeff.study.cache.CacheContext;
import cn.jeff.study.applyer.CacheApplyerLink;
import org.apache.ibatis.session.Configuration;

import java.util.List;

/**
 * @author swzhang
 * @date 2019/10/22
 */
public class CacheReplacer {


    public void replaceMapperByFile(Configuration configuration, List<CacheContext> cacheContexts) {
        new CacheApplyerLink(configuration, null,  null, cacheContexts).callApply();

    }

}
