package com.nowpay.wio.server.entity;

/**
 * HTTPS协议服务端配置
 * User: 韩彦伟
 * Date: 14-8-6
 * Time: 上午10:28
 * To change this template use File | Settings | File Templates.
 */
public interface HttpsServerConfig extends WIOServerConfig{

    public boolean isCheckSSL();


}
