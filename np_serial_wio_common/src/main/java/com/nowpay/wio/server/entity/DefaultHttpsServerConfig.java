package com.nowpay.wio.server.entity;

import com.nowpay.wio.server.protocol.IWIOServiceProvider;

import java.net.InetSocketAddress;

/**
 * 缺省的HttpsServerConfig
 * User: 韩彦伟
 * Date: 14-8-6
 * Time: 下午2:16
 * To change this template use File | Settings | File Templates.
 */
public class DefaultHttpsServerConfig implements HttpsServerConfig {
    private String serverName;

    private boolean isCheckSSL;

    private InetSocketAddress inetAddress;

    private String charset;

    private IWIOServiceProvider serviceProvider;

    public DefaultHttpsServerConfig(InetSocketAddress inetAddress,String charset,IWIOServiceProvider serviceProvider){
        this.inetAddress = inetAddress;
        this.charset = charset;
        this.serviceProvider = serviceProvider;
    }

    public DefaultHttpsServerConfig(InetSocketAddress inetAddress,String charset,IWIOServiceProvider serviceProvider,boolean isCheckSSL){
        this.inetAddress = inetAddress;
        this.charset = charset;
        this.serviceProvider = serviceProvider;
        this.isCheckSSL = isCheckSSL;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public boolean isCheckSSL() {
        return false;
    }

    @Override
    public InetSocketAddress getInetAddress() {
        return this.inetAddress;
    }

    @Override
    public String getCharset() {
        return this.charset;
    }

    @Override
    public IWIOServiceProvider getServiceProvider() {
        return this.serviceProvider;
    }
}
