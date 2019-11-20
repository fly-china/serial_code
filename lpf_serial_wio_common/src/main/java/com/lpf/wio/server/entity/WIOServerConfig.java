package com.lpf.wio.server.entity;

import com.lpf.wio.server.protocol.IWIOServiceProvider;

import java.net.InetSocketAddress;

/**
 * 网络通讯服务端配置
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
