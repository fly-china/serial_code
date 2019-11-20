package com.lpf.wio.server.protocol;

import java.util.Map;

/**
 * WIO服务提供者
   <p>切记必须是无状态的， 不然会产生并发问题
 * Date: 14-8-7
 * Time: 上午8:30
 * To change this template use File | Settings | File Templates.
 */
public interface IWIOServiceProvider {

    /**
     * 核心服务类
     * @param headerMap
     * @param requestUri -- 请求的URI 比如：/favcon.icon
     * @param requestReport   --- 请求报文
     * @param charset --- 请求所用字符集
     * @return
     */
    String service(String requestUri,String requestReport,String charset,Map<String,String> headerMap);
}
