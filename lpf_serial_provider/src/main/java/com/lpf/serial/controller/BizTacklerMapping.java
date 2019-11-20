package com.lpf.serial.controller;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务处理映射
 * Date: 14-8-7
 * Time: 下午2:16
 * To change this template use File | Settings | File Templates.
 */
public class BizTacklerMapping {

    private final static Map<String,IBizTackler> funcode_tackler_map = new HashMap<String, IBizTackler>();

    static{
        IBizTackler getSerialCodeTackler = new GetSerialCodeTackler();
        IBizTackler getPospSerialCodeTackler = new GetPospSerialCodeTackler();
        funcode_tackler_map.put(getSerialCodeTackler.getFuncode(),getSerialCodeTackler);
        funcode_tackler_map.put(getPospSerialCodeTackler.getFuncode(),getPospSerialCodeTackler);
    }

    /**
     * 先按funcode找，找不到再按URI找，再找不到就为空了
     * @param requestUri
     * @param funcode
     * @return
     */
    public static IBizTackler getTackler(String requestUri,String funcode){
        if(StringUtils.isNotBlank(funcode))
            return funcode_tackler_map.get(funcode);
        else if(StringUtils.isNotBlank(requestUri)){
            return funcode_tackler_map.get(requestUri);
        }else
            return null;
    }
}