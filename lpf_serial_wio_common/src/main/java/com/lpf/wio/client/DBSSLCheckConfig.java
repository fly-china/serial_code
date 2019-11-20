package com.lpf.wio.client;

/**
 * Created with IntelliJ IDEA.
 * Date: 15-3-30
 * Time: 下午4:23
 * To change this template use File | Settings | File Templates.
 */
public class DBSSLCheckConfig {

    /**
     *  密钥库文件路径
     */
    private String sslKeyStorePath;
    /**
     *  密钥库密码
     */
    private String sslKeyStorePassword;
    /**
     *  密钥库类型
     */
    private String sslKeyStoreType;

    public String getSslKeyStorePath() {
        return sslKeyStorePath;
    }

    public void setSslKeyStorePath(String sslKeyStorePath) {
        this.sslKeyStorePath = sslKeyStorePath;
    }

    public String getSslKeyStorePassword() {
        return sslKeyStorePassword;
    }

    public void setSslKeyStorePassword(String sslKeyStorePassword) {
        this.sslKeyStorePassword = sslKeyStorePassword;
    }

    public String getSslKeyStoreType() {
        return sslKeyStoreType;
    }

    public void setSslKeyStoreType(String sslKeyStoreType) {
        this.sslKeyStoreType = sslKeyStoreType;
    }
}