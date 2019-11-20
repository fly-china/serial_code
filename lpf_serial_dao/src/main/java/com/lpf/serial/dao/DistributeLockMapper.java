package com.lpf.serial.dao;

import com.lpf.serial.domain.DistributeLock;
import com.lpf.serial.domain.DistributeLockExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DistributeLockMapper {
    int countByExample(DistributeLockExample example);

    int deleteByExample(DistributeLockExample example);

    int deleteByPrimaryKey(String id);

    int insert(DistributeLock record);

    int insertSelective(DistributeLock record);

    List<DistributeLock> selectByExample(DistributeLockExample example);

    DistributeLock selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") DistributeLock record, @Param("example") DistributeLockExample example);

    int updateByExample(@Param("record") DistributeLock record, @Param("example") DistributeLockExample example);

    int updateByPrimaryKeySelective(DistributeLock record);

    int updateByPrimaryKey(DistributeLock record);
}