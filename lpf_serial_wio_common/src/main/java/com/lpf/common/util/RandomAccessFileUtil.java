package com.lpf.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;

public class RandomAccessFileUtil {
	
	private static Logger LOGGER = LoggerFactory.getLogger(RandomAccessFileUtil.class);

	private static String filePath ="/export/data/fund_operation"; //System.getProperty("java.io.tmpdir");
	
		
	public static synchronized void writeLog(String content) throws IOException{
		
	
		Date today = new Date();
		String lineS =System.getProperty("line.separator");
		String fileName = filePath+File.separator+DateUtil.getStringFromDate(today, "yyyyMMdd")+"_repairedPayment.txt";
		LOGGER.info(fileName);
		File file = new File(fileName);
		if(!file.exists()){
		    file.createNewFile();	
		}
			// 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile;
		    randomFile = new RandomAccessFile(file, "rw");
		    // 文件长度，字节数
	        long fileLength = randomFile.length();
	        //将写文件指针移到文件尾。
	        randomFile.seek(fileLength);
	        //for(int i=0;i<30;i++){
	        	randomFile.write(content.getBytes("utf-8"));
	        //}
	        
	        randomFile.writeBytes(lineS);
	        randomFile.close();
	}


	public static  void writeLog(String fileName,String content) throws IOException{
		
		String tempfileName = filePath+File.separator+fileName;
		LOGGER.info(fileName);
		File file = new File(tempfileName);
		if(!file.exists()){
		    file.createNewFile();	
		}
				// 打开一个随机访问文件流，按读写方式
	        RandomAccessFile randomFile;
		    randomFile = new RandomAccessFile(file, "rw");
		    // 文件长度，字节数
	        long fileLength = randomFile.length();
	        //将写文件指针移到文件尾。
	        randomFile.seek(fileLength);
	        randomFile.write(content.getBytes("utf-8"));
	        randomFile.close();
	}
}
