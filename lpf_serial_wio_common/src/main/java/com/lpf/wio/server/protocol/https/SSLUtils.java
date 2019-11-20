package com.lpf.wio.server.protocol.https;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SSLUtils
 * Date: 14-1-20
 * Time: 上午10:38
 * comment:
 */
public class SSLUtils {

    private static final Logger log = LoggerFactory.getLogger(SSLUtils.class);

    private static final KeyManagerFactory KEY_MANAGER_FACTORY = createKeyManagerFactory();

    private static final TrustManagerFactory TRUST_MANAGER_FACTORY = createTrustManagerFactory() ;

    private static final X509Certificate[] EMPTY_X509CERTIFICATE_ARRAY = new X509Certificate[]{};

    private static final X509TrustManager ACCEPT_ALL=new TrustManager(false);

    private static final Map<String,SSLContext> SSLCONTEXT_CACHE = new ConcurrentHashMap<String,SSLContext>() ;

    private static final ClassLoader cl = SSLUtils.class.getClassLoader() ;


    /**
     * 创建服务端SSLEngine
     *
     * @param ksPath
     * @param ksPass
     * @param twoWayAuth
     * @return
     * @throws Exception
     */
    public static SSLEngine createServerSSLEngine(String ksPath, String ksPass, boolean twoWayAuth) throws Exception {
        return createSSLEngine(createServerSSLContext(ksPath, ksPass, twoWayAuth),twoWayAuth, false);
    }

    /**
     * 创建客户端SSLEngine
     *
     * @param tsPath
     * @param tsPass
     * @param twoWayAuth
     * @return
     * @throws Exception
     */
    public static SSLEngine createClientSSLEngine(String tsPath, String tsPass, boolean twoWayAuth,boolean checkServerCer) throws Exception {
        return createSSLEngine(createClientSSLContext(tsPath, tsPass, twoWayAuth,checkServerCer),twoWayAuth, true);
    }


    public static SSLEngine createSSLEngine(SSLContext context, boolean twoWayAuth,boolean isClient) {
        SSLEngine sslEngine = context.createSSLEngine();
        sslEngine.setUseClientMode(isClient);
        if(!isClient && twoWayAuth)
            sslEngine.setNeedClientAuth(true);//need client authentication
        sslEngine.setEnabledCipherSuites(sslEngine.getSupportedCipherSuites());
        return sslEngine;
    }

    public static SSLContext createServerSSLContext(String ksPath, String ksPass, boolean twoWayAuth) throws Exception {
        return createSSLContext(twoWayAuth, ksPath, null, ksPass, null, false,true);
    }

    public static SSLContext createClientSSLContext(String tsPath, String tsPass, boolean twoWayAuth,boolean checkServerCer) throws Exception {
        return createSSLContext(twoWayAuth, null, tsPath, null, tsPass, true,checkServerCer);
    }

    /**
     * 创建SSLContext
     *
     * @param twoWayAuth 是否双向认证
     * @param ksPath     密钥管理路径
     * @param tsPath     信任管理路径
     * @param ksPass     密钥管理密码
     * @param tsPass     信任管理密码
     * @param isClient   是否是客户端
     * @return
     * @throws Exception
     */
    public static SSLContext createSSLContext(boolean twoWayAuth, String ksPath, String tsPath, String ksPass, String tsPass, boolean isClient,boolean checkServerCer) throws Exception {
        String cacheKey = twoWayAuth + ksPath + tsPath + isClient + checkServerCer ;
        SSLContext sslCtx = SSLCONTEXT_CACHE.get(cacheKey);
        if(sslCtx == null){
            sslCtx = SSLContext.getInstance("SSL");
            KeyStore ks = null, ts = null;
            KeyManagerFactory kmf = null;
            TrustManagerFactory tmf = null;
            if (isClient) {
                if (tsPath != null) {
                    ts = KeyStore.getInstance("JKS");
                    ts.load(cl.getResourceAsStream(tsPath), tsPass.toCharArray());
                    tmf = TRUST_MANAGER_FACTORY;
                    tmf.init(ts);
                }
                if (twoWayAuth) {
                    ks = KeyStore.getInstance("JKS");
                    ks.load(cl.getResourceAsStream(ksPath), ksPass.toCharArray());
                    kmf = KEY_MANAGER_FACTORY;
                    kmf.init(ks, ksPass.toCharArray());
                }
            } else {
                if (ksPath != null) {
                    ks = KeyStore.getInstance("JKS");
                    ks.load(cl.getResourceAsStream(ksPath), ksPass.toCharArray());
                    kmf = KEY_MANAGER_FACTORY;
                    kmf.init(ks, ksPass.toCharArray());
                }
                if (twoWayAuth) {
                    ts = KeyStore.getInstance("JKS");
                    ts.load(cl.getResourceAsStream(tsPath), tsPass.toCharArray());
                    tmf = TRUST_MANAGER_FACTORY;
                    tmf.init(ts);
                }

            }
            sslCtx.init(kmf != null ? kmf.getKeyManagers() : null, checkServerCer?(tmf != null ? tmf.getTrustManagers(): null):new X509TrustManager[]{ACCEPT_ALL}, null);
        }
        return sslCtx;
    }


