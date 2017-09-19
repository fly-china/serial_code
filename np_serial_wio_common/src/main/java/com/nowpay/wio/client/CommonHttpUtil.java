package com.nowpay.wio.client;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map.Entry;

public class CommonHttpUtil {
	private static final String QSTRING_EQUAL = "=";
	private static final String QSTRING_SPLIT = "&";
	private static final String QURL_SPLIT = "?";
	private static final HttpConnectionManager connectionManager;

	private static final HttpClient client;
	private static Logger log = Logger.getLogger(CommonHttpUtil.class);
	static {

		HttpConnectionManagerParams params = loadHttpConfFromFile();

		connectionManager = new MultiThreadedHttpConnectionManager();

		connectionManager.setParams(params);

		client = new HttpClient(connectionManager);
	}

	private static HttpConnectionManagerParams loadHttpConfFromFile() {
		HttpConnectionManagerParams params = new HttpConnectionManagerParams();
		params.setConnectionTimeout(5000);
		params.setSoTimeout(6000);
		params.setStaleCheckingEnabled(true);
		params.setTcpNoDelay(true);
		params.setDefaultMaxConnectionsPerHost(100);
		params.setMaxTotalConnections(150);
		params.setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler(0, false));
		return params;
	}

	public static String post(String url, String content) {
		if(content==null){
			return null;
		}
		return post(url, "UTF-8", content);
	}

	public static String post(String url, String encode, String content) {
		try {
			return post(url, content.getBytes(encode));
		} catch (UnsupportedEncodingException e) {
			log.info("HTTP_POST encode error");
			return null;
		}
	}

	public static String post(String url, String encode,HashMap<String, String> content) {
		return post(url, encode, CreateLinkString(content));
	}

	public static String get(String url, String content) {
		return get(url + QURL_SPLIT + content);
	}

	public static String get(String url, HashMap<String, String> content) {
		return get(url, CreateLinkString(content));
	}


	private static String get(String url) {
		GetMethod method = new GetMethod(url);
		method.addRequestHeader("Connection", "Keep-Alive");
		method.getParams().setCookiePolicy(org.apache.commons.httpclient.cookie.CookiePolicy.IGNORE_COOKIES);
		method.addRequestHeader("Content-Type","application/x-www-form-urlencoded");
		try {
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				log.info("HTTP_GET net status error");
				return null;
			}
			return InputStream2String(method.getResponseBodyAsStream(), method.getResponseCharSet());
		} catch (SocketTimeoutException e) {
			log.info("HTTP_GET server get response timeout");
			return null;
		} catch (IOException e) {
			log.info("HTTP_GET get inputstream error");
			return null;
		}
	}

	private static String post(String url, byte[] content) {
		PostMethod method = new PostMethod(url);
		method.addRequestHeader("Connection", "Keep-Alive");
		method.getParams().setCookiePolicy(org.apache.commons.httpclient.cookie.CookiePolicy.IGNORE_COOKIES);
		method.setRequestEntity(new ByteArrayRequestEntity(content));
		method.addRequestHeader("Content-Type","application/x-www-form-urlencoded");
		try {
			int statusCode = client.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				log.info("HTTP_POST net status error");
				return null;
			}
			return InputStream2String(method.getResponseBodyAsStream(), method.getResponseCharSet());
		} catch (SocketTimeoutException e) {
			log.info("HTTP_POST server get response timeout");
			return null;
		} catch (IOException e) {
			log.info("HTTP_POST get inputstream error");
			return null;
		}
	}

	private static String CreateLinkString(HashMap<String, String> params){
		StringBuffer sb = new StringBuffer();
		for (Entry<String, String> pair : params.entrySet()) {
			sb.append(pair.getKey()).append(QSTRING_EQUAL).append(pair.getValue()).append(QSTRING_SPLIT);
		}
		//log.info("createLinkString:"+sb.substring(0, sb.length()-1).toString());
		return sb.substring(0, sb.length()-1).toString();
	}

	private static String InputStream2String(InputStream input, String encode) {
		BufferedReader br = null;
		try {
			if (encode == null || encode.equals("")) {
				encode="UTF-8";
			}
			br = new BufferedReader(new InputStreamReader(input, encode));
			StringBuffer sb = new StringBuffer();
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			log.info("Stream2String encode error");
			return null;
		} catch (IOException e) {
			log.info("Stream2String read error");
			return null;
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				log.info("Stream2String close error");
				return null;
			}
		}
	}
}
