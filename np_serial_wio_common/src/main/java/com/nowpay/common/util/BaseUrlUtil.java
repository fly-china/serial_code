package com.nowpay.common.util;

import javax.servlet.http.HttpServletRequest;
public class BaseUrlUtil {

	/**
	 *<p>Description:从请求中获得服务器应用地址，使用时直接拼接action地址即可
	 *	 eg:本地测试时会返回:http://localhost:8080/fund_merchant/
	 *       服务器会返回:http://域名或者服务器IP:服务器端口/fund_merchant/
	 *</p>
	 */
	public static String getBasePath(HttpServletRequest request){
		String path = request.getContextPath();
		int port = request.getServerPort();
		String basePath = "";
		if(port==80){
			basePath = request.getScheme()+"://"+request.getServerName()+path+"/";
		}else{
			basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
		}
		return basePath;
	}
}
