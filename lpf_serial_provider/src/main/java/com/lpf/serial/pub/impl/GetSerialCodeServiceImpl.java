package com.lpf.serial.pub.impl;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.lpf.serial.domain.App;
import com.lpf.serial.itf.IAppService;
import com.lpf.serial.itf.IGenerateSerialCodeService;
import com.lpf.serial.itf.ISerialCode;
import com.lpf.serial.pub.itf.IGetSerialCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @ClassName: GetSerialCodeServiceImpl
 * @Description: TODO
 */
@Service(value = "getSerialCodeService")
public class GetSerialCodeServiceImpl implements IGetSerialCodeService, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(GetSerialCodeServiceImpl.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final SimpleDateFormat sdfDay = new SimpleDateFormat("yyMMdd");



    private static final String TABLENAME_PREFIX = "serialcode_";

    private final static Map<String, LinkedBlockingQueue<Long>> mappingTable = new ConcurrentHashMap<String, LinkedBlockingQueue<Long>>();

    //监听触发值（监听到小于该值才添加）
    private static final int QUEUE_LISTERNER_COUNT = 15000;
    //触发监听后添加个数
    private static final int QUEUE_OFFER_COUNT = 5000;
    //初始化队列添加个数
    private static final int QUEUE_INIT_COUNT = 18000;
    //数据库自增长步长
    private static final int stepFactor = 10;
    //监听周期(秒)
    private static final int LISTERNER_CYCLE = 1;

    //管理添加队列的线程池
    private static final ExecutorService pool = Executors.newCachedThreadPool();

    @Resource(name = "generateSerialCodeService")
    private IGenerateSerialCodeService generateSerialCodeService;
    @Resource(name = "appService")
    private IAppService appService;

    @Resource
    private ISerialCode serialCodeService;

    @Override
    public String getSerialCode(String sysNo, String bizNo) {
        String serialCode = "";
        logger.info("GetSerialCodeServiceImpl.getSerialCode|sysNo=" + sysNo + "|bizNo=" + bizNo);
        if (!checkParam(sysNo, bizNo)) return "";
        try {
            long id = generateSerialCode(TABLENAME_PREFIX + sysNo, sysNo);
            if (id > 0) {
                StringBuffer sb = new StringBuffer(sysNo);
                sb.append(bizNo).append(sdf.format(new Date()));
                String tempId = "00000000" + id;
                tempId = tempId.substring(tempId.length() - 7);
                sb.append(tempId);
                serialCode = sb.toString();
            }
            logger.info("GetSerialCodeServiceImpl.getSerialCode|sysNo=" + sysNo + "|bizNo=" + bizNo + "|serialCode=" + serialCode);
            return serialCode;
        }catch (Exception e){
            logger.error("GetSerialCodeServiceImpl.getSerialCode|获取失败，发生异常",e);
        }
        return "";
    }

    @Override
    public String getPospSerialCode(String sysNo) {
        String serialCode = "";
        logger.info("GetSerialCodeServiceImpl.getPospSerialCode|sysNo=" + sysNo);
        if (!checkPopsParam(sysNo)) return "";
        try {
            long id = generateSerialCode(TABLENAME_PREFIX + sysNo, sysNo);
            if (id > 0) {
                StringBuffer sb = new StringBuffer();
                sb.append(sdfDay.format(new Date()));
                String tempId = "00000000" + id;
                tempId = tempId.substring(tempId.length() - 6);
                sb.append(tempId);
                serialCode = sb.toString();
            }
            logger.info("GetSerialCodeServiceImpl.getPospSerialCode|sysNo=" + sysNo + "|bizNo=" + "|serialCode=" + serialCode);
            return serialCode;
        }catch (Exception e){
            logger.error("GetSerialCodeServiceImpl.getPospSerialCode|获取失败，发生异常",e);
        }
        return "";
    }

    @Override
    public String getCMBSerialCode(String sysNo) {
        logger.info("GetSerialCodeServiceImpl.getCMBSerialCode|sysNo=" + sysNo);
        if (!checkPopsParam(sysNo)) return "";
        try {
            long id = generateSerialCode(TABLENAME_PREFIX + sysNo, sysNo);
            if (id > 0) {
                String tempId = "0000000000" + id;
                tempId = tempId.substring(tempId.length() - 10);
                return tempId;
            }
        }catch (Exception e){
            logger.error("GetSerialCodeServiceImpl.getCMBSerialCode|获取失败，发生异常",e);
        }
        return "";
    }


    private static final String sevenDigitsSysNo = "65535";
    @Override
    public String getSevenDigitsSerialCode() {
        logger.info("GetSerialCodeServiceImpl.getSevenDigitsSerialCode");
        try {
            long id = generateSerialCode(TABLENAME_PREFIX + sevenDigitsSysNo, sevenDigitsSysNo);
            if (id > 0) {
                String tempId = "00000000" + id;
                tempId = tempId.substring(tempId.length() - 7);
                return tempId;
            }
        }catch (Exception e){
            logger.error("GetSerialCodeServiceImpl.getSevenDigitsSerialCode|获取失败，发生异常",e);
        }
        return "";
    }

    private boolean checkParam(String sysNo, String bizNo) {
        if (StringUtils.isBlank(sysNo) || StringUtils.isBlank(bizNo)) {
            logger.error("GetSerialCodeServiceImpl.getSerialCode param is null");
            return false;
        }
        if (sysNo.length() != 4 || bizNo.length() != 2) {
            logger.error("GetSerialCodeServiceImpl.getSerialCode param length is invalid");
            return false;
        }
        return true;
    }

    private boolean checkPopsParam(String sysNo) {
        if (StringUtils.isBlank(sysNo)) {
            logger.error("GetSerialCodeServiceImpl.getSerialCode param is null");
            return false;
        }
        return true;

    }

    public Long generateSerialCode(String tableName, String sysNo) throws Exception{
        LinkedBlockingQueue<Long> queue = mappingTable.get(tableName);
        if (queue == null) {
            if(serialCodeService.existsTables(tableName)) {
                synchronized (GetSerialCodeServiceImpl.class) {
                    queue = mappingTable.get(tableName);
                    if (queue == null) {
                        logger.info("GetSerialCodeServiceImpl.generateSerialCode|找不到对应的系统" + tableName);
                        //保存到app表
                        if (this.saveApp(sysNo) > 0) {
                            logger.info("GetSerialCodeServiceImpl.generateSerialCode|保存" + sysNo + "成功");
                        }
                        queue = new LinkedBlockingQueue<Long>(20000);
                        mappingTable.put(tableName, queue);
                        pool.execute(new InsertThread(tableName, queue, QUEUE_INIT_COUNT));
                    }
                }
            }else{
                throw new Exception(tableName+"表不存在！");
            }
        }
        Long id = null;
        try {
            id = queue.take();
        } catch (InterruptedException e) {
            logger.error("queue.take()出错",e);
        }
        return id;
    }

    /**
     * 保存App
     * @param sysNO
     * @return
     */
    private int saveApp(String sysNO){
        try {
            App app = new App();
            app.setAppId(sysNO);
            app.setAppName(sysNO);
            return appService.insert(app);
        }catch(Exception e){
            logger.error("GetSerialCodeServiceImpl.saveApp|异常",e);
        }
        return 0;
    }

    //启动监听线程
    public void afterPropertiesSet() throws Exception {
        logger.warn("afterPropertiesSet,初始化缓存队列");
        List<App> appList = appService.getAll();
        for (App app : appList) {
            String tableName = TABLENAME_PREFIX + app.getAppId();
            LinkedBlockingQueue queue = new LinkedBlockingQueue<Long>(20000);
            mappingTable.put(tableName, queue);
            pool.execute(new InsertThread(tableName, queue, QUEUE_INIT_COUNT));
        }
        logger.warn("afterPropertiesSet,启动队列监听线程");
        new Thread(new QueueListerner()).start();
    }

    //周期监听队列的长度，当小于阀值的时添加
    class QueueListerner implements Runnable {
        int lognum =10;
        int count = 0;
        boolean logFlag = false;
        public void run() {
            while (true) {
                count ++ ;
                logFlag = false;
                if(count>=lognum) {
                    logFlag = true;
                    logger.info("检查队列长度开始");
                }
                for (Map.Entry<String, LinkedBlockingQueue<Long>> entry : mappingTable.entrySet()) {
                    LinkedBlockingQueue<Long> queue = entry.getValue();
                    if(logFlag) {
                        logger.info(entry.getKey() + "队列长度为" + queue.size());
                    }
                    if (queue.size() < QUEUE_LISTERNER_COUNT) {
                        //启动线程去创建
                        pool.execute(new InsertThread(entry.getKey(), queue, QUEUE_OFFER_COUNT));
                    }
                }
                if(logFlag) {
                    logger.info("检查队列长度结束");
                    count = 0;
                }
                try {
                    Thread.sleep(LISTERNER_CYCLE * 1000);
                } catch (InterruptedException e) {
                    logger.error("监听线程休眠异常",e);
                }
            }
        }
    }

    private final static ConcurrentSkipListSet<String> set = new ConcurrentSkipListSet<String>();

    //插入线程
    class InsertThread implements Runnable {
        private String tableName;
        private LinkedBlockingQueue<Long> queue;
        private Integer count;

        public InsertThread(String tableName, LinkedBlockingQueue<Long> queue, Integer count) {
            this.tableName = tableName;
            this.queue = queue;
            this.count = count;
        }

        public void run() {
            synchronized (InsertThread.class) {
                if (set.contains(tableName)) {
                    return;
                }
                set.add(tableName);
            }
            try {
                logger.info(tableName + "队列size为" + queue.size() + ",开始添加" + QUEUE_OFFER_COUNT + "个到队列中");
                long startTime = System.currentTimeMillis();
                int size = (count + stepFactor -1) / stepFactor;
                int mod = count % stepFactor;
                int preSize = stepFactor;
                for (int i = 0; i < size; i++) {
                    Long num = generateSerialCodeService.generateSerialCode(tableName);
                    if(i == size-1 && mod!=0){
                        preSize = mod;
                    }
                    for(int j = preSize; j>0; j--){
                        queue.offer(num-j);
                    }
                }
                Date end = new Date();
                logger.info("为队列" + tableName + "添加完成,现在size为" + queue.size() + ",耗时为" + (System.currentTimeMillis()-startTime + "毫秒"));
            } catch (Exception e) {
                logger.error("从数据库获取流水号失败",e);
            } finally {
                set.remove(tableName);
            }
        }
    }
}