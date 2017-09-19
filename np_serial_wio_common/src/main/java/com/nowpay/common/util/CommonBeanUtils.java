package com.nowpay.common.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Bean工具类
 * User: 韩彦伟
 * Date: 14-8-29
 * Time: 下午6:03
 * To change this template use File | Settings | File Templates.
 */
public class CommonBeanUtils {

    public static Map<String, Object> toMap(Object bean){
        if(bean == null)
            return new HashMap<String, Object>();

        if(bean instanceof Map) return (Map<String, Object>) bean;

        Map<String, Object> key_value_map = new HashMap<String, Object>();

        try{
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for(PropertyDescriptor propertyDescriptor : propertyDescriptors){
                Method readMethod = propertyDescriptor.getReadMethod();
                key_value_map.put(propertyDescriptor.getName(), readMethod.invoke(bean));
            }
        }catch(Exception ex){
            throw new RuntimeException(ex.getMessage());
        }

        return key_value_map;
    }

}
