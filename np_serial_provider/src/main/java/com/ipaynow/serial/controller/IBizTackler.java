package com.ipaynow.serial.controller;

import java.util.Map;

/**
 * 接口服务端业务处理器
 * User: 韩彦伟
 * Date: 14-8-7
 * Time: 上午10:21
 * To change this template use File | Settings | File Templates.
 */
public interface IBizTackler {

    public String getFuncode();

    public String tackle(Map<String, String> dataMap, String charset, Map<String, String> headerMap);
}
