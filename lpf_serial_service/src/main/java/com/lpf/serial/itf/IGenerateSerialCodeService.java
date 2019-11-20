package com.lpf.serial.itf;

import java.sql.SQLException;

/**
 * @ClassName: IGenerateSerialCodeService
 * @Description: TODO
 * @Author mengliangliang
 * @Date 2016/3/23 9:42
 */
public interface IGenerateSerialCodeService {

    long generateSerialCode(String tableName) throws Exception;

}
