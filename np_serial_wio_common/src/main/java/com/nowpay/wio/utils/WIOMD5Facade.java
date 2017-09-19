package com.nowpay.wio.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.*;

import com.nowpay.common.beans.SpecialFieldContainer;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nowpay.common.security.MD5;

/**
 * WIOMD5Facade  ---  汇集多类型MD5签名逻辑
 * User: 韩彦伟
 * Date: 14-8-6
 * Time: 下午7:52
 * To change this template use File | Settings | File Templates.
 */
public class WIOMD5Facade {
    private final static Logger LOGGER = LoggerFactory.getLogger(WIOMD5Facade.class);

    /**
     * 针对NowPay目前统一的MD5签名方式：key1=value1&key2=value2....keyn=valuen&securityKeySignature  进行MD5
     * @param dataMap  --需要参与MD5签名的数据
     * @param securityKey    --密钥
     * @return
     */
    public static boolean validateFormDataParamMD5(Map<String,String> dataMap,String securityKey,String currentSignature){
        if(MapUtils.isEmpty(dataMap)) return false;

        Set<String> keySet = dataMap.keySet();
        List<String> keyList = new ArrayList<String>(keySet);
        Collections.sort(keyList);

        StringBuilder toMD5StringBuilder = new StringBuilder();
        for(String key : keyList){
            String value = dataMap.get(key);

            if(StringUtils.isNotBlank(value)){
                toMD5StringBuilder.append(key+"="+ value+"&");
            }
        }

        try{
            String securityKeyMD5 = MD5.md5(securityKey,"");
            toMD5StringBuilder.append(securityKeyMD5);

            String toMD5String = toMD5StringBuilder.toString();

            String actualMD5Value = MD5.md5(toMD5String,"");

            return actualMD5Value.equals(currentSignature);
        }catch (Exception ex){
            return false;
        }
    }

    
    public static boolean validateParamMD5NOPWD(Map<String,String> dataMap,String securityKey,String currentSignature){
        if(MapUtils.isEmpty(dataMap)) return false;

        Set<String> keySet = dataMap.keySet();
        List<String> keyList = new ArrayList<String>(keySet);
        Collections.sort(keyList);

        StringBuilder toMD5StringBuilder = new StringBuilder();
        for(String key : keyList){
            String value = dataMap.get(key);

            if(StringUtils.isNotBlank(value)){
                toMD5StringBuilder.append(key+"="+ value+"&");
            }
        }
        if(toMD5StringBuilder.length()>0){
        	toMD5StringBuilder.deleteCharAt(toMD5StringBuilder.lastIndexOf("&"));
        }

        try{
            toMD5StringBuilder.append(securityKey);

            String toMD5String = toMD5StringBuilder.toString();

            String actualMD5Value = MD5.md5(toMD5String,"");

            return actualMD5Value.equals(currentSignature);
        }catch (Exception ex){
            return false;
        }
    }

    public static boolean validateParamMD5NOPWD(Map<String,String> dataMap,String securityKey,String currentSignature,String charset){
        if(MapUtils.isEmpty(dataMap)) return false;

        Set<String> keySet = dataMap.keySet();
        List<String> keyList = new ArrayList<String>(keySet);
        Collections.sort(keyList);

        StringBuilder toMD5StringBuilder = new StringBuilder();
        for(String key : keyList){
            String value = dataMap.get(key);

            if(StringUtils.isNotBlank(value)){
                toMD5StringBuilder.append(key+"="+ value+"&");
            }
        }
        if(toMD5StringBuilder.length()>0){
            toMD5StringBuilder.deleteCharAt(toMD5StringBuilder.lastIndexOf("&"));
        }

        try{
            toMD5StringBuilder.append(securityKey);

            String toMD5String = toMD5StringBuilder.toString();

            String actualMD5Value = MD5.md5(toMD5String,charset);

            return actualMD5Value.equals(currentSignature);
        }catch (Exception ex){
            return false;
        }
    }



    /**
     * 针对NowPay目前统一的MD5签名方式：key1=value1&key2=value2....keyn=valuen&securityKeySignature  进行MD5
     * <p>要先对Key进行按字典升序排序
     * @param dataMap  -- 数据
     * @param securityKey    --密钥
     * @param charset
     * @return
     */
    public static String getFormDataParamMD5(Map<String,String> dataMap,String securityKey,String charset){
        if(MapUtils.isEmpty(dataMap)) return null;

        Set<String> keySet = dataMap.keySet();
        List<String> keyList = new ArrayList<String>(keySet);
        Collections.sort(keyList);

        StringBuilder toMD5StringBuilder = new StringBuilder();
        for(String key : keyList){
            String value = dataMap.get(key);

            if(StringUtils.isNotBlank(value)){
                toMD5StringBuilder.append(key+"="+ value+"&");
            }
        }

        try{
            String securityKeyMD5 = MD5.md5(securityKey,charset);
            toMD5StringBuilder.append(securityKeyMD5);

            String toMD5String = toMD5StringBuilder.toString();
            LOGGER.debug("待MD5签名字符串："+toMD5String);

            String lastMD5Result = MD5.md5(toMD5String,charset);
            LOGGER.debug("MD5签名后字符串:"+lastMD5Result);

            return lastMD5Result;
        }catch (Exception ex){
            //ignore
            return "";
        }
    }
    
