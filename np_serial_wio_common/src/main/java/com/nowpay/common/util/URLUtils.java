package com.nowpay.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * URL工具类
 * User: 韩彦伟
 * Date: 14-8-17
 * Time: 上午10:57
 * To change this template use File | Settings | File Templates.
 */
public class URLUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(URLUtils.class);

    public static String urldecode(String originString,String charset){
        try{
            return URLDecoder.decode(originString,charset);
        }catch (Exception ex){
            LOGGER.error("urldecode error-->");
        }

        return originString;
    }

    public static String urlencode(String originString,String charset){
        try{
            return URLEncoder.encode(originString, charset);
        }catch (Exception ex){
            LOGGER.error("URLEncoder error-->");
        }

        return originString;
    }
}
