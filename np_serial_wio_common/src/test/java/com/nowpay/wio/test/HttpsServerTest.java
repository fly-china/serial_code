package com.nowpay.wio.test;

import com.nowpay.wio.server.entity.HttpsServerConfig;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Https协议服务器
 * User: 韩彦伟
 * Date: 14-8-6
 * Time: 上午11:05
 * To change this template use File | Settings | File Templates.
 */
public class HttpsServerTest {

    private Logger logger = LoggerFactory.getLogger(HttpsServerTest.class);

    public static void main(String[] args) {
        new HttpsServerTest().startServer();
    }

    public void startServer() {
        // 配置服务器-使用java线程池作为解释线程
        ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        // 设置 pipeline factory.
        bootstrap.setPipelineFactory(new HttpsPipelineFactory());

        InetSocketAddress inetSocketAddress = new InetSocketAddress(16666);
        // 绑定端口
        bootstrap.bind(inetSocketAddress);

        logger.info("HttpsServerTest start on 16666");
    }

    private static class HttpsPipelineFactory implements ChannelPipelineFactory {

        public ChannelPipeline getPipeline() throws Exception {
            // Create a default pipeline implementation.
            ChannelPipeline pipeline = Channels.pipeline();

            pipeline.addLast("decoder", new HttpRequestDecoder());
            pipeline.addLast("encoder", new HttpResponseEncoder());
            //http处理handler
            pipeline.addLast("handler", new HttpsNettyHandlerTest());

            return pipeline;
        }
    }
}
