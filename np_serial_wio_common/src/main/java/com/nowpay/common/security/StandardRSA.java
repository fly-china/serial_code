package com.nowpay.common.security;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created with IntelliJ IDEA.
 * User: 韩彦伟
 * Date: 15-2-7
 * Time: 下午10:56
 * To change this template use File | Settings | File Templates.
 */
public class StandardRSA {
    public static class Keys{
        RSAPublicKey publicKey;
        RSAPrivateKey privateKey;
        public Keys(RSAPublicKey pub,RSAPrivateKey priv){
            this.publicKey=pub;
            this.privateKey=priv;
        }

        public RSAPublicKey getPublicKey(){
            return publicKey;
        }

        public RSAPrivateKey getPrivateKey(){
            return privateKey;
        }
    }
    /**
     * 生成公钥和私钥
     * @throws NoSuchAlgorithmException
     *
     */
    public static Keys getKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new Keys(publicKey,privateKey);
    }


    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data, RSAPublicKey publicKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        // 模长
        int key_len = publicKey.getModulus().bitLength() / 8;
        // 加密数据长度 <= 模长-11
        String[] datas = SeStringUtils.splitString(data, key_len - 11);
        String mi = "";
        //如果明文长度大于模长-11则要分组加密
        for (String s : datas) {
            mi += ASCIIUtils.bcd2Str(cipher.doFinal(s.getBytes()));
        }
        return mi;
    }

    public static String encrypt(String data,String publicKey) throws Exception{
        return encryptByPublicKey(data,getPublicKey(publicKey));
    }

    public static String decrypt(String data,String privateKey) throws Exception{
        return decryptByPrivateKey(data,getPrivateKey(privateKey));
    }

    /**
     * 私钥解密
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        //模长
        int key_len = privateKey.getModulus().bitLength() / 8;
        byte[] bytes = data.getBytes();
        byte[] bcd = ASCIIUtils.asciiToBcd(bytes, bytes.length);
        //System.err.println(bcd.length);
        //如果密文长度大于模长则要分组解密
        String ming = "";
        byte[][] arrays = SeArrayUtils.splitArray(bcd, key_len);
        for(byte[] arr : arrays){
            ming += new String(cipher.doFinal(arr));
        }
        return ming;
    }

    /**
     * RSA签名
     * @param content 待签名数据
     * @param privateKey 商户私钥
     * @return 签名值
     */
    public static String sign(String content, String privateKey)
    {
        try
        {
            PKCS8EncodedKeySpec priPKCS8 	= new PKCS8EncodedKeySpec( BASE64.decode(privateKey) );
            KeyFactory keyf 				= KeyFactory.getInstance("RSA");
            PrivateKey priKey 				= keyf.generatePrivate(priPKCS8);

            Signature signature = Signature
                    .getInstance("SHA1WithRSA");

            signature.initSign(priKey);
            signature.update( content.getBytes() );

            byte[] signed = signature.sign();

            return BASE64.encode(signed);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * RSA验签名检查
     * @param content 待签名数据
     * @param sign 签名值
     * @param publicKey 公钥
     * @return 布尔值
     */
    public static boolean verify(String content, String sign, String publicKey)
    {
        try
        {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = BASE64.decode(publicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));


            Signature signature = Signature
                    .getInstance("SHA1WithRSA");

            signature.initVerify(pubKey);
            signature.update( content.getBytes() );

            return signature.verify(BASE64.decode(sign) );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public static String getKeyString(Key key){
        byte[] keyBytes = key.getEncoded();
        return BASE64.encode(keyBytes);
    }

    public static RSAPublicKey getPublicKey(String pubKeyString) throws Exception{
        byte[] keyBytes = BASE64.decode(pubKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    public static RSAPrivateKey getPrivateKey(String privateKeyString) throws Exception{
        byte[] keyBytes = BASE64.decode(privateKeyString);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    public static void main(String[] args) throws Exception{
        Keys keys = getKeys();
//        String pubKey = getKeyString(keys.getPublicKey()).replace("\n","");
//        System.out.println(pubKey);
//        String privateKey = getKeyString(keys.getPrivateKey()).replace("\n","");
//        System.out.println(privateKey);
//        String data = "大好人好烟味";

        String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDq3z1jSNsAv9wvyO+92IBIaWfE8tfHxEtkUQlvxdxDeN8PhgtvMYS6kzNr30FzaiiS+4YZ77KfW/SN5D7RAurJjudz/NaZRSS3LVixlNVK4LZUz3YeG+qpVD9pT52Nnmw8eI7eHmJeE/gFn4rvdMlF/fOrb+dwZN9OQaQ5jYA6/QIDAQAB";
        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOrfPWNI2wC/3C/I773YgEhpZ8Ty18fES2RRCW/F3EN43w+GC28xhLqTM2vfQXNqKJL7hhnvsp9b9I3kPtEC6smO53P81plFJLctWLGU1UrgtlTPdh4b6qlUP2lPnY2ebDx4jt4eYl4T+AWfiu90yUX986tv53Bk305BpDmNgDr9AgMBAAECgYEAmMcZ4WlUu8oRaivkdrNLzs5xp5Tnc5RmDh0AbGph+qm3PxvMeEnXsp1px3Nz2aVmOyXNdzWKFUr1aw7bJY4mNjLMET6HoQb7190AuRL/790xHdLe9ze2j/NLmODN19GK+H5nenEEgH8cGYJtcri2VBsX6LFrzxTNWcZX+zRUtYECQQD83zAXatuvhSI+2KbAQkoir5SsD5r9z3iA68tP5DYG7HhAMNA94fQS5XCRUga7PxdafvOkAoRqZ/fsR/SeURMxAkEA7ccMhmbnLz3+KTcRL3x9iOYZswbyDYiFz7bYbT3ldh4fUn1xsRdZx9PrseGWTxkWjJy5whc99QjvVbK7+o75jQJAQtkJkJc6HitWw0BozJh5mrPJ3LZ/ZatsEsDCzYrp0Wi/3VBKLKHH8RrgaDpD90oVdWHsLKZRtQNpIfhfrV6HcQJAISLFFxHOE+JSwhGffnudzu8qE63bG5Gz/B9iB89BY/WMreU3a9NyOovI49ApLzqyzsOki2zLwJMPL2UqFx/HnQJAeqHLO/Hx5mGWM4F6B5ktjQ3XAlmcsMWjpJAVuiPOy9ei/qMgxHmI34xgoQaGe/HwxA7rO/dcVWe6CQAme6u0Jg==";
        String data = "好人哈";
        String encodeString = encryptByPublicKey(data,getPublicKey(pubKey));
        String decodeString = decryptByPrivateKey(encodeString,getPrivateKey(privateKey));
        System.out.println(decodeString);

        String signData = sign(data,privateKey);
        System.out.println(verify(data,signData,pubKey));
    }
}
