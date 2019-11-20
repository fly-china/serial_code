package com.lpf.wio.client;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Http协议客户端
 * Date: 14-8-9
 * Time: 下午2:29
 * To change this template use File | Settings | File Templates.
 */
public class HttpClientUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    public static String sendHttp(String url, String data,String charset) throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        //请求超时时间  5S
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,5000);
        //读取超时时间  6S
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,6000);

        HttpPost httpPost = new HttpPost(url);

        HttpEntity entity = new StringEntity(data);
        httpPost.setEntity(entity);
        HttpResponse response = httpClient.execute(httpPost);
        StatusLine status = response.getStatusLine();
        if (HttpStatus.SC_OK != status.getStatusCode()) {
            logger.error("Http通讯失败,httpcode=" + status.getStatusCode());
            throw new Exception("Http通讯失败,httpcode=" + status.getStatusCode());
        }

        entity = response.getEntity();
        return EntityUtils.toString(entity, charset);
    }

    public static String httpGet(String url,String charset) throws Exception{
        HttpClient httpClient = new DefaultHttpClient();
        //请求超时时间  5S
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,5000);
        //读取超时时间  6S
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,6000);

        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = httpClient.execute(httpGet);
        StatusLine status = response.getStatusLine();
        if (HttpStatus.SC_OK != status.getStatusCode()) {
            logger.error("Http通讯失败,httpcode=" + status.getStatusCode());
            throw new Exception("Http通讯失败,httpcode=" + status.getStatusCode());
        }

        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity, charset);
    }

}
