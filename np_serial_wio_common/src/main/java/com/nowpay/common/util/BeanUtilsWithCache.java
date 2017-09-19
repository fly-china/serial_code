package com.nowpay.common.util;

import org.apache.commons.lang3.StringUtils;

import java.lang.ref.SoftReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 提供缓存Method的BeanUtils--一般的反射机制请走apache的BeanUtils
 * <p>1. 考虑了重复反射获取Method的性能降低问题，将Method缓存起来
 * <p>
 * @author 韩彦伟
 * @since 2013-10-14
 */
public class BeanUtilsWithCache {

	private static SoftReference<Map<String, Method>> key_Method_ref;
	
	@SuppressWarnings("rawtypes")
	public static void setFieldValue(Object bean, String fieldName,	Object elementValue, Class elementValueClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		if(bean == null || StringUtils.isBlank(fieldName) || elementValue == null) return;
		
		String className = bean.getClass().getName();
		String methodKey = "SET|"+className+"|"+fieldName;
		
		Method setMethod = getKey_method_map().get(methodKey);
		if(setMethod == null){
			String setMethodName = "set"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
			setMethod = bean.getClass().getMethod(setMethodName, elementValueClass);
			if(setMethod != null)
				getKey_method_map().put(methodKey, setMethod);
		}
		
		if(setMethod != null)
			setMethod.invoke(bean, elementValue);
	}	
	
	private static Map<String, Method> getKey_method_map(){
		if(key_Method_ref == null || key_Method_ref.get() == null){
			key_Method_ref = new SoftReference<Map<String,Method>>(new ConcurrentHashMap<String, Method>());
		}
		
		return key_Method_ref.get();		
	}
	
	static class Person{
		private String name;
		
		private int age;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}		
	}
	
	static class Chinese extends Person{
		private String province;
		private String district;
		public String getProvince() {
			return province;
		}
		public void setProvince(String province) {
			this.province = province;
		}
		public String getDistrict() {
			return district;
		}
		public void setDistrict(String district) {
			this.district = district;
		}		
	}
	
	public static void main(String[] args) throws Exception{
		Chinese newChinese = new Chinese();
		
		setFieldValue(newChinese, "name", "christ",String.class);
		
		setFieldValue(newChinese, "age",1000,int.class);
		
		setFieldValue(newChinese, "name", "hyw",String.class);
		
		setFieldValue(newChinese, "age",2000,int.class);
	}
}
