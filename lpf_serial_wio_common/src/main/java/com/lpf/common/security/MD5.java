package com.lpf.common.security;

import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;

public class MD5 {
    /**
     * MD5方法
     * 
     * @param text 明文
     * @param charset 密钥
     * @return 密文
     * @throws Exception
     */
	public static String md5(String text, String charset) throws Exception {
        if(StringUtils.isBlank(charset))
            charset = "UTF-8";

		byte[] bytes = text.getBytes(charset);
		
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.update(bytes);
		bytes = messageDigest.digest();
		
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < bytes.length; i ++)
		{
			if((bytes[i] & 0xff) < 0x10)
			{
				sb.append("0");
			}

			sb.append(Long.toString(bytes[i] & 0xff, 16));
		}
		
		return sb.toString().toLowerCase();
	}
	
	/**
	 * MD5验证方法
	 * 
	 * @param text 明文
	 * @param charset 字符编码
	 * @param md5 密文
	 * @return true/false
	 * @throws Exception
	 */
	public static boolean verify(String text, String charset, String md5) throws Exception {
		String md5Text = md5(text, charset);
		if(md5Text.equalsIgnoreCase(md5))
		{
			return true;
		}

			return false;
	}

    public static String encrypt16MD5(String paramString){
        String str = encryptMD5(paramString);
        return str.substring(16);
    }

    public static String encryptMD5(String paramString){
        char[] arrayOfChar1 = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try{
            byte[] arrayOfByte = paramString.getBytes();
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(arrayOfByte);
            arrayOfByte = localMessageDigest.digest();
            int i = arrayOfByte.length;
            char[] arrayOfChar2 = new char[i * 2];
            int j = 0;
            for (int k = 0; k < arrayOfByte.length; ++k){
                int l = arrayOfByte[k];
                arrayOfChar2[(j++)] = arrayOfChar1[(l >>> 4 & 0xF)];
                arrayOfChar2[(j++)] = arrayOfChar1[(l & 0xF)];
            }
            return new String(arrayOfChar2);
        }
        catch (Exception localException){
        }
        return null;
    }


    public static void main(String[] args) {
        String ss = encryptMD5("asdasdas");
        System.out.println(ss);
    }
}