package com.nowpay.common.util;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;


public class SocketPingUtil {

    public static boolean checkPort(String ip,int port) throws IOException {
        Socket socket = null;
        try {
            socket = new Socket();
            SocketAddress endpoint = new InetSocketAddress(ip, port);
            socket.connect(endpoint, 3000);
            return true;
        }catch (Exception ex){
            return false;
        } finally {
            if(socket != null)
                socket.close();
        }
    }
}