    /**
     * 创建默认的信任管理工厂
     *
     * @return
     * @throws java.security.NoSuchAlgorithmException
     */
    public static TrustManagerFactory createTrustManagerFactory()  {
        final String algo = TrustManagerFactory.getDefaultAlgorithm();
        try {
            return TrustManagerFactory.getInstance(algo);
        } catch (final NoSuchAlgorithmException ex) {
            try {
                return TrustManagerFactory.getInstance("SunX509");
            } catch (NoSuchAlgorithmException e) {
                log.error("NoSuchAlgorithmException", e);
                return null ;
            }
        }
    }

    /**
     * 创建默认的密钥管理工厂
     *
     * @return
     * @throws java.security.NoSuchAlgorithmException
     */
    public static KeyManagerFactory createKeyManagerFactory()  {
        final String algo = KeyManagerFactory.getDefaultAlgorithm();
        try {
            return KeyManagerFactory.getInstance(algo);
        } catch (final NoSuchAlgorithmException ex) {
            try {
                return KeyManagerFactory.getInstance("SunX509");
            } catch (NoSuchAlgorithmException e) {
                log.error("NoSuchAlgorithmException",e);
                return null ;
            }
        }
    }

    private static Properties sslConfig = new Properties();
    private static volatile boolean init = false ;

    public static Properties getSSLConfig() {
        if (init)
            return sslConfig;
        try {
            sslConfig.load(SSLUtils.class.getResourceAsStream(Constant.SSL_CONF_PATH));
            init = true ;
        } catch (Exception e) {
            //default
            initConfigByDef(sslConfig);
        }
        return sslConfig;
    }

    private static class TrustManager implements X509TrustManager {

        private final boolean checkServerValidity;

        TrustManager(boolean checkServerValidity) {
            this.checkServerValidity = checkServerValidity;
        }

        /**
         * Never generates a CertificateException.
         */
//        @Override
        public void checkClientTrusted(X509Certificate[] certificates, String authType)
        {
            return;
        }

        //        @Override
        public void checkServerTrusted(X509Certificate[] certificates, String authType)
                throws CertificateException
        {
            if (checkServerValidity) {
                for (X509Certificate certificate : certificates)
                {
                    certificate.checkValidity();
                }
            }
        }

        /**
         * @return an empty array of certificates
         */
//        @Override
        public X509Certificate[] getAcceptedIssuers()
        {
            return EMPTY_X509CERTIFICATE_ARRAY;
        }
    }

    private static void initConfigByDef(Properties config) {
        config.setProperty(Constant.SERVER_SSL_PATH, Constant.DEF_SERVER_SSL_PATH);
        config.setProperty(Constant.SERVER_SSL_PASS, "123456");
        config.setProperty(Constant.CLIENT_SSL_PATH, Constant.DEF_CLIENT_SSL_PATH);
        config.setProperty(Constant.CLIENT_SSL_PASS, "123456");
    }

}
