package com.nowpay.common.util;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * xml 与 Bean 转换工具
 * @author 韩彦伟
 * @since 2013-09-04
 */
public class JaxbUtils {

	@SuppressWarnings("unchecked")
	public static <T> T xmlToBean(String xml,Class<T> c) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(c);
		
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		
		T bean = (T) unmarshaller.unmarshal(new StringReader(xml));
		
		return bean;		
	}
	
	public static String beanToXml(Object bean) throws JAXBException{
		JAXBContext jaxbContext = JAXBContext.newInstance(bean.getClass());
		Marshaller marshaller = jaxbContext.createMarshaller();
		
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		StringWriter xmlWriter = new StringWriter();
		
		marshaller.marshal(bean, xmlWriter);
		
		return xmlWriter.getBuffer().toString();
	}
	public static String beanToXml(Object bean,String encoding) throws JAXBException{
        JAXBContext jaxbContext = JAXBContext.newInstance(bean.getClass());
		Marshaller marshaller = jaxbContext.createMarshaller();

		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING,encoding);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

		StringWriter xmlWriter = new StringWriter();
        xmlWriter.append("<?xml version=\"1.0\" encoding=\""+encoding+"\"?>\n") ;
		marshaller.marshal(bean, xmlWriter);


		return xmlWriter.getBuffer().toString();
	}


    public static String beanToXml(Object bean,String encoding,String head) throws JAXBException{
        JAXBContext jaxbContext = JAXBContext.newInstance(bean.getClass());
        Marshaller marshaller = jaxbContext.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING,encoding);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);

        StringWriter xmlWriter = new StringWriter();
        marshaller.marshal(bean, xmlWriter);
        return xmlWriter.getBuffer().toString();
    }

}
