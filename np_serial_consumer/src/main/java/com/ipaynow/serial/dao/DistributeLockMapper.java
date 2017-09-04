package com.ipaynow.serial.dao;

import com.ipaynow.serial.domain.DistributeLock;
import com.ipaynow.serial.domain.DistributeLockExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

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