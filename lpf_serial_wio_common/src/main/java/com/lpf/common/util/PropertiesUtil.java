package com.lpf.common.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtil {
	private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
	private static Map<String,PropertiesUtil> map = new HashMap<String,PropertiesUtil>();
	private Properties pros ;
	private static String charset = "UTF-8";
	
	
	private PropertiesUtil(Properties pros){
		this.pros = pros;
	}
	
	/**
	* @Title: 根据指定编码加载属性文件
	* @Description: 适配属性文件定义为gbk,读取时指定的编码为utf-8
	* @param  
	* @return PropertiesUtil 返回类型
	*/
	public static PropertiesUtil load(String filePath,String charset){
		PropertiesUtil.charset= charset;
		return PropertiesUtil.load(filePath);
	}
	/**
	 *<p>Description:静态加载属性文件</p>
	 * @Title: load 
	 * @param filePath	文件路径
	 * @return	 config 返回Config 对象
	 * @author wangkang
	 */
	public static PropertiesUtil load(String filePath){
		if(map.containsKey(filePath)){
			logger.debug("配置文件:"+filePath+"已加载");
		}else{
			logger.debug("加载"+filePath+"配置文件");
			InputStream input = null;
			InputStreamReader reader = null;
			try {
//				String path = PropertiesUtil.class.getResource("/").getPath() + filePath;
//				System.out.println("配置文件path:"+path);
//				InputStream input =new FileInputStream(path);
				input = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
				reader = new InputStreamReader(input, charset);
				Properties _pros = new Properties();
				_pros.load(reader);
				//_pros.load(input);
				map.put(filePath, new PropertiesUtil(_pros));
			} catch (IOException e) {
				//为书写方便, 不抛出异常,结果返回null
				logger.error("加载"+filePath+"配置文件异常",e);
			}finally{
				try {
					if(input != null){
						input.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					if(reader != null){
						reader.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return map.get(filePath);
	}
	
	/**
	 *<p>Description:取得Config中的属性值</p>
	 * @Title: getProValue 
	 * @param key
	 * @return
	 * @author wangkang
	 */
	public String getProValue(String key){
		return pros.getProperty(key);
	}
	
	public String getProValueFormat(String key,Object... obj){
		String str = pros.getProperty(key);
		return MessageFormat.format(str, obj);
	}
	
	public static void main(String[] args) {
		
	}
}
