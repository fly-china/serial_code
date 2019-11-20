package com.lpf.wio.client;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Windows
 * Date: 14-8-9
 * Time: 下午7:52
 * To change this template use File | Settings | File Templates.
 */
public class HttpByteArrayClient {

    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    public static String sendHttp(String url, String data,String charset) throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        HttpEntity entity = new ByteArrayEntity(data.getBytes(charset));
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
}
