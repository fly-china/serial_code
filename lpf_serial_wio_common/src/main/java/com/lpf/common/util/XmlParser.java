package com.lpf.common.util;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Iterator;

/**
 * 
 * <p>Title: XmlParser</p>
 * <p>Description: 报文xml解析器</p>
 * <p>Company: ChinaBank</p>
 * @ClassName: XmlParser 
 * @author JD
 * @date 2013-6-7 上午11:35:21 
 * @version 1.0.0
 *
 */
public class XmlParser {

	private final static String PATHDELIMITER = "/";
	private static final Logger logger = LoggerFactory.getLogger(XmlParser.class);
	
	/**
	 * 
	 *<p>Description: 按路径取元素的值，路径形式为：ap/Corp/DbAccName</p>
	 * @Title: getElementValueByPath 
	 * @param xmlStr
	 * @param path
	 * @return
	 * @author JD
	 */
	public static String getElementValueByPath(String xmlStr, String path){
		try {
			if(StringUtils.isBlank(path)) return null;
			
			String[] directories = path.split(PATHDELIMITER);
			
			Document document = DocumentHelper.parseText(xmlStr);
			Element rootElement = document.getRootElement();
			
			return getElementValueByPath(rootElement, directories);			
			
		} catch (DocumentException e) {
			// ignore
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private static String getElementValueByPath(Element rootElement, String[] directories){
		if(directories.length <= 1) return null;
		
		String[] subDirectory = Arrays.copyOfRange(directories, 1, directories.length);
		String elementName = subDirectory[0];
		Iterator<Element> elementIterator = rootElement.elementIterator(elementName);
		if(subDirectory.length == 1){
			while(elementIterator.hasNext()){
				Element element = elementIterator.next();
				String textValue = element.getText();
				if(StringUtils.isNotBlank(textValue))
					return textValue;
			}
		}else{
			while(elementIterator.hasNext()){
				Element element = elementIterator.next();
				String lastResult = getElementValueByPath(element, subDirectory);
				if(StringUtils.isNotBlank(lastResult))
					return lastResult;
			}
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T parseToBean(String xmlStr,Class<T> c){
		try{
			Object bean = c.newInstance();
			Document document = DocumentHelper.parseText(xmlStr);
			Element rootElement = document.getRootElement();
			
			//填充bean的值
			setValuesForBean(rootElement, bean);
			
			return (T) bean;
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
	
	@SuppressWarnings("unchecked")
	private static Object setValuesForBean(Element root, Object bean){
		Iterator<Element> elementIterator = root.elementIterator();
		if(!elementIterator.hasNext()){
			try {
				String elementName = root.getName();
				String fieldName = elementName.substring(0, 1).toLowerCase()+elementName.substring(1);
				String elementValue = root.getText();
								
				BeanUtilsWithCache.setFieldValue(bean, fieldName, elementValue, String.class);
			} catch (Exception e) {
				logger.error("属性设值异常");
			} 
			
			return bean;
		}
		
		while(elementIterator.hasNext()){
			Element child = elementIterator.next();
			setValuesForBean(child, bean);
		}
		
		return bean;
	}
}
