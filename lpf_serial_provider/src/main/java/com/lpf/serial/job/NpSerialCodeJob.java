package com.lpf.serial.job;

import com.lpf.serial.component.DSLockComponent;
import com.lpf.serial.domain.DistributeLock;
import com.lpf.serial.impl.SerialCodeService;
import com.lpf.common.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/7/20.
 */
public class NpSerialCodeJob {
    private static Logger logger = LoggerFactory.getLogger(NpSerialCodeJob.class);

    /**
     * 清理数据临界值
     */
    private static int criticalValue = 500;
    @Resource
    private SerialCodeService serialCodeService;
    @Resource
    private DSLockComponent dsLockComponent;

    /**
     * 清理数据
     */
    public void clearSerialCodeHistoryData() {
        long startTime = System.currentTimeMillis();
        logger.info("NpSerialCodeJob.clearSerialCodeHistoryData|流水号数据清理任务开始执行");
        List<String> tableNameList = serialCodeService.getSerialCodeTableName();
        DistributeLock distributeLock = new DistributeLock();
        for (int i = 0; i < tableNameList.size(); i++) {
            String tableName = tableNameList.get(i);
            distributeLock.setId(DateUtil.getStringFromDate(new Date(), "yyyyMMdd") + tableName);
            boolean flag = dsLockComponent.lock(distributeLock, 1);
            if (flag) {
                try {
                    int count = serialCodeService.getSerialCodeTableCount(tableNameList.get(i));
                    logger.info("NpSerialCodeJob.clearSerialCodeHistoryData|流水号表：" + tableName + "，共有数据" + count + "条");
                    if (count > criticalValue) {
                        logger.info("NpSerialCodeJob.clearSerialCodeHistoryData|清理表：" + tableName);
                        int result = serialCodeService.clearSerialCodeData(tableName);
                        if (result > 0) {
                            logger.info("NpSerialCodeJob.clearSerialCodeHistoryData|清理表" + tableName + "成功");
                        } else {
                            logger.info("NpSerialCodeJob.clearSerialCodeHistoryData|清理表" + tableName + "失败");
                        }
                    }
                } catch (Exception e) {
                    logger.error("NpSerialCodeJob.clearSerialCodeHistoryData|清理表" + tableName + "发生异常", e);
                } finally {
                    dsLockComponent.unlock(distributeLock, 1);
                }
            } else {
                logger.info("NpSerialCodeJob.clearSerialCodeHistoryData|已经有机器正在清理表" + tableName);
            }
        }
        long endTime = System.currentTimeMillis();
        logger.info("NpSerialCodeJob.clearSerialCodeHistoryData|流水号数据清理任务执行结束,共耗时：" + (endTime - startTime) + "毫秒");
    }
}
