package com.ipaynow.serial.test;

import com.ipaynow.serial.pub.itf.IGetSerialCodeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName: GetSerialCodeServiceTest
 * @Description: TODO
 * @Author
 * @Date 2016/3/23 10:30
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:META-INF/spring/spring.xml")
public class GetSerialCodeServiceTest {

    @Resource(name = "getSerialCodeService")
    private IGetSerialCodeService getSerialCodeService;

    private static Map<String, Integer> map = new ConcurrentHashMap<String, Integer>();
    private static final Logger logger = LoggerFactory.getLogger(GetSerialCodeServiceTest.class);
    private static int count = 0;

    private CountDownLatch cdl = new CountDownLatch(100);

    @Test
    public void getPospSerialCodeTest() throws InterruptedException {
        String serialCode = getSerialCodeService.getPospSerialCode("1000");
        System.out.println("**********************serialCode=" + serialCode);
    }


    @Test
    public void getSerialCodeTest() throws InterruptedException {
        String serialCode = getSerialCodeService.getSerialCode("1000", "01");
        System.out.println("**********************serialCode=" + serialCode);
       /* while(true){

        }*/

        ExecutorService pool = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 100; i++) {
            pool.execute(new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int j = 0; j < 501; j++) {
                            String serialCode = getSerialCodeService.getSerialCode("1000", "01");
                            System.out.println("**********************serialCode=" + serialCode);
                            String serialCode2 = getSerialCodeService.getSerialCode("2000", "01");
                            System.out.println("**********************serialCode=" + serialCode2);
                            if (serialCode == null || serialCode2 == null) {
                                throw new Exception("出现空流水号");
                            }

                            Integer value = map.get(serialCode);
                            if (value == null) {
                                map.put(serialCode, 1);
                            } else {
                                map.put(serialCode, value + 1);
                            }
                        }
                        cdl.countDown();
                        logger.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" + cdl.getCount() + "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }));
        }
        try {
            cdl.await();
            logger.info("-------------------------");
            logger.info("mapSize=" + map.size());
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if(entry.getKey()==null){
                    System.out.println("===========");
                }
            }
            logger.info("-------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
       /* while(true){

        }*/


    }
}