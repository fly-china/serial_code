package com.lpf.serial.test;

import com.lpf.serial.pub.itf.IGetSerialCodeService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 * User: Windows
 * Date: 16-3-12
 * Time: 下午3:31
 * To change this template use File | Settings | File Templates.
 */
public class Starter {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("META-INF/spring/*.xml");
        applicationContext.start();

        IGetSerialCodeService getSerialCodeService = applicationContext.getBean(IGetSerialCodeService.class);
        System.out.println(getSerialCodeService.getSerialCode("1000","01"));
    }
}
