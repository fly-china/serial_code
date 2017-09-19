package com.nowpay.common.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;

/**
 * ftp实现文件的上传下载
 */
public class FtpUtils {


    /**
     * Description: 向FTP服务器上传文件
     * @param host FTP服务器host
     * @param port FTP服务器端口
     * @param username FTP登录账号
     * @param password FTP登录密码
     * @param ftpPath FTP服务器保存目录
     * @param filename 上传到FTP服务器上的文件名
     */
    public static boolean upload(String host,int port,String username, String password, String ftpPath, String filename) {

        FTPClient ftpClient = new FTPClient();
        InputStream fis = null;
        boolean flag = false;
        try {
            ftpClient.connect(host,port);
            ftpClient.login(username,password);

            ftpClient.enterLocalPassiveMode();

            File srcFile = new File(filename);

            fis = new FileInputStream(srcFile);

            //设置文件类型（二进制）
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

            //String name = srcFile.getName();
            flag = ftpClient.storeFile(ftpPath, fis);
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
            throw new RuntimeException("FTP客户端出错！", e);
        } finally {
            IOUtils.closeQuietly(fis);
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("关闭FTP连接发生异常！", e);
            }
        }
        return flag;
    }

    /**
     * Description: 从FTP服务器下载文件
     * @param host FTP服务器host
     * @param port FTP服务器端口
     * @param username FTP登录账号
     * @param password FTP登录密码
     * @param ftpFile FTP服务器上的要下载的文件名（全路径包括文件名）
     * @param localPath 下载后保存到本地的路径(全路径包括文件名)
     */
    public static boolean download(String host, int port,String username, String password, String ftpFile,String localPath) {
        FTPClient ftpClient = new FTPClient();
        FileOutputStream fos = null;
        boolean flag = false;
        try {
            ftpClient.connect(host,port);
            ftpClient.login(username,password);
            ftpClient.enterLocalPassiveMode();

            fos = new FileOutputStream(localPath);

            //设置文件类型（二进制）
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.retrieveFile(ftpFile, fos);
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
            throw new RuntimeException("FTP客户端出错！", e);
        } finally {
            IOUtils.closeQuietly(fos);
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("关闭FTP连接发生异常！", e);
            }
        }
        return flag;
    }
    /**
     * Description: 从FTP服务器下载文件
     * @param host FTP服务器hostname
     * @param port FTP服务器端口
     * @param username FTP登录账号
     * @param password FTP登录密码
     * @param remotePath FTP服务器上的相对路径
     * @param fileName 要下载的文件名
     * @param localPath 下载后保存到本地的路径
     * @return
     */
    public static boolean downFile(String host, int port,String username, String password, String remotePath,String fileName,String localPath) {
        boolean success = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(host, port);
            //如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
            ftp.login(username, password);//登录
            reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return success;
            }
            ftp.changeWorkingDirectory(remotePath);//转移到FTP服务器目录
            FTPFile[] fs = ftp.listFiles();
            for(FTPFile ff:fs){
                if(ff.getName().equals(fileName)){
                    File localFile = new File(localPath+"/"+ff.getName());

                    OutputStream is = new FileOutputStream(localFile);
                    ftp.retrieveFile(ff.getName(), is);
                    is.close();
                }
            }

            ftp.logout();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return success;
    }

    public static void main(String[] args) {
        download("file.ipaynow.cn",2211,"username","password","/daycount/2016-03-29.csv","D:/h0329.csv");
    }
}