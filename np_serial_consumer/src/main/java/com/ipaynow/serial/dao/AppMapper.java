package com.ipaynow.serial.dao;

import com.ipaynow.serial.domain.App;
import com.ipaynow.serial.domain.AppExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AppMapper {
    int countByExample(AppExample example);

    int deleteByExample(AppExample example);

    int deleteByPrimaryKey(String appId);

    int insert(App record);

    int insertSelective(App record);

    List<App> selectByExample(AppExample example);

    App selectByPrimaryKey(String appId);

    int updateByExampleSelective(@Param("record") App record, @Param("example") AppExample example);

    int updateByExample(@Param("record") App record, @Param("example") AppExample example);

    int updateByPrimaryKeySelective(App record);

    int updateByPrimaryKey(App record);
}