package com.lpf.common.beans;

import java.util.HashSet;
import java.util.Set;

/**
 * 特殊字段容器
 * Date: 14-8-10
 * Time: 下午12:46
 * To change this template use File | Settings | File Templates.
 */
public class SpecialFieldContainer {

    public final static String NAME_CLASS = "class";

    public final static String NAME_SERIALVERSIONUID = "serialVersionUID";

    private static Set<String> container = new HashSet<String>();
    static {
        container.add(NAME_CLASS);
        container.add(NAME_SERIALVERSIONUID);
    }

    public static boolean contains(String fieldName){
        if(fieldName == null) return false;

        return container.contains(fieldName);
    }
}
