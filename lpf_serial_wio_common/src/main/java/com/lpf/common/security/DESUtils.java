package com.lpf.common.security;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import java.security.spec.KeySpec;

/**
 * 本程序主要是DES及3DES的加解密
 * UnionDesEncrypt及UnionDesDecrypt为DES加解密
 * Union3DesEncrypt及Union3DesDecrypt为3DES加解密
 * UnionEncryptData及UnionDecryptData为字符串方式,加密方式以密钥长度为准
 *
 * @since: 12-7-26 上午11:51
 * @version: 1.0.0
 */
public class DESUtils {

    /**
     * des加密
     *
     * @param key  密钥
     * @param data 明文数据 16进制且长度为16的整数倍不足时补0
     * @return 密文数据
     */
    public static byte[] UnionDesEncrypt(byte[] key, byte[] data) {
        try {
            KeySpec ks = new DESKeySpec(key);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
            SecretKey ky = kf.generateSecret(ks);
            Cipher c = Cipher.getInstance("DES/ECB/NoPadding");
            c.init(Cipher.ENCRYPT_MODE, ky);
            return c.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public static void main(String[] args) {
//        PKCS5Padding
//        byte[] str1 = "11111111111111111111111111111111".getBytes();
//        System.out.print(new String(UnionDesEncrypt(str1,str1)));
//    }
    /**
     * des解密
     *
     * @param key  密钥
     * @param data 密文数据 16进制且长度为16的整数倍
     * @return 明文数据
     */
    public static byte[] UnionDesDecrypt(byte[] key, byte[] data) {
        try {
            KeySpec ks = new DESKeySpec(key);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");
            SecretKey ky = kf.generateSecret(ks);
            Cipher c = Cipher.getInstance("DES/ECB/NoPadding");
            c.init(Cipher.DECRYPT_MODE, ky);
            return c.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 3des加密
     *
     * @param key  密钥
     * @param data 明文数据 16进制且长度为16的整数倍不足时补0
     * @return 密文数据
     */
    public static byte[] Union3DesEncrypt(byte[] key, byte[] data) {
        try {
            byte[] k = new byte[24];
            if (key.length == 16) {
                System.arraycopy(key, 0, k, 0, key.length);
                System.arraycopy(key, 0, k, 16, 8);
            } else {
                System.arraycopy(key, 0, k, 0, 24);
            }
            byte[] buff = EncodeUtils.complementZero(data);
            KeySpec ks = new DESedeKeySpec(k);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");
            SecretKey ky = kf.generateSecret(ks);
            Cipher c = Cipher.getInstance("DESede/ECB/NoPadding");
            c.init(Cipher.ENCRYPT_MODE, ky);
            return c.doFinal(buff);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 3des解密
     *
     * @param key  密钥
     * @param data 密文数据 16进制且长度为16的整数倍
     * @return 明文数据
     */
    public static byte[] Union3DesDecrypt(byte[] key, byte[] data) {
        try {
            byte[] k = new byte[24];
            if (key.length == 16) {
                System.arraycopy(key, 0, k, 0, key.length);
                System.arraycopy(key, 0, k, 16, 8);
            } else {
                System.arraycopy(key, 0, k, 0, 24);
            }
            KeySpec ks = new DESedeKeySpec(k);
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");
            SecretKey ky = kf.generateSecret(ks);
            Cipher c = Cipher.getInstance("DESede/ECB/NoPadding");
            c.init(Cipher.DECRYPT_MODE, ky);
            return c.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 加密数据
     *
     * @param key  密钥 16进制且长度为16的整数倍
     * @param data 明文数据 16进制且长度为16的整数倍不足时补0
     * @return 密文数据  16进制
     */
    public static String UnionEncryptData(String key, String data) {
        if ((key.length() != 16) && (key.length() != 32) && (key.length() != 48)) {
            return null;
        }
        int lenOfKey;
        lenOfKey = key.length();
        String strEncrypt = "";
        byte sourData[] = EncodeUtils.hexString2Byte(data);
        switch (lenOfKey) {
            case 16:
                byte desKey8[] = EncodeUtils.hexString2Byte(key);
                byte encrypt8[] = UnionDesEncrypt(desKey8, sourData);
                strEncrypt = EncodeUtils.byte2HexString(encrypt8);
                break;
            case 32:
                String newKey = key.substring(0, 16);
                newKey = key + newKey;
                byte keyByte16[] = EncodeUtils.hexString2Byte(newKey);
                byte encrypt16[] = Union3DesEncrypt(keyByte16, sourData);
                strEncrypt = EncodeUtils.byte2HexString(encrypt16);
                break;
            case 48:
                byte keyByte24[] = EncodeUtils.hexString2Byte(key);
                byte encrypt24[] = Union3DesEncrypt(keyByte24, sourData);
                strEncrypt = EncodeUtils.byte2HexString(encrypt24);
                break;
        }
        return strEncrypt;
    }

    /**
     * 数据解密
     *
     * @param key  密钥 支持单倍和多倍密钥
     * @param data 密文数据 16进制且长度为16的整数倍
     * @return 明文数据 16进制
     */
    public static String UnionDecryptData(String key, String data) {
        if ((key.length() != 16) && (key.length() != 32) && (key.length() != 48)) {
            return (null);
        }
        int lenOfKey;
        lenOfKey = key.length();
        String strEncrypt = "";
        byte sourData[] = EncodeUtils.hexString2Byte(data);
        switch (lenOfKey) {
            case 16:
                byte desKey8[] = EncodeUtils.hexString2Byte(key);
                byte encrypt8[] = UnionDesDecrypt(desKey8, sourData);
                strEncrypt = EncodeUtils.byte2HexString(encrypt8);
                break;
            case 32:
                String newKey = key.substring(0, 16);
                newKey = key + newKey;
                byte keyByte16[] = EncodeUtils.hexString2Byte(newKey);
                byte encrypt16[] = Union3DesDecrypt(keyByte16, sourData);
                strEncrypt = EncodeUtils.byte2HexString(encrypt16);
                break;
            case 48:
                byte keyByte24[] = EncodeUtils.hexString2Byte(key);
                byte encrypt24[] = Union3DesDecrypt(keyByte24, sourData);
                strEncrypt = EncodeUtils.byte2HexString(encrypt24);
                break;
        }
        return strEncrypt;
    }

}
