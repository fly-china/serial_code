package com.lpf.serial.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by liangliang on 2016/3/28.
 */
public class SerialBeanFactory implements ApplicationContextAware {

    private static ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {

        ctx = ac;
    }

    public static Object generateBean(String beanName) {

        Object object = null;
        object = ctx.getBean(beanName);
        return object;

    }

}
