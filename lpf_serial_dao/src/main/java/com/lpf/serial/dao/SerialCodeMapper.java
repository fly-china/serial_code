package com.lpf.serial.dao;


import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SerialCodeMapper {

    int insertAutoID(String tableName);

    long getLastInsertId();

    int deleteAutoID(long autoId);

    int setAutoIncrementIncrement(int stepFactor);

    List<String> getSerialCodeTableName();

    int getSerialCodeTableCount(@Param("tableName") String tableName);

    int clearSerialCodeData(@Param("tableName") String tableName);

    int getTableCount(@Param("tableName") String tableName);





}