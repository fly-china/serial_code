package com.lpf.job;

/**
 * Created by ishare on 17-6-15.
 * 筛选条件
 */
public class JobCondition {
    static String gj;  //工作经验
    static String city;//城市
    static String kd;//编程语言
    static String filename;

    public static void setFilename(String filename) {
        JobCondition.filename = filename;
    }

    public static void setGj(String gj) {
        JobCondition.gj = gj;
    }



    public static void setCity(String city) {
        JobCondition.city = city;
    }



    public static void setKd(String kd) {
        JobCondition.kd = kd;
    }
}
