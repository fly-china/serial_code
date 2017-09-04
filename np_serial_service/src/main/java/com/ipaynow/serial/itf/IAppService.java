package com.ipaynow.serial.itf;

import com.ipaynow.serial.domain.App;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Windows
 * Date: 16-3-12
 * Time: 下午2:42
 * To change this template use File | Settings | File Templates.
 */
public interface IAppService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public App getApp(String appId);

    List<App> getAll();

    int insert(App record);
}
