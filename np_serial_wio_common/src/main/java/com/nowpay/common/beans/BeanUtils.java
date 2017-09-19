package com.nowpay.common.beans;

import org.apache.commons.lang3.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * BeanUtils
 * User: 韩彦伟
 * Date: 14-8-14
 * Time: 上午11:42
 * To change this template use File | Settings | File Templates.
 */
public class BeanUtils {

    /**
     * 转换成报文Map
     * @param bean
     * @return
     */
    public static Map<String,Object> convertToMap(Object bean){
        Map<String,Object> result = new HashMap<String, Object>();
        if(bean == null) return null;

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for(PropertyDescriptor propertyDescriptor : propertyDescriptors){
                String fieldName = propertyDescriptor.getName();
                Method readMethod = propertyDescriptor.getReadMethod();
                Object valueObj = readMethod.invoke(bean);

                result.put(fieldName,valueObj);
            }
        }catch (Exception ex){
            // ignore
        }

        return result;
    }

    /**
     * 转换成报文Map
     * @param bean
     * @return
     */
    public static Map<String,String> convertToReportMap(Object bean){
        Map<String,String> result = new HashMap<String, String>();
        if(bean == null) return null;

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for(PropertyDescriptor propertyDescriptor : propertyDescriptors){
                String fieldName = propertyDescriptor.getName();

                if(SpecialFieldContainer.contains(fieldName)) continue;

                Method readMethod = propertyDescriptor.getReadMethod();
                Object valueObj = readMethod.invoke(bean);
                String value = "";
                if(valueObj != null)
                    value = valueObj.toString();

                if(StringUtils.isNotBlank(value))
                    result.put(fieldName,value);
            }
        }catch (Exception ex){
            // ignore
        }

        return result;
    }
}
