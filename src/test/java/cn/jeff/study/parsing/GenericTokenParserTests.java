package cn.jeff.study.parsing;

import org.apache.ibatis.parsing.GenericTokenParser;
import org.apache.ibatis.parsing.TokenHandler;
import org.junit.Test;

/**
 * @author swzhang
 * @date 2019/12/03
 */
public class GenericTokenParserTests {

    @Test
    public void testParse() {
        GenericTokenParser genericTokenParser = new GenericTokenParser("${", "}", new TestTokenHandler());
        String result = genericTokenParser.parse("select ${sieie} sss");
        System.out.printf("result = %s\n", result);
        result = genericTokenParser.parse("select ${sieie } ${ssssde} sss");
        System.out.printf("result = %s\n", result);
        result = genericTokenParser.parse("select ${sieie s");
        System.out.printf("result = %s\n", result);

    }

    public static class TestTokenHandler implements TokenHandler {

        @Override
        public String handleToken(String content) {
            System.out.println(content);
            return content;
        }
    }
}
