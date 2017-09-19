package com.nowpay.common.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.io.StringWriter;

/**
 * @freemark渲染模板内容
 */
public class FreeMarkerUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(FreeMarkerUtil.class);

    /**
     * 根据模版生成字符串内容
     * @param templeFileName
     * @param model
     * @return
     */
    public static String renderTemplate(FreeMarkerConfigurer freemarkerConfigurer,String templeFileName,Object model){
        StringWriter out = null;
        try {
            Configuration configuration = freemarkerConfigurer.getConfiguration();
            Template  template = configuration.getTemplate(templeFileName);
            out = new StringWriter();
            template.process(CommonBeanUtils.toMap(model),out);
            out.flush();
        }
        catch (IOException e)
        {
            LOGGER.error("根据模版生成字符串内容流关闭的流输出IO异常", e);
        }
        catch (TemplateException e)
        {
            LOGGER.error("根据模版生成报文字符串流关闭的模版异常", e);
        }
        finally
        {
            try{
                if(out != null){
                    out.close();
                }
            } catch (IOException e) {
                LOGGER.error("根据模版生成报文内容流关闭的IO异常", e);
            }
        }
        if(out != null){
            return out.toString();
        }else{
            return "";
        }
    }
}
