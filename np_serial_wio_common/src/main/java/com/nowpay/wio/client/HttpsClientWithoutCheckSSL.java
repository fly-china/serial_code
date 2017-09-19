package com.nowpay.wio.client;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * Https客户端（不带SSL验证） ---有待修改，先暂时用着
 * User: 韩彦伟
 * Date: 14-8-9
 * Time: 下午2:20
 * To change this template use File | Settings | File Templates.
 */
public class HttpsClientWithoutCheckSSL implements X509TrustManager{

    private static Logger logger = LoggerFactory.getLogger(HttpsClientWithoutCheckSSL.class);

    public static String sendHttps(String url,String reportContent,String charset){
        if(StringUtils.isBlank(reportContent))
            reportContent = "";

        URL r_url = null;
        HttpsURLConnection urlConn = null;
        OutputStream out = null;
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[] { new HttpsClientWithoutCheckSSL() },new java.security.SecureRandom());
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    return true;
                }
            };
            r_url = new URL(url);
            urlConn = (HttpsURLConnection) r_url.openConnection();
            urlConn.setSSLSocketFactory(sc.getSocketFactory());
            urlConn.setHostnameVerifier(hv);
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);
            urlConn.setConnectTimeout(5000);
            urlConn.setReadTimeout(6000);
            urlConn.setRequestProperty("Charset", charset);
            urlConn.setRequestMethod("POST");
            out = urlConn.getOutputStream();
            out.write(reportContent.getBytes(charset));
            out.flush();

            int status = urlConn.getResponseCode();
            //System.out.println("请求返回httpStatus为:"+status);
        } catch (Exception e) {
            logger.error("发送Https请求异常" , e);
        }finally {
            try {
                if(out != null)
                    out.close();
            } catch (Exception e) {
                logger.error("关闭https网络流写流关闭失败",e);
            }
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
            logger.error("接收Https响应数据异常" , e);
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
    public static String sendHttpsWithState(String url,String reportContent,String charset){
        String error_code = "";
        if(StringUtils.isBlank(reportContent))
            reportContent = "";

        URL r_url = null;
        HttpsURLConnection urlConn = null;
        OutputStream out = null;
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[] { new HttpsClientWithoutCheckSSL() },new java.security.SecureRandom());
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    return true;
                }
            };
            r_url = new URL(url);
            urlConn = (HttpsURLConnection) r_url.openConnection();
            urlConn.setSSLSocketFactory(sc.getSocketFactory());
            urlConn.setHostnameVerifier(hv);
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);
            urlConn.setConnectTimeout(5000);
            urlConn.setReadTimeout(6000);
            urlConn.setRequestProperty("Charset", charset);
            urlConn.setRequestMethod("POST");
            out = urlConn.getOutputStream();
            out.write(reportContent.getBytes(charset));
            out.flush();

            int status = urlConn.getResponseCode();
            //System.out.println("请求返回httpStatus为:"+status);
            if (HttpStatus.SC_OK != status) {
                logger.error("Https通讯失败,httpscode=" + status);
                error_code = "success=N";
                throw new Exception("Https通讯失败,httpscode=" + status);
            }
        } catch (Exception e) {
            logger.error("发送Https请求异常" , e);
            error_code = "success=N";
        }finally {
            try {
                if(out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("关闭输出流发生异常",e);
            }
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
            error_code = "success=N";
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
        if(!StringUtils.isEmpty(error_code)){
            data = error_code + data;
        }
        logger.info("Https 响应内容:"+data);

        return data;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
