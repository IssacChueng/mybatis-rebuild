package cn.jeff.study.map;

import org.junit.Test;

import java.util.Map;

/**
 * @author swzhang
 * @date 2019/10/22
 */
public class MapPutTests {

    @Test
    public void testPut() {
        Map<String, String> map = new StrictMap<>();
        map.put("1", "1");
        map.computeIfPresent("1", (k, ov) -> {
            return "2";
        });
        System.out.println(map.get("1"));
    }
}
