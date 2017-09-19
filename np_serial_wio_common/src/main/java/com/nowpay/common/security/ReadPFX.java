package com.nowpay.common.security;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Enumeration;

public class ReadPFX {

    private static Logger logger = LoggerFactory.getLogger(ReadPFX.class);

    public ReadPFX (){  
    }  
    //转换成十六进制字符串  
    public static String Byte2String(byte[] b) {  
        String hs="";  
        String stmp="";  
   
        for (int n=0;n<b.length;n++) {  
            stmp=(Integer.toHexString(b[n] & 0XFF));
            if (stmp.length()==1) hs=hs+"0"+stmp;  
            else hs=hs+stmp;  
            //if (n<b.length-1)  hs=hs+":";  
        }  
        return hs.toUpperCase();  
    }  
      
    public static byte[] StringToByte(int number) {   
        int temp = number;   
        byte[] b=new byte[4];   
        for (int i=b.length-1;i>-1;i--){   
            b[i] = new Integer(temp&0xff).byteValue();//将最高位保存在最低位   
            temp = temp >> 8; //向右移8位   
        }   
        return b;   
    }   
    public static PrivateKey GetPvkformPfx(String strPfx, String strPassword){
        try {  
            KeyStore ks = KeyStore.getInstance("PKCS12");
            FileInputStream fis = new FileInputStream(strPfx);
            // If the keystore password is empty(""), then we have to set  
            // to null, otherwise it won't work!!!  
            char[] nPassword = null;  
            if ((strPassword == null) || strPassword.trim().equals("")){  
                nPassword = null;  
            }  
            else  
            {  
                nPassword = strPassword.toCharArray();  
            }  
            ks.load(fis, nPassword);  
            fis.close();  
//            System.out.println("keystore type=" + ks.getType());
            // Now we loop all the aliases, we need the alias to get keys.  
            // It seems that this value is the "Friendly name" field in the  
            // detals tab <-- Certificate window <-- view <-- Certificate  
            // Button <-- Content tab <-- Internet Options <-- Tools menu   
            // In MS IE 6.  
            Enumeration enumas = ks.aliases();
            String keyAlias = null;  
            if (enumas.hasMoreElements())// we are readin just one certificate.  
            {  
                keyAlias = (String)enumas.nextElement();   
                System.out.println("alias=[" + keyAlias + "]");  
            }  
            // Now once we know the alias, we could get the keys.  
//            System.out.println("is key entry=" + ks.isKeyEntry(keyAlias));
            PrivateKey prikey = (PrivateKey) ks.getKey(keyAlias, nPassword);
            return prikey;
        }  
        catch (Exception e)  
        {  
            logger.error("获取私钥时发生错误",e);
        }  
        return null;  
    }

    public static PublicKey GetPukformPfx(String strPfx, String strPassword){
        try {
            KeyStore ks = KeyStore.getInstance("PKCS12");
            FileInputStream fis = new FileInputStream(strPfx);
            // If the keystore password is empty(""), then we have to set
            // to null, otherwise it won't work!!!
            char[] nPassword = null;
            if ((strPassword == null) || strPassword.trim().equals("")){
                nPassword = null;
            }
            else
            {
                nPassword = strPassword.toCharArray();
            }
            ks.load(fis, nPassword);
            fis.close();
//            System.out.println("keystore type=" + ks.getType());
            // Now we loop all the aliases, we need the alias to get keys.
            // It seems that this value is the "Friendly name" field in the
            // detals tab <-- Certificate window <-- view <-- Certificate
            // Button <-- Content tab <-- Internet Options <-- Tools menu
            // In MS IE 6.
            Enumeration enumas = ks.aliases();
            String keyAlias = null;
            if (enumas.hasMoreElements())// we are readin just one certificate.
            {
                keyAlias = (String)enumas.nextElement();
                System.out.println("alias=[" + keyAlias + "]");
            }
            // Now once we know the alias, we could get the keys.
//            System.out.println("is key entry=" + ks.isKeyEntry(keyAlias));
            PrivateKey prikey = (PrivateKey) ks.getKey(keyAlias, nPassword);
            Certificate cert = ks.getCertificate(keyAlias);
            PublicKey pubkey = cert.getPublicKey();

            return pubkey;
        }
        catch (Exception e)
        {
            logger.error("获取公钥时发生错误",e);
        }
        return null;
    }

    public static String getPrivateKeyString(String pfxFile,String password){
        RSAPrivateKey privateKey = (RSAPrivateKey) GetPvkformPfx(pfxFile,"123456");

        byte[] keyBytes = privateKey.getEncoded();
        String privateKeyString = BASE64.encode(keyBytes);
        privateKeyString = privateKeyString.replaceAll("\r","");
        return privateKeyString.replaceAll("\n", "");
    }

    public static void main(String[] args) {
        String filePath = "F:/server.pfx";
        RSAPrivateKey privateKey = (RSAPrivateKey) GetPvkformPfx(filePath,"123456");

        byte[] keyBytes = privateKey.getEncoded();
        Base64 base64 = new Base64();
        String privateKeyString = base64.encodeAsString(keyBytes);
        System.out.println(privateKeyString);

        RSAPublicKey rsaPublicKey = (RSAPublicKey) GetPukformPfx(filePath,"123456");

        byte[] keyBytes1 = rsaPublicKey.getEncoded();
        String pubKeyString = base64.encodeAsString(keyBytes1);
        System.out.println(pubKeyString);
    }
}