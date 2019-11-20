package com.lpf.wio.client;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.ConnectionPoolTimeoutException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.KeyStore;

/**
 * Created with IntelliJ IDEA.
 * Date: 15-3-30
 * Time: 下午3:16
 * To change this template use File | Settings | File Templates.
 */
public class HttpsClientDbCheckSSL{

    private static Logger logger = LoggerFactory.getLogger(HttpsClientDbCheckSSL.class);

    private static CloseableHttpClient httpClient;// = buildHttpClient();
    // 连接超时时间，默认10秒
    private static int socketTimeout = 5000;
    // 传输超时时间，默认30秒
    private static int connectTimeout = 5000;
    private static int requestTimeout = 5000;

    public static CloseableHttpClient buildHttpClient(DBSSLCheckConfig dbsslCheckConfig) {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            FileInputStream instream = new FileInputStream(new File(
                    dbsslCheckConfig.getSslKeyStorePath()));// 加载本地的证书进行https加密传输
            try {
                keyStore.load(instream,dbsslCheckConfig.getSslKeyStorePassword().toCharArray());// 设置证书密码
            } finally {
                instream.close();
            }
            // Trust own CA and all self-signed certs
            SSLContext sslcontext = SSLContexts
                    .custom()
                    .loadKeyMaterial(keyStore,
                            dbsslCheckConfig.getSslKeyStorePassword().toCharArray()).build();
            // Allow TLSv1 protocol only
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[] { "TLSv1" },
                    null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(connectTimeout)
                    .setConnectionRequestTimeout(requestTimeout)
                    .setSocketTimeout(socketTimeout).build();
            httpClient = HttpClients.custom()
                    .setDefaultRequestConfig(requestConfig)
                    .setSSLSocketFactory(sslsf).build();
            return httpClient;
        } catch (Exception e) {
            throw new RuntimeException("error create httpclient......", e);
        }
    }

    public static String doGet(DBSSLCheckConfig dbsslCheckConfig,String url,String reportContent,String charset) throws Exception {
       //创建https链接
        httpClient = buildHttpClient(dbsslCheckConfig);

        HttpGet httpget = new HttpGet(url);
        try {
            logger.debug("Executing request " + httpget.getRequestLine());
            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                @Override
                public String handleResponse(final HttpResponse response)
                        throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity)
                                : null;
                    } else {
                        throw new ClientProtocolException(
                                "Unexpected response status: " + status);
                    }
                }
            };
            return httpClient.execute(httpget, responseHandler);
        } finally {
            httpget.releaseConnection();
        }
    }

    public static String doPostXML(DBSSLCheckConfig dbsslCheckConfig,String url,String reportContent,String charset) {

        //创建https链接
        httpClient = buildHttpClient(dbsslCheckConfig);

        String result = null;
        HttpPost httpPost = new HttpPost(url);

        // 得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
        StringEntity postEntity = new StringEntity(reportContent,charset);
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.setEntity(postEntity);
        // 设置请求器的配置
        logger.info("executing request" + httpPost.getRequestLine());
        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, charset);
        } catch (ConnectionPoolTimeoutException e) {
            logger.error(
                    "http post throw ConnectionPoolTimeoutException(wait time out)",
                    e);
        } catch (ConnectTimeoutException e) {
            logger.error("http post throw ConnectTimeoutException", e);
        } catch (SocketTimeoutException e) {
            logger.error("http post throw SocketTimeoutException", e);
        } catch (Exception e) {
            logger.error("http post throw Exception", e);
        } finally {
            httpPost.abort();
        }
        return result;
    }








    /*

    public static String sendHttps(DBSSLCheckConfig dbsslCheckConfig,String url,String reportContent,String charset) throws Exception{
        System.setProperty("javax.net.ssl.keyStore", dbsslCheckConfig.getSslKeyStorePath());
        System.setProperty("javax.net.ssl.keyStorePassword",dbsslCheckConfig.getSslKeyStorePassword());
        System.setProperty("javax.net.ssl.keyStoreType", dbsslCheckConfig.getSslKeyStoreType());
        System.setProperty("java.protocol.handler.pkgs", "sun.net.www.protocol");

        URL r_url = null;
        HttpsURLConnection urlConn = null;
        try {
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    return true;
                }
            };
            r_url = new URL(url);
            urlConn = (HttpsURLConnection) r_url.openConnection();
            urlConn.setHostnameVerifier(hv);
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);
            urlConn.setRequestProperty("Charset", charset);
            urlConn.setRequestMethod("POST");
            OutputStream out = urlConn.getOutputStream();
            out.write(reportContent.getBytes(charset));
            out.flush();

            int status = urlConn.getResponseCode();
            //System.out.println("请求返回httpStatus为:"+status);
        } catch (Exception e) {
            logger.error("发送Https请求异常" + e);
        }

        StringBuffer buffer = new StringBuffer();
        InputStream input = null;
        try {
            if(urlConn != null){
                input = urlConn.getInputStream();
                byte[] bt = new byte[1024];
                int length = -1;
                while ((length = input.read(bt)) != -1) {
                    buffer.append(new String(bt, 0, length, charset));
                }
            }
        } catch (Exception e) {
            logger.error("接收Https响应数据异常" + e);
        }finally{
            if(input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    logger.error("Https数据接收完毕后，关闭流异常" + e);
                }
            }
        }

        String data = buffer.toString();
        logger.info("Https 响应内容:"+data);

        return data;
    }

    public static void main(String[] args) throws Exception{
        DBSSLCheckConfig dbsslCheckConfig = new DBSSLCheckConfig();
        dbsslCheckConfig.setSslKeyStorePath("E:/apiclient_cert.p12");
        dbsslCheckConfig.setSslKeyStorePassword("1233019102");
        dbsslCheckConfig.setSslKeyStoreType("PKCS12");
        String responseText = sendHttps(dbsslCheckConfig,"https://juhe.mch.weixin.qq.com/secapi/pay/refund","<xml>\n" +
                "    <appid>wxa44e08a3cf8b560d</appid>\n" +
                "    <mch_id>1233019102</mch_id>\n" +
                "    <nonce_str>577fd60255d4bb0f466464849ffe6d8e</nonce_str>\n" +
                "    <sign>0B98C9DB91745FC343509B55A9E73359</sign>\n" +
                "    <transaction_id>1004320818201503120030750555</transaction_id>\n" +
                "    <out_trade_no>2000105009330315</out_trade_no>\n" +
                "    <out_refund_no>201503302000105100301591</out_refund_no>\n" +
                "    <total_fee>10</total_fee>\n" +
                "    <refund_fee>10</refund_fee>\n" +
                "    <refund_fee_type>CNY</refund_fee_type>\n" +
                "    <op_user_id>1233019102</op_user_id>\n" +
                "</xml>","UTF-8");
        System.out.println(responseText);
    }
    */
}