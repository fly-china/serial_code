package com.nowpay.common.security;

public class SeStringUtils {
	public static String stringBlank(String value) {
		value = stringNull(value);
		if (value == null){
			value = "";
		}
		return value;
	}

	/**
	 * 
	 * null "" 格式化为null
	 * 
	 * 
	 * 
	 * @param value
	 * 
	 * @return null
	 */

	public static String stringNull(String value) {
		if (value != null){
			value = value.trim();
			if (value.equals("")){
				value = null;
			}
		}
		return value;
	}
	
    /** 
     * 拆分字符串 
     */  
    public static String[] splitString(String string, int len) {  
        int x = string.length() / len;  
        int y = string.length() % len;  
        int z = 0;  
        if (y != 0) {  
            z = 1;  
        }  
        String[] strings = new String[x + z];  
        String str = "";  
        for (int i=0; i<x+z; i++) {  
            if (i==x+z-1 && y!=0) {  
                str = string.substring(i*len, i*len+y);  
            }else{  
                str = string.substring(i*len, i*len+len);  
            }  
            strings[i] = str;  
        }  
        return strings;  
    } 

}