    /**
    * @Title: md5签名
    * @Description: 可以自定义参数分隔符
    * @param  
    * @return String 返回类型
    */
    public static String getFormDataParamMD5(Map<String,String> dataMap,String securityKey,String charset,String splitor){
        if(MapUtils.isEmpty(dataMap)) return null;

        Set<String> keySet = dataMap.keySet();
        List<String> keyList = new ArrayList<String>(keySet);
        Collections.sort(keyList);

        StringBuilder toMD5StringBuilder = new StringBuilder();
        for(String key : keyList){
            String value = dataMap.get(key);

            if(StringUtils.isNotBlank(value)){
                toMD5StringBuilder.append(key+"="+ value+"&");
            }
        }

        try{
            String securityKeyMD5 = MD5.md5(securityKey,charset);
            toMD5StringBuilder.append(securityKeyMD5);

            String toMD5String = toMD5StringBuilder.toString();
            LOGGER.debug("待MD5签名字符串："+toMD5String);

            String lastMD5Result = MD5.md5(toMD5String,charset);
            LOGGER.debug("MD5签名后字符串:"+lastMD5Result);

            return lastMD5Result;
        }catch (Exception ex){
            //ignore
            return "";
        }
    }

    public static String getFormDataParamMD5(Object object,String securityKey,String charset){
        Map<String,String> dataMap = new HashMap<String, String>();

        try{
            BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for(PropertyDescriptor propertyDescriptor : propertyDescriptors){
                String fieldName = propertyDescriptor.getName();
                if(SpecialFieldContainer.contains(fieldName))
                    continue;

                Method readMethod = propertyDescriptor.getReadMethod();
                Object valueObject = readMethod.invoke(object);

                if(valueObject != null){
                    dataMap.put(fieldName,valueObject.toString());
                }
            }

            return getFormDataParamMD5(dataMap,securityKey,charset);
        }catch (Exception ex){
            //ignore
            return "";
        }
    }
    
    /**
    * @Title: 将beanInfo转换为map
    * @Description: 将beanIfo转换为map
    * @param  
    * @return Map<String,String> 返回类型
    * @Author 赵晓春 
    * @date 2014.12.09
    */
    public static Map<String,String> getFormDataParam(Object object){
    	 Map<String,String> dataMap = new HashMap<String, String>();

         try{
             BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
             PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
             for(PropertyDescriptor propertyDescriptor : propertyDescriptors){
                 String fieldName = propertyDescriptor.getName();
                 if(SpecialFieldContainer.contains(fieldName))
                     continue;

                 Method readMethod = propertyDescriptor.getReadMethod();
                 Object valueObject = readMethod.invoke(object);

                 if(valueObject != null){
                     dataMap.put(fieldName,valueObject.toString());
                 }
             }
         }catch(Exception e){
        	 e.printStackTrace();
         }
         return dataMap;

    }

    /**
     * 对map中的value进行空格处理
     * @Auth zhaoxiaochun
     * @date 2015.06.26
     * @param contentData
     * @return　签名后的map对象
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> fileterData(Map<String, ?> contentData) {
        Map.Entry<String, String> obj = null;
        Map<String, String> submitFromData = new HashMap<String, String>();
        for (Iterator<?> it = contentData.entrySet().iterator(); it.hasNext();) {
            obj = (Map.Entry<String, String>) it.next();
            String value = obj.getValue();
            if (StringUtils.isNotBlank(value)) {
                // 对value值进行去除前后空处理
                submitFromData.put(obj.getKey(), value.trim());
            }
        }
        return submitFromData;
    }

    public static void main(String[] args) {
        boolean isEqual = "funcode=POSN001&nowPayOrderNo=2000105100443979&posMchNo=123456789012345&posTransDay=2015-04-03&refrenceNo=104122233268&terminalNo=12345679&tradeStatus=Y&e7879f069564bcfc12f44d21b4908bd2".equals("funcode=POSN001&nowPayOrderNo=2000105100443979&posMchNo=123456789012345&posTransDay=2015-04-03&refrenceNo=104122233268&terminalNo=12345679&tradeStatus=Y&e7879f069564bcfc12f44d21b4908bd2");
        System.out.print(isEqual);
    }
}