package cn.jeff.study;

import cn.jeff.study.applyer.MapperApplyerLink;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.Configuration;

import java.io.IOException;

/**
 * @author swzhang
 * @date 2019/09/27
 */
public class MapperReplacer {

    public void replaceMapperByFile(XNode mapperNode, Configuration configuration, String resource, boolean useCache) throws IOException {

        new MapperApplyerLink(configuration, mapperNode,  resource).callApply();

    }

}
