package com.ipaynow.serial.test;

/**
 * @author
 * @create 2017-10-11 18:37
 **/
public class Demo {

    public static void main(String[] args) throws Exception {

        String cmd = "calc";
        Runtime.getRuntime().exec(cmd);

        System.out.println(foo(3));

    }


    private static int foo(int n) {
        if (n < 2) {
            return n;
        } else
            return 2 * foo(n - 1) + foo(n - 2);
    }
}



