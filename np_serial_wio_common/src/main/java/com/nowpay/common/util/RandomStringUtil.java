package com.nowpay.common.util;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Windows
 * Date: 15-4-18
 * Time: 下午2:43
 * To change this template use File | Settings | File Templates.
 */
public class RandomStringUtil {

    /**
     * 生成随即密码
     * @param pwd_len 生成的密码的总长度
     * @return  密码的字符串
     */
    public static String genRandomNum(int pwd_len){
        //45是因为数组是从0开始的，26个字母+10个数字+10个特殊字符
        final int  maxNum = 46;
        int i;  //生成的随机数
        int count = 0; //生成的密码的长度
        char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'

        };
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        while(count < pwd_len){
            //生成随机数，取绝对值，防止生成负数，
            i = Math.abs(r.nextInt(maxNum));  //生成的数最大为46-1
            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count ++;
            }
        }
        return pwd.toString();
    }

    public static String getNumberNum(int pwd_len){
        //45是因为数组是从0开始的，26个字母+10个数字+10个特殊字符
        final int  maxNum = 46;
        int i;  //生成的随机数
        int count = 0; //生成的密码的长度
        char[] str = {  '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        while(count < pwd_len){
            //生成随机数，取绝对值，防止生成负数，
            i = Math.abs(r.nextInt(maxNum));  //生成的数最大为46-1
            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count ++;
            }
        }
        return pwd.toString();
    }

    /**
     * 计算报文响应性能
     * @param n
     * @return
     */
    public static String grade(long n){
        String rs="毫秒";
        if(0<=n && n<3000){
            rs ="A---->" + n + rs;
        }else if(3000<=n && n<5000){
            rs = "B---->" + n + rs;
        }else if(5000<=n && n<7000){
            rs = "C---->" + n + rs;
        }else if(7000<=n && n<9000){
            rs = "D---->" + n + rs;
        }else{
            rs = "E---->" + n + rs;
        }
        return rs;
    }


    public static void main(String [] args){
        String a = getNumberNum(6);
        System.out.print(a);
    }
}
