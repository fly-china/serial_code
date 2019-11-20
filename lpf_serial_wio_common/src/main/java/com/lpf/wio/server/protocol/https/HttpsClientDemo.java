package com.lpf.wio.server.protocol.https;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Date: 14-8-6
 * Time: 下午2:41
 * To change this template use File | Settings | File Templates.
 */
public class HttpsClientDemo {

    public static void main(String[] args) throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://122.49.35.82:10800/");
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("funcode", "B001"));
        nvps.add(new BasicNameValuePair("appId", "appId"));
        nvps.add(new BasicNameValuePair("mhtOrderNo", "mhtOrderNo"));
        nvps.add(new BasicNameValuePair("mhtOrderName", "B001"));
        nvps.add(new BasicNameValuePair("mhtOrderType", "mhtOrderType"));
        nvps.add(new BasicNameValuePair("mhtCurrencyType", "mhtCurrencyType"));
        nvps.add(new BasicNameValuePair("mhtOrderAmt", "mhtOrderAmt"));
        nvps.add(new BasicNameValuePair("mhtOrderDetail", "mhtOrderDetail"));
        nvps.add(new BasicNameValuePair("mhtOrderTimeOut", "mhtOrderTimeOut"));
        nvps.add(new BasicNameValuePair("mhtOrderStartTime", "mhtOrderStartTime"));
        nvps.add(new BasicNameValuePair("notifyUrl", "notifyUrl"));
        nvps.add(new BasicNameValuePair("mhtCharset", "UTF-8"));
        nvps.add(new BasicNameValuePair("payChannelType", "payChannelType"));
        nvps.add(new BasicNameValuePair("mhtSignType", "mhtSignType"));
        nvps.add(new BasicNameValuePair("mhtSignature", "mhtSignature"));

        httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        HttpResponse httpResponse = httpClient.execute(httpPost);
        HttpEntity httpEntity = httpResponse.getEntity();
        String body = EntityUtils.toString(httpEntity);
        body = URLDecoder.decode(body, "UTF-8");
        System.out.println(body);
    }
}
