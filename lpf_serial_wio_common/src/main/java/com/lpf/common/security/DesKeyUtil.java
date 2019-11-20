package com.lpf.common.security;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: 树强
 * Date: 15-11-12
 * Time: 下午2:25
 * To change this template use File | Settings | File Templates.
 */
public class DesKeyUtil {

    private static Logger logger = LoggerFactory.getLogger(DesKeyUtil.class);

    private static DesKeyUtil instance = null;

    private String desKey;

    private static Integer desKeyLength = 8;

    /**
     * 根据3个密钥文件生成顶级密钥
     * @param fileOne 密钥文件1
     * @param fileTwo 密钥文件2
     * @param fileThree 密钥文件3
     * @param charset 编码字符集
     */
    private DesKeyUtil(String fileOne, String fileTwo, String fileThree, String charset){
        if (StringUtils.isBlank(desKey)) {
            StringBuilder desStr = new StringBuilder();
            String keyOne = getDesKeyFile(fileOne, charset);
            String keyTwo = getDesKeyFile(fileTwo, charset);
            String keyThree = getDesKeyFile(fileThree, charset);

            for(int i=0; i<desKeyLength; i++ ){
                desStr.append(keyOne.charAt(i)).append(keyThree.charAt(i));
            }
            desKey = desStr.append(keyTwo).toString();
        }
    }

    /**
     * 根据3个密钥文件生成顶级密钥
     * @param fileOne 密钥文件1
     * @param fileTwo 密钥文件2
     * @param fileThree 密钥文件3
     * @param charset 编码字符集
     * @return
     */
    public static DesKeyUtil getInstance(String fileOne, String fileTwo, String fileThree, String charset) {
        if (instance == null) {
            instance = new DesKeyUtil(fileOne, fileTwo, fileThree, charset);
        }
        return instance;
    }

    public String getDesKey(){
        return desKey;
    }

    /**
     * 将密钥放入三个文件
     * @param fileOne 密钥文件1
     * @param fileTwo 密钥文件2
     * @param fileThree 密钥文件3
     * @param charset 编码字符集
     * @return
     */
    private static void setDesKey(String fileOne, String fileTwo, String fileThree, String desKey, String charset) {
        if(StringUtils.isBlank(desKey) || desKey.length()!=24 ){
            return;
        }
        StringBuilder keyOne = new StringBuilder();
        StringBuilder keyTwo = new StringBuilder();
        StringBuilder keyThree = new StringBuilder();
        try {
            int length = desKeyLength * 2;
            keyTwo.append(desKey.substring(length));
            for(int i=0; i < length; i=i+2 ){
                keyOne.append(desKey.charAt(i));
                keyThree.append(desKey.charAt(i+1));
            }
            logger.error("将密钥放入三个文件");
            createFile(fileOne, keyOne.toString(), charset);
            createFile(fileTwo, keyTwo.toString(),charset);
            createFile(fileThree, keyThree.toString(), charset);
        }catch (Exception e){
            logger.error("将密钥放入三个文件出错");
        }
    }

    /**
     * 生成密钥文件
     * @param filePath 文件路径
     * @param charset 编码字符集
     * @return
     */
    private static void createFile(String filePath, String desKey, String charset) throws Exception{
        File file = new File(filePath);
        try{
            if(!file.exists()){
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, charset);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            try {
                bufferedWriter.write(desKey);
            } catch (IOException e) {
                logger.error("将密钥放入三个文件出错", e);
            }finally {
                bufferedWriter.close();
                outputStreamWriter.close();
                fileOutputStream.close();
            }
        }catch (Exception e){

        }
    }

    /**
     * 读取密钥文件
     * @param filePath 文件路径
     * @param charset 编码字符集
     * @return
     */
    private String getDesKeyFile(String filePath, String charset){
        String key = "";
        File file = new File(filePath);
        if(!file.exists()){
            return key;
        }
        try {
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), charset);//考虑到编码格式
            try {
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt;
                while((lineTxt = bufferedReader.readLine()) != null){
                    key += lineTxt;
                }
            } catch (Exception e) {
                logger.error("读取密钥文件“" + filePath + "”内容出错", e);
            }finally {
                if(read != null){
                    read.close();
                }
            }
        }catch (Exception e){

        }
        return key;
    }

}