package com.nowpay.wio.server.entity;

import com.nowpay.wio.server.protocol.IWIOServiceProvider;

import java.net.InetSocketAddress;

/**
 * 网络通讯服务端配置
 * User: 韩彦伟
 * Date: 14-8-6
 * Time: 上午10:18
 * To change this template use File | Settings | File Templates.
 */
public interface WIOServerConfig {

    public String getServerName();

    public InetSocketAddress getInetAddress();

    public String getCharset();

    public IWIOServiceProvider getServiceProvider();
}
