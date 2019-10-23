package cn.jeff.study.cache;

import org.apache.ibatis.parsing.XNode;

/**
 * @author swzhang
 * @date 2019/10/22
 */
public class CacheContext {
    private XNode mapperNode;

    private String resource;

    public XNode getMapperNode() {
        return mapperNode;
    }

    public void setMapperNode(XNode mapperNode) {
        this.mapperNode = mapperNode;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
}
