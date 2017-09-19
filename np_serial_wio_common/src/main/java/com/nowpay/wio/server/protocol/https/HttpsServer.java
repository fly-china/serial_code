package com.nowpay.wio.server.protocol.https;

import com.nowpay.wio.server.entity.HttpsServerConfig;
import com.nowpay.wio.server.entity.WIOServerConfig;
import com.nowpay.wio.server.protocol.IProtocolServer;
import com.nowpay.wio.server.protocol.IWIOServiceProvider;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.ssl.SslHandler;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.util.HashedWheelTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLEngine;
import java.util.concurrent.Executors;

/**
 * Https协议服务器
 * User: 韩彦伟
 * Date: 14-8-6
 * Time: 上午11:05
 * To change this template use File | Settings | File Templates.
 */
public class HttpsServer implements IProtocolServer {

    private Logger logger = LoggerFactory.getLogger(HttpsServer.class);

    private HttpsServerConfig httpsServerConfig;

    //特别重要，需要设置默认的工作者数目，否则就会有默认cpu内核数*2
    private final static int nioChannelWorkerCount = 500;

    public HttpsServer(HttpsServerConfig httpsServerConfig){
        this.httpsServerConfig = httpsServerConfig;
    }

    @Override
    public WIOServerConfig getWIOServerConfig() {
        return this.httpsServerConfig;
    }

    @Override
    public void startServer() {
        // 配置服务器-使用java线程池作为解释线程
        ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool(),nioChannelWorkerCount));
        // 设置 pipeline factory.
        bootstrap.setPipelineFactory(new HttpsPipelineFactory((HttpsServerConfig) getWIOServerConfig()));

        //关闭纳格算法
        bootstrap.setOption("child.tcpNoDelay", true);
        //
        bootstrap.setOption("child.keepAlive", true);

        bootstrap.setOption("reuseAddress", true);

        //最大连接队列中的可承载量
        bootstrap.setOption("backlog",1000);

        // 绑定端口
        bootstrap.bind(getWIOServerConfig().getInetAddress());

        String serverName = httpsServerConfig.getServerName();
        if(serverName == null) serverName = "";
        logger.info(serverName+" HttpsServer start on "+getWIOServerConfig().getInetAddress().getPort());
    }

    private static class HttpsPipelineFactory implements ChannelPipelineFactory {

        private HttpsServerConfig httpsServerConfig;

        //一定设置成成员变量，千万不能放在getPipeline里面new 会内存泄露
        private HashedWheelTimer wheelTimer = new HashedWheelTimer();

        private final int request_maxInitialLineLength = 1024 * 64;//请求行限制在8K
        private final int request_maxHeaderSize = 1024 * 128; //请求报文头限制在16K
        private final int request_maxChunkSize = 1024 * 512;//报文体限制在80K

        private HttpsPipelineFactory(HttpsServerConfig httpsServerConfig){
            this.httpsServerConfig = httpsServerConfig;
        }

        public ChannelPipeline getPipeline() throws Exception {
            // Create a default pipeline implementation.
            ChannelPipeline pipeline = Channels.pipeline();

            //是否需要SSL
            if (httpsServerConfig.isCheckSSL()) {
                SSLEngine engine = SSLUtils.createServerSSLEngine("","",false);
                pipeline.addLast("ssl", new SslHandler(engine));
            }

            //设置超时时间,四个参数：Schedule触发器，读超时时间，写超时时间，读写超时时间
            pipeline.addLast("idleStateHandler",new IdleStateHandler(wheelTimer,20,20,10));


            pipeline.addLast("decoder", new HttpRequestDecoder(request_maxInitialLineLength,request_maxHeaderSize,request_maxChunkSize));
            pipeline.addLast("encoder", new HttpResponseEncoder());
            //http处理handler
            IWIOServiceProvider serviceProvider = httpsServerConfig.getServiceProvider();
            HttpsNettyHandler httpsNettyHandler = new HttpsNettyHandler(serviceProvider, httpsServerConfig.getCharset());
            pipeline.addLast("handler", httpsNettyHandler);

            return pipeline;
        }
    }
}
