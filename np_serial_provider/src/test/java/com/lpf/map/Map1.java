package com.lpf.map;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author
 * @create 2017-10-16 10:51
 **/
public class Map1 {


    public static void main(String[] args) throws Exception {

        Map testMap = new HashMap();
        testMap.put("lpf", "lpf");
        for (int i = 0; i < 100; i++) {
            testMap.put(i + "", i);
        }


        Hashtable hashtable = new Hashtable();
        hashtable.put("lpf","111");
        hashtable.put(null,"222");

        Map conMap = new ConcurrentHashMap();
        conMap.put("lpf", "lpf");

    }


    /**
     * 下面是jdk1.8版本中的hash算法，相比jdk1.7中少了三次位运算
     */
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
