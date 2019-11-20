package com.lpf.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 对象深度克隆
 *
 */
public class CloneUtils {

    private static Logger logger = LoggerFactory.getLogger(CloneUtils.class);

	@SuppressWarnings("unchecked")
	public static <T> T deepClone(Class<T> c, Object obj){
		 try{
             logger.info("---------------------------------------对象深度克隆开始-------------------------------------------");
             logger.info("深度克隆对象类型："+c.getName());
             logger.info("深度克隆对象为："+obj.toString());
			 //save the object to a byte array
	          ByteArrayOutputStream bout = new ByteArrayOutputStream();
	          ObjectOutputStream out = new ObjectOutputStream(bout);
	          out.writeObject(obj);
	          out.close();
	          
	          //read a clone of the object from byte array
	          ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
	          ObjectInputStream in = new ObjectInputStream(bin);
	          Object ret = in.readObject();
	          in.close();

             logger.info("深度克隆新对象结果为："+ret.toString());
             logger.info("---------------------------------------对象深度克隆结束-------------------------------------------");
	          
	          return (T)ret;
	      }catch(Exception e){
             logger.error("深度克隆对象失败",e);
	          throw new RuntimeException("深度克隆对象失败",e);
	      }
	}
}
