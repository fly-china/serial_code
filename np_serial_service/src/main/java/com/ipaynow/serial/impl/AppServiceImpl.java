package com.ipaynow.serial.impl;

import com.ipaynow.serial.dao.AppMapper;
import com.ipaynow.serial.domain.App;
import com.ipaynow.serial.domain.AppExample;
import com.ipaynow.serial.itf.IAppService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Windows
 * Date: 16-3-12
 * Time: 下午2:48
 * To change this template use File | Settings | File Templates.
 */
@Service(value="appService")
public class AppServiceImpl implements IAppService {

    @Resource
    private AppMapper appMapper;

    public App getApp(String appId) {
        AppExample appExample = new AppExample();
        appExample.createCriteria().andAppIdEqualTo("0001");

        List<App> appList = appMapper.selectByExample(appExample);

        if (CollectionUtils.isNotEmpty(appList)) {
            for (App app : appList) {
                System.out.println(app.getAppName());
            }
            return appList.get(0);
        }

        return null;
    }

    public List<App> getAll() {
        return appMapper.selectByExample(new AppExample());
    }

    public int insert(App record){
        return appMapper.insert(record);
    }
}
