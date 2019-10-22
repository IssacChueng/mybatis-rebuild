package cn.jeff.study.map;

import java.util.HashMap;

/**
 * @author swzhang
 * @date 2019/10/22
 */
public class StrictMap<K, V> extends HashMap<K, V> {
    @Override
    public V put(K key, V value) {
        if (containsKey(key)) {
            throw new IllegalArgumentException();
        }

        return super.put(key, value);
    }
}
