package com.lpf.serial.controller;

import com.lpf.serial.pub.itf.IGetSerialCodeService;
import com.lpf.serial.utils.ComponentLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * B001- 订单验证接口业务处理器
 * Date: 14-8-7
 * Time: 上午10:23
 * To change this template use File | Settings | File Templates.
 */
public class GetSerialCodeTackler implements IBizTackler {


    private static final Logger logger = LoggerFactory.getLogger(GetSerialCodeTackler.class);

    private IGetSerialCodeService getSerialCodeService = ComponentLocator.getBean(IGetSerialCodeService.class);
    @Override
    public String getFuncode() {
        return "GetSerialCode";
    }

    @Override
    public String tackle(Map<String, String> dataMap,String charset,Map<String,String> headerMap) {
        String repString = "";
        try {
            String sysNo = dataMap.get("sysNo");
            String bizNo = dataMap.get("bizNo");
            logger.info("sysNo="+sysNo+",bizNo="+bizNo );
            repString = getSerialCodeService.getSerialCode(sysNo, bizNo);
        } catch (Exception e) {
            repString = "ERROR";
            logger.error("AgentPayController.agentPay error : ", e);
        }
        logger.info("****************repString=" + repString);
        return repString;
    }
}