package com.nowpay.common.security;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * 3DES加密提供者
 * @author 韩彦伟
 * @since: 2012-09-14
 * @version: 1.0.0
 */
public class ThreeDESEncryptProvider {
	private static Logger logger = LoggerFactory.getLogger(ThreeDESEncryptProvider.class);
	
	public static String encrypt(String data,String key) {
		try {
			return EncodeUtils.byte2HexString(DESUtils.Union3DesEncrypt(key.getBytes("utf-8"), Base64.encodeBase64(data.getBytes("utf-8"))));
		} catch (UnsupportedEncodingException e) {
			logger.error("3DES加密错误");
            return null;
		}
	}

	public static String decrypt(String data,String key){
		try {
			return new String(Base64.decodeBase64(DESUtils.Union3DesDecrypt(key.getBytes("utf-8"), EncodeUtils.hexString2Byte(data))), "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("3DES解密错误");
            return null;
		}
	}

    private static String deciphering(String ciphertext, String mchNo) throws Exception {
        String PWD_SUFIX = "ZhiDaGeWuDi";
        String plainText = ThreeDESEncryptProvider.decrypt(ciphertext,
                MD5.md5(mchNo + PWD_SUFIX,"UTF-8")); byte[] decodeByte =
                BASE64.decode(plainText); String decodeText = new String(decodeByte);
        return decodeText;

    }

    private static String chipher(String mchNo, String plainText) throws Exception {
         String PWD_SUFIX = "ZhiDaGeWuDi";
        String ciphertext = BASE64.encode(plainText.getBytes());
        ciphertext = ThreeDESEncryptProvider.encrypt(ciphertext, MD5.md5(mchNo + PWD_SUFIX, "utf-8"));
        return ciphertext;
    }

    public static void main(String args[]){
        try {
            String chiphertext = chipher("1280615601","nnpygL2RyWjQVVN6unYpF9eLs3dhaYcP");
            System.out.println(chiphertext);
            System.out.println(deciphering(chiphertext,"1280615601"));
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
