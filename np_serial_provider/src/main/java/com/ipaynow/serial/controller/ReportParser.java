package com.ipaynow.serial.controller;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 报文解析器
 * User: 韩彦伟
 * Date: 14-8-7
 * Time: 上午9:44
 * To change this template use File | Settings | File Templates.
 */
public class ReportParser {

    /**
     * 表单类型报文解析
     * @param reportContent
     * @param charset
     * @return
     */
    public static Map<String,String> parseFormDataPatternReport(String reportContent,String charset) throws Exception{
        if(StringUtils.isBlank(reportContent)) return null;

        String[] domainArray = reportContent.split(IWIOServerConstants.DILIMITER_FORMDATA_MD);

        Map<String,String> key_value_map = new HashMap<String, String>();
        for(String domain : domainArray){
            String[] kvArray = domain.split(IWIOServerConstants.DILIMITER_FORMDATA_KV);

            if(kvArray.length == 2){
                String decodeString = URLDecoder.decode(kvArray[1], charset);
                String lastInnerValue = new String(decodeString.getBytes(charset), DevPlatTradeConstants.DEFAULT_CHARSET);

                if(StringUtils.isNotBlank(lastInnerValue))
                    key_value_map.put(kvArray[0], lastInnerValue);
            }
        }

        return key_value_map;
    }

    public static String formatFormDataPatternResponse(Map<String,String> dataMap,String charset) throws Exception{
        if(MapUtils.isEmpty(dataMap)) return null;

        if(StringUtils.isBlank(charset)) charset = DevPlatTradeConstants.DEFAULT_CHARSET;

        StringBuilder responseContentBuilder = new StringBuilder();
        Set<String> keySet = dataMap.keySet();
        for(String key : keySet){
            String value = dataMap.get(key);
            if(StringUtils.isNotBlank(value))
                value = URLEncoder.encode(value,charset);
            responseContentBuilder.append(key+IWIOServerConstants.DILIMITER_FORMDATA_KV+value+IWIOServerConstants.DILIMITER_FORMDATA_MD);
        }

        responseContentBuilder.deleteCharAt(responseContentBuilder.lastIndexOf(IWIOServerConstants.DILIMITER_FORMDATA_MD));

        return responseContentBuilder.toString();
    }
}
