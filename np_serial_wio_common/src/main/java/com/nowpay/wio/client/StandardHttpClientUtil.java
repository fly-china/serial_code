package com.nowpay.wio.client;

import org.apache.commons.collections.MapUtils;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Set;

/**
 * 所有的Https单向和双向认证工具类<p>如果不满足要求的在此类进行补充，但一定要经过严格测试
 * User: 韩彦伟
 * Date: 15-12-12
 * Time: 下午4:03
 * To change this template use File | Settings | File Templates.
 */
public class StandardHttpClientUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(StandardHttpClientUtil.class);

    public static String sendHttpsPost(String url,String reportContent,String charset){
        //记录开始时间
        long start = System.currentTimeMillis();
        CloseableHttpClient closeableHttpClient = null;
        CloseableHttpResponse response = null;
        try{
            closeableHttpClient = createSSLClientDefault();

            //请求参数设置
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(6000).build();
            httpPost.setProtocolVersion(HttpVersion.HTTP_1_1);
            httpPost.setConfig(requestConfig);

            //发送请求
            httpPost.setEntity(new StringEntity(reportContent, charset));
            response = closeableHttpClient.execute(httpPost);

            //处理响应
            StatusLine statusLine = response.getStatusLine();
            String responseContent = EntityUtils.toString(response.getEntity(),charset);

            if(200 == statusLine.getStatusCode()){
                LOGGER.info("200 OK,apacheHttpsClient响应内容为：" + responseContent);
            }else {
                LOGGER.info("apacheHttpsClient请求失败,响应内容为：" + responseContent);
            }
            long end = System.currentTimeMillis();
            LOGGER.info("apacheHttpsClient请求耗时:" + (end-start) + "毫秒");
            return responseContent;
        }catch (Exception ex){
            LOGGER.error("apacheHttpsClient请求异常----->",ex);
        }finally {
            try{
                if(response != null)
                    response.close();
                if(closeableHttpClient != null)
                    closeableHttpClient.close();
            }catch (Exception ex){
                LOGGER.error("apacheHttpsClient调用--关闭流失败",ex);
            }
        }

        return null;
    }

    public static String sendHttpsPostSupport302(String url,String reportContent,String charset){
        //记录开始时间
        long start = System.currentTimeMillis();
        CloseableHttpClient closeableHttpClient = null;
        CloseableHttpResponse response = null;
        try{
            closeableHttpClient = createSSLClientDefault();

            //请求参数设置
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(6000).build();
            httpPost.setProtocolVersion(HttpVersion.HTTP_1_1);
            httpPost.setConfig(requestConfig);

            //发送请求
            httpPost.setEntity(new StringEntity(reportContent, charset));
            response = closeableHttpClient.execute(httpPost);

            //处理响应
            StatusLine statusLine = response.getStatusLine();
            String responseContent = EntityUtils.toString(response.getEntity(),charset);

            if(200 == statusLine.getStatusCode()){
                LOGGER.info("200 OK,apacheHttpsClient响应内容为：" + responseContent);
            }else if(302 == statusLine.getStatusCode()){
                return response.getFirstHeader("Location").getValue();
            }else{

                LOGGER.info("apacheHttpsClient请求失败,响应内容为：" + responseContent);
            }
            long end = System.currentTimeMillis();
            LOGGER.info("apacheHttpsClient请求耗时:" + (end-start) + "毫秒");
            return responseContent;
        }catch (Exception ex){
            LOGGER.error("apacheHttpsClient请求异常----->",ex);
        }finally {
            try{
                if(response != null)
                    response.close();
                if(closeableHttpClient != null)
                    closeableHttpClient.close();
            }catch (Exception ex){
                LOGGER.error("apacheHttpsClient调用--关闭流失败",ex);
            }
        }

        return null;
    }

    public static String sendHttpsPostSupport302WithHeader(String url,Map<String,String> headerMap,String reportContent,String charset){
        //记录开始时间
        long start = System.currentTimeMillis();
        CloseableHttpClient closeableHttpClient = null;
        CloseableHttpResponse response = null;
        try{
            closeableHttpClient = createSSLClientDefault();

            //请求参数设置
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(6000).build();
            httpPost.setProtocolVersion(HttpVersion.HTTP_1_1);
            httpPost.setConfig(requestConfig);

            if(MapUtils.isNotEmpty(headerMap)){
                Set<String> headerKeySet = headerMap.keySet();
                for(String headerKey:headerKeySet){
                    httpPost.setHeader(headerKey,headerMap.get(headerKey));
                }
            }

            //发送请求
            httpPost.setEntity(new StringEntity(reportContent, charset));
            response = closeableHttpClient.execute(httpPost);

            //处理响应
            StatusLine statusLine = response.getStatusLine();
            String responseContent = EntityUtils.toString(response.getEntity(),charset);

            if(200 == statusLine.getStatusCode()){
                LOGGER.info("200 OK,apacheHttpsClient响应内容为：" + responseContent);
            }else if(302 == statusLine.getStatusCode()){
                return response.getFirstHeader("Location").getValue();
            }else{

                LOGGER.info("apacheHttpsClient请求失败,响应内容为：" + responseContent);
            }
            long end = System.currentTimeMillis();
            LOGGER.info("apacheHttpsClient请求耗时:" + (end-start) + "毫秒");
            return responseContent;
        }catch (Exception ex){
            LOGGER.error("apacheHttpsClient请求异常----->",ex);
        }finally {
            try{
                if(response != null)
                    response.close();
                if(closeableHttpClient != null)
                    closeableHttpClient.close();
            }catch (Exception ex){
                LOGGER.error("apacheHttpsClient调用--关闭流失败",ex);
            }
        }

        return null;
    }

    private static CloseableHttpClient createSSLClientDefault() throws Exception{
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                //信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (Exception ex) {
            LOGGER.error("apacheHttpsClient创建CloseableHttpClient失败",ex);
            throw ex;
        }
    }

    /**
     * 重写验证方法，取消检测ssl
     */
    private static TrustManager truseAllManager = new X509TrustManager(){

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] arg0, String arg1)
                throws CertificateException {
            // TODO Auto-generated method stub

        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] arg0, String arg1)
                throws CertificateException {
            // TODO Auto-generated method stub

        }

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }

    };
}
