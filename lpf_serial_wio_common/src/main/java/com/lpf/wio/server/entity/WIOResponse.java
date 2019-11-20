package com.lpf.wio.server.entity;

import java.util.Map;

/**
 * 网络通讯响应
 * Date: 14-8-6
 * Time: 上午10:52
 * To change this template use File | Settings | File Templates.
 */
public class WIOResponse {

    /**
     * 通讯响应码
     */
    private String responseCode;

    /**
     * 结果码
     */
    private String resultCode;

    /**
     * 结果数据Map
     */
    private Map<String,String> dataMap;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public Map<String, String> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, String> dataMap) {
        this.dataMap = dataMap;
    }
}
