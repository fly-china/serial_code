package com.lpf.serial.controller;

/**
 * WIOServer常量
 * Date: 14-8-7
 * Time: 上午9:35
 * To change this template use File | Settings | File Templates.
 */
public interface IWIOServerConstants {

    /**
     * 关键域字段--业务码
     */
    String FIELD_FUNDCODE = "funcode";

    /**
     * 商户字符集
     */
    String FIELD_MHTCHARSET = "mhtCharset";

    /**
     * 分隔符-- 表单型数据的多域分隔符
     */
    String DILIMITER_FORMDATA_MD = "&";

    /**
     * 分隔符-- 表单型数据的KV分隔符
     */
    String DILIMITER_FORMDATA_KV = "=";

    /**
     * 字符集- UTF-8
     */
    String CHARSET_UTF8 = "UTF-8";

    /**
     * 字符集- GBK
     */
    String CHARSET_GBK = "GBK";
}