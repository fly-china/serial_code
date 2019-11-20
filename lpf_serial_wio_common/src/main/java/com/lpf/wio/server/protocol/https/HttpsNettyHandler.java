package com.lpf.wio.server.protocol.https;

import com.lpf.common.util.URLUtils;
import com.lpf.wio.server.protocol.IWIOServiceProvider;
import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.buffer.DynamicChannelBuffer;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;
import org.jboss.netty.handler.codec.http.*;
import org.jboss.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.*;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Netty处理器2--可以带协议头
 * Date: 15-1-26
 * Time: 上午9:39
 * To change this template use File | Settings | File Templates.
 */
public class HttpsNettyHandler extends SimpleChannelUpstreamHandler{
    private Logger logger = LoggerFactory.getLogger(HttpsNettyHandler.class);

    //响应报文最大长度16K
    private final static int response_content_length = 1024 * 16;

    private IWIOServiceProvider serviceProvider;

    private String charset;

    public HttpsNettyHandler(IWIOServiceProvider serviceProvider, String charset){
        this.serviceProvider = serviceProvider;
        this.charset = charset;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        logger.info("HttpsNettyHandler--请求消息接受开始");

        HttpRequest request = (HttpRequest) e.getMessage();
        logger.info("请求来自于" + request.getUri());
        String uri = request.getUri();
        logger.info("uri:"+uri);
        if(uri.contains("/favicon.ico")) {
            writeResponse(e,"Bad Request!");
            return;
        }

        int contentLength = (int) HttpHeaders.getContentLength(request);
        ChannelBuffer channelBuffer = request.getContent();
        byte[] contentByteArray = new byte[contentLength];
        for(int index=0;index < contentLength;index++){
            contentByteArray[index] = channelBuffer.getByte(index);
        }

        //请求报文
        String requestContent = "";
        if(contentLength >0){
            requestContent = new String(contentByteArray, charset);
        }else{
            //如果contentLength 为0则说明用的是Get方法，所以直接将URL后面的串取出来作为reqestContent
            int paramIndex = uri.indexOf("?");
            if(paramIndex >0)
                requestContent = uri.substring(paramIndex +1);
        }

        logger.info("收到请求报文：" +URLUtils.urldecode(requestContent,charset));
        if(StringUtils.isBlank(requestContent)) {
            writeResponse(e,"Bad Request!");
            return;
        }
        
      
        //添加必要的报文头
        Map<String,String> headerMap = buildHeader(e,request, requestContent);

        String responseContent = serviceProvider.service(uri,requestContent,charset,headerMap);
        logger.debug("响应报文："+ URLUtils.urldecode(responseContent,charset));

        writeResponse(e, responseContent);

        logger.info("HttpsNettyHandler--请求消息响应结束");

    }

    //回写返回信息
    private void writeResponse(MessageEvent e, String responseContent) throws UnsupportedEncodingException {
        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
        ChannelBuffer buffer = new DynamicChannelBuffer(response_content_length);
        buffer.writeBytes(responseContent.getBytes(charset));
        response.setContent(buffer);
        response.setHeader("Content-Type", "text/html;charset="+charset);
        response.setHeader("Content-Length", response.getContent().writerIndex());
        
        Channel ch = e.getChannel();
        try{
            // Write the initial line and the header.
            ChannelFuture channelFuture = ch.write(response);
            //只支持短连接，即写完后立即关闭连接
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        }catch (Throwable throwable){
            ch.disconnect();
            ch.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)  throws Exception {
        Channel ch = e.getChannel();
        Throwable cause = e.getCause();
        if (cause instanceof TooLongFrameException) {
            sendError(ctx, BAD_REQUEST);
            return;
        }

        cause.printStackTrace();
        if (ch.isConnected()) {
            sendError(ctx, INTERNAL_SERVER_ERROR);
        }
    }

    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, status);
        response.setHeader(CONTENT_TYPE, "text/plain;charset="+charset);
        response.setContent(ChannelBuffers.copiedBuffer("Failure:" + status.toString() + "\r\n", CharsetUtil.UTF_8));

        // Close the connection as soon as the error message is sent.
        ctx.getChannel().write(response).addListener(ChannelFutureListener.CLOSE);
    }

    private Map<String,String> buildHeader(MessageEvent messageEvent,HttpRequest request, String requestContent) {
        Map<String,String> headerMap = new HashMap<String, String>();
        headerMap.put(Constant.HEADER_FIELD_ACCEPT,request.getHeader(Constant.HEADER_FIELD_ACCEPT));
        headerMap.put(Constant.HEADER_FIELD_UserAgent,request.getHeader(Constant.HEADER_FIELD_UserAgent));
        logger.info("request:------------>"+request.getHeaders(Constant.HEADER_FIELD_UserAgent));
        request.setHeader(Constant.HEADER_FIELD_UserAgent, "");

        String remoteHostAddress = request.getHeader("X-Real-IP");  //for nginx
        if(StringUtils.isBlank(remoteHostAddress))
            remoteHostAddress = request.getHeader("X-Forwarded-For");//for nginx
        if(StringUtils.isBlank(remoteHostAddress)){ // for direct
            InetSocketAddress inetSocketAddress = (InetSocketAddress) messageEvent.getRemoteAddress();
            if(inetSocketAddress != null)
                remoteHostAddress = inetSocketAddress.getAddress().getHostAddress();
        }

        String remoteDomainName = request.getHeader("Host");
        logger.info("客户端主机IP---------->"+remoteHostAddress+",域名---->"+remoteDomainName);
        headerMap.put(Constant.HEADER_FIELD_RemoteIp, remoteHostAddress);
        headerMap.put(Constant.HEADER_FIELD_RemoteDomainName, remoteDomainName);
        
        return headerMap;
    }
}
