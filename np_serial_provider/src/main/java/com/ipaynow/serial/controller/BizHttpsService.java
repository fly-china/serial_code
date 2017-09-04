package com.ipaynow.serial.controller;


import com.nowpay.devplat.common.util.RandomStringUtil;
import com.nowpay.devplat.common.util.URLUtils;
import com.nowpay.wio.server.protocol.IWIOServiceProvider;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 交易的WIOService
 * User: 韩彦伟
 * Date: 14-8-7
 * Time: 上午9:30
 * To change this template use File | Settings | File Templates.
 */
public class BizHttpsService implements IWIOServiceProvider {

    private Logger logger = LoggerFactory.getLogger(BizHttpsService.class);

    @Override
    public String service(String requestUri, String requestReport, String charset, Map<String, String> headerMap) {
        try {
            //先适配字符集
            charset = adapterCharset(requestReport, charset);
            logger.info("中小开发者HTTPS服务请求报文："+URLUtils.urldecode(requestReport,charset));

            Map<String,String> key_value_map = ReportParser.parseFormDataPatternReport(requestReport, charset);
            String funcode = key_value_map.get(IWIOServerConstants.FIELD_FUNDCODE);
            long startTime=System.currentTimeMillis();   //获取开始时间
            IBizTackler bizTackler = BizTacklerMapping.getTackler(requestUri,funcode);
            String bizRespResult = bizTackler.tackle(key_value_map,charset,headerMap);
            long endTime=System.currentTimeMillis(); //获取结束时间
            logger.info("funcode:" + funcode +","+RandomStringUtil.grade(endTime-startTime) + ",响应报文："+ URLUtils.urldecode(bizRespResult, DevPlatTradeConstants.DEFAULT_CHARSET));
            return bizRespResult;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("中小开发者HTTPS服务通讯失败",e);
            return "中小开发者HTTPS服务通讯失败";
        }
    }

    private String adapterCharset(String requestReport, String charset) {
        int mhCharsetIndex = requestReport.indexOf(IWIOServerConstants.FIELD_MHTCHARSET+IWIOServerConstants.DILIMITER_FORMDATA_KV);
        if(mhCharsetIndex >= 0){
            String subRequestContent = requestReport.substring(mhCharsetIndex+(IWIOServerConstants.FIELD_MHTCHARSET.length()+1));
            if(StringUtils.isNotBlank(subRequestContent)){
                int nextDomainDilimiterIndex = subRequestContent.indexOf(IWIOServerConstants.DILIMITER_FORMDATA_MD);
                if(nextDomainDilimiterIndex < 0){
                    if(IWIOServerConstants.CHARSET_UTF8.equalsIgnoreCase(subRequestContent)
                            || IWIOServerConstants.CHARSET_GBK.equalsIgnoreCase(subRequestContent))
                        charset = subRequestContent;
                }
                else{
                    String mhtCharsetValue = subRequestContent.substring(0,nextDomainDilimiterIndex);
                    if(StringUtils.isNotBlank(mhtCharsetValue)){
                        if(IWIOServerConstants.CHARSET_UTF8.equalsIgnoreCase(mhtCharsetValue)
                                || IWIOServerConstants.CHARSET_GBK.equalsIgnoreCase(mhtCharsetValue))
                            charset = mhtCharsetValue;
                    }
                }
            }
        }
        return charset;
    }
}