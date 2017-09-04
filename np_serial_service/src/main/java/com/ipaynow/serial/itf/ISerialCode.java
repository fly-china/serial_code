package com.ipaynow.serial.itf;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2016/7/20.
 */
public interface ISerialCode {

    /**
     * 获取流水号表
     * @return
     */
    List<String> getSerialCodeTableName();


    boolean existsTables(String tableName);

    /**
     * 获取流水表记录数
     * @param tableName
     * @return
     */
    int getSerialCodeTableCount(@Param("tableName") String tableName);

    /**
     * 清理数据
     * @param tableName
     * @return
     */
    int clearSerialCodeData(@Param("tableName") String tableName);
}
