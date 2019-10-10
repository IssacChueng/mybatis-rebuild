package cn.jeff.study;

import cn.jeff.study.core.MapperApplyer;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.session.Configuration;

import java.io.IOException;
import java.io.Reader;
import java.time.Instant;

/**
 * @author swzhang
 * @date 2019/09/27
 */
public class MapperChanger {

    public void changeMapperByFile(Reader mapperXml, Configuration configuration, Class<?> mapperClass) throws IOException {
        System.out.println("create XPathParser" + Instant.now());

        XPathParser parser = new XPathParser(mapperXml);
        System.out.println("get mapper node starting" + Instant.now());
        XNode mapperNode = parser.evalNode("/mapper");
        String namespace = mapperNode.getStringAttribute("namespace");
        String resource = mapperClass.getName().replace(".", "/") + ".xml";
        System.out.println("create mapperApplyer starting" + Instant.now());

        new MapperApplyer(configuration, mapperNode, namespace, resource).apply();

    }

}
