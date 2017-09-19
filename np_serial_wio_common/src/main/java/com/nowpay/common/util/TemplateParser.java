package com.nowpay.common.util;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.StringWriter;
import java.util.Map;

public class TemplateParser {

	private static final Logger logger = LoggerFactory.getLogger(TemplateParser.class);
	private Configuration config = null;
	
	private TemplateParser(){
		config = new Configuration();
		config.setObjectWrapper(new DefaultObjectWrapper());
	}
	
	private static class SingletonHolder{
		private static TemplateParser signletion = new TemplateParser();
	}
	
	public static TemplateParser getInstance(){
		return SingletonHolder.signletion;
	}
	
	
	/**
	 * 根据模版名称，数据模型，组装模版
	 * @param root 数据模型
	 * @param templatePath 模板路径
	 * @param templateName 模版名称
	 * @return
	 */
	public String getStrByTemplate(Map<String,Object> root,String templatePath,String templateName){
		logger.info("模板文件:"+templatePath);
		String result = "";
		try {
			config.setDirectoryForTemplateLoading(new File(templatePath));
			Template template = config.getTemplate(templateName);
			StringWriter out = new StringWriter();
			template.process(root,out);
			out.flush();
			out.close();
			result = out.toString();
		} catch (Exception e) {
			logger.error("加载"+templatePath+templateName+"文件异常",e);
			e.printStackTrace();
		} 
		return result;
	}
	
}
