package com.ipaynow.serial.dao;

import com.ipaynow.serial.domain.App;
import com.ipaynow.serial.domain.AppExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AppMapper {
    int countByExample(AppExample example);

    int deleteByExample(AppExample example);

    int insert(App record);

    int insertSelective(App record);

    List<App> selectByExample(AppExample example);

    int updateByExampleSelective(@Param("record") App record, @Param("example") AppExample example);

    int updateByExample(@Param("record") App record, @Param("example") AppExample example);
}