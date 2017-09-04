package com.ipaynow.serial.controller;

import com.nowpay.devplat.common.util.PropertiesUtil;
import com.nowpay.wio.server.entity.DefaultHttpsServerConfig;
import com.nowpay.wio.server.protocol.https.HttpsServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;

/**
 * 中小开发者的HTTPS服务端
 * User: 韩彦伟
 * Date: 14-8-7
 * Time: 下午2:51
 * To change this template use File | Settings | File Templates.
 */
@Service
public class BizHttpsServer implements InitializingBean{

    private static final Logger logger = LoggerFactory.getLogger(BizHttpsServer.class);
    public void start(){
        PropertiesUtil propertiesUtil = PropertiesUtil.load("sdk.properties");
        String port = propertiesUtil.getProValue("sdk.https.port");
        String charset = propertiesUtil.getProValue("sdk.https.charset");
        InetSocketAddress inetAddress = new InetSocketAddress(Integer.parseInt(port));
        DefaultHttpsServerConfig defaultHttpsServerConfig = new DefaultHttpsServerConfig(inetAddress,charset,new BizHttpsService());
        defaultHttpsServerConfig.setServerName("中小开发者Https服务");

        HttpsServer httpsServer = new HttpsServer(defaultHttpsServerConfig);
        httpsServer.startServer();
    }

    public static void main(String[] args) {
        new BizHttpsServer().start();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        start();
        logger.info("BizHttpsServer启动完毕");
    }
}
