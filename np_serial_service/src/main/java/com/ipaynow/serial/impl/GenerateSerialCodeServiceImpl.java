package com.ipaynow.serial.impl;

import com.ipaynow.serial.dao.SerialCodeMapper;
import com.ipaynow.serial.itf.IGenerateSerialCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

/**
 * @ClassName: GenerateSerialCodeServiceImpl
 * @Description: TODO
 * @Author mengliangliang
 * @Date 2016/3/23 9:47
 */
@Service(value = "generateSerialCodeService")
public class GenerateSerialCodeServiceImpl implements IGenerateSerialCodeService {

    private static final Logger logger = LoggerFactory.getLogger(GenerateSerialCodeServiceImpl.class);

    private static final int TRY_COUNT = 5;

    //数据库自增长步长
    private static final int stepFactor = 10;

    @Autowired
    private SerialCodeMapper serialCodeMapper;
    @Transactional(rollbackFor = Exception.class)
    public long generateSerialCode(String tableName)  throws Exception {
        long serialCode = 0;
        for (int i = 0; i < TRY_COUNT; i ++) {
            try {
                serialCodeMapper.setAutoIncrementIncrement(stepFactor);
                int count = serialCodeMapper.insertAutoID(tableName);
                if (count <= 0) {
                    logger.error("GenerateSerialCodeServiceImpl.generateSerialCode insert failed");
                } else {
                    serialCode = serialCodeMapper.getLastInsertId();
                    if(serialCode==1L){
                        logger.info("第一次初始化执行");
                        count = serialCodeMapper.insertAutoID(tableName);
                        if (count <= 0) {
                            logger.error("GenerateSerialCodeServiceImpl.generateSerialCode insert failed");
                        } else {
                            serialCode = serialCodeMapper.getLastInsertId();
                        }
                    }
                }
                if (serialCode > 0) break;
            } catch (Exception e) {
                logger.error("GenerateSerialCodeServiceImpl.generateSerialCode Exception", e);
                throw e;
            }
            //logger.info("GenerateSerialCodeServiceImpl.generateSerialCode|tryCount=" + i + "|tableName=" + tableName);
        }
        //logger.info("GenerateSerialCodeServiceImpl.generateSerialCode|serialCode=" + serialCode + "|tableName=" + tableName);
        return serialCode;
    }

}
