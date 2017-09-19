package com.nowpay.wio.server.protocol.https;

/**
 * com.wangyin.front.core.Constant
 * User: wymouzhongyuan
 * Date: 13-12-12
 * Time: 下午3:20
 * comment: Constant
 */
public class Constant {

    public static final String PROTOCOL_CODEC_KEY = "protocol.codec";

    public static final String PROTOCOL_CONTYPE_KEY = "protocol.keepAlive";

    public static final String PROTOCOL_HEART_KEY = "protocol.heart";

    public static final String PROTOCOL_HEARTBEAT_KEY = "protocol.heartbeat";

    public static final String PROTOCOL_PORT_KEY = "protocol.port";

    public static final String TRANSFORM_GLOBAL_KEY = "(TRANSFORM_GLOBAL_KEY)";

    public static final String RESPONSE_TARGET_KEY = "(RESPONSE_TARGET_KEY)";


    public static final String TARGET_DEFAULT_METHOD = "call";
    /**
     * 短连接
     */
    public static final String PROTOCOL_CONTYPE_NOKEEP = "false";
    /**
     * 长连接
     */
    public static final String PROTOCOL_CONTYPE_KEEP = "true";

    public static final String PROTOCOL_CONTYPE_DEFAULT = PROTOCOL_CONTYPE_NOKEEP;

    /**
     * 登陆状态Key
     */
    public static final String LOGIN_STATE_KEY = "LOGIN_STATE_KEY";
    /**
     * 默认连接close时 发送登出消息的超时
     */
    public static final int DEFAULT_LOGOUT_TIMEOUT = 1 * 1000;

    public static final String FRONT_EX_KEY = "isFrontEx";

    public static final String SSL_CONF_PATH = "front.properties";

    public static final String SERVER_SSL_PATH = "server.keystore.path";
    public static final String DEF_SERVER_SSL_PATH = "server.jks";

    public static final String SERVER_SSL_PASS = "server.keystore.pass";

    public static final String CLIENT_SSL_PATH = "client.keystore.path";
    public static final String DEF_CLIENT_SSL_PATH = "client.jks";

    public static final String CLIENT_SSL_PASS = "client.keystore.pass";

    public static final String SSL_KEY = "ssl" ;

    public static final String CHECK_SERVER_CER = "checkServerCer" ;

    public static final String ASYN_RESPONSE_KEY = "asyn_response" ;

    /**
     * 短连接，消费端线程池配置
     */
    public static final String DEF_THREAD_POOL = "fixed" ;

    public static final String DEF_THREADS = "20" ;

    public static final String DEF_QUEUES = "5000" ;

    public static final String DEF_APP_NAME = "Front-provider" ;

    public static final String CALL_BACK_SIGN = "call_back_service" ;

    /******************************************报头域字段****************************************************************/
    public static final String HEADER_FIELD_ACCEPT = "Accept";
    public static final String HEADER_FIELD_UserAgent = "User-Agent";

    public static final String HEADER_FIELD_RemoteIp = "RemoteIp";
    public static final String HEADER_FIELD_RemoteDomainName = "RemoteDomainName";

}
