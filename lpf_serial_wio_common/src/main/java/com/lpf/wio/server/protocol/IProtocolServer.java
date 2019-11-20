package com.lpf.wio.server.protocol;

import com.lpf.wio.server.entity.WIOServerConfig;

/**
 * 协议服务器
 * Date: 14-8-6
 * Time: 上午10:49
 * To change this template use File | Settings | File Templates.
 */
public interface IProtocolServer {

    public WIOServerConfig getWIOServerConfig();

    public void startServer();
}
