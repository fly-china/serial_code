package com.ipaynow.serial.impl;

import com.ipaynow.serial.dao.SerialCodeMapper;
import com.ipaynow.serial.itf.ISerialCode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2016/7/20.
 */
@Service
public class SerialCodeService implements ISerialCode{

    @Resource
    private SerialCodeMapper serialCodeMapper;


    public List<String> getSerialCodeTableName() {
        return serialCodeMapper.getSerialCodeTableName();
    }


    public int getSerialCodeTableCount(String tableName) {
        return serialCodeMapper.getSerialCodeTableCount(tableName);
    }


    public int clearSerialCodeData(String tableName){
        return serialCodeMapper.clearSerialCodeData(tableName);
    }

    public boolean existsTables(String tableName) {
       if(serialCodeMapper.getTableCount(tableName)>0){
           return true;
       }else{
           return false;
       }
    }
}
