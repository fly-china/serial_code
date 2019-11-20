package com.lpf.serial.component;


import com.lpf.serial.dao.DistributeLockMapper;
import com.lpf.serial.domain.DistributeLock;
import com.lpf.serial.domain.DistributeLockExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * ClassName:DSLockComponent
 * Description: 分布式锁公共类
 * Date: 2016/4/13 8:58
 */

@Transactional
@Component(value = "dsLockComponent")
public class DSLockComponent {

    private static Logger logger = LoggerFactory.getLogger(DSLockComponent.class);

    @Autowired
    private DistributeLockMapper distributeLockMapper;

    final String STATE_LOCK = "1"; // 锁定状态
    final String STATE_UNLOCK = "0"; // 未锁定状态
    final int retryTimes = 3;

    /**
     * 获取分布式锁
     *
     * @param dslock 锁信息
     * @param times  尝试次数，小于等于0默认尝试三次
     * @return true:获取成功
     */
    public boolean lock(DistributeLock dslock, Integer times) {
        if (times <= 0) {
            times = retryTimes;
        }
        for (int i = 0; i < times; i++) {
            if (this.lock(dslock) == true) {
                return true;
            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 创建锁
     *
     * @param lock 锁信息
     * @return true:加锁成功
     */
    private boolean lock(DistributeLock lock) {

        // 1.验证ID是否合法
        if (StringUtils.isEmpty(lock.getId())) {
            return false;
        }

        // 2.从数据库获取锁
        DistributeLock dslock = null;
        dslock = distributeLockMapper.selectByPrimaryKey(lock.getId());
        if (dslock == null) {
            // 2.1初次加锁
            try {
                dslock = new DistributeLock();
                dslock.setId(lock.getId());
                dslock.setMessage(lock.getMessage());
                dslock.setModifier(lock.getModifier());
                dslock.setState(STATE_LOCK); // 字符串
                dslock.setOwner(lock.getOwner());
                dslock.setVersion(0L);
                dslock.setModifiedtime(new Date());
                dslock.setCreatedTime(new Date());
                distributeLockMapper.insert(dslock);
                return true;
            } catch (Exception e) {
                logger.error("DSLockComponent.lock|初次加锁初次加锁异常", e);
                return false;
            }
        } else {
            // 2.2验证锁
            Long oldVersion = dslock.getVersion();
            if (STATE_LOCK.equals(dslock.getState())) {
                // 已经锁定
                return false;
            } else {
                // 未锁定
                try {
                    dslock.setVersion(dslock.getVersion() + 1);
                    dslock.setState(STATE_LOCK);
                    dslock.setMessage(dslock.getMessage());
                    dslock.setModifier(dslock.getModifier());
                    dslock.setModifiedtime(new Date());

                    // 根据ID和version更新数据库，更新成功即获取锁成功
                    DistributeLockExample updateExample = new DistributeLockExample();
                    DistributeLockExample.Criteria criteria = updateExample.createCriteria();
                    criteria.andIdEqualTo(dslock.getId());
                    criteria.andVersionEqualTo(oldVersion);
                    int result = distributeLockMapper.updateByExample(dslock, updateExample);
                    if (1 != result) {
                        return false;
                    } else {
                        return true;
                    }
                } catch (Exception e) {
                    logger.error("DSLockComponent.lock|数据库加锁异常", e);
                    return false;
                }
            }
        }
    }

    public boolean unlock(DistributeLock lock, Integer times) {
        if (times <= 0) {
            times = retryTimes;
        }
        for (int i = 0; i < times; i++) {
            if (this.unlock(lock) == true) {
                return true;
            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 释放锁资源
     *
     * @param lock 锁信息
     * @return true:释放成功
     */
    private boolean unlock(DistributeLock lock) {

        // 1.验证ID有效性
        if (StringUtils.isEmpty(lock.getId())) {
            return false;
        }

        // 2.获取锁对象
        DistributeLock dslock = distributeLockMapper.selectByPrimaryKey(lock.getId());
        if (dslock == null) {
            // 2.1锁尚未创建
            return true;
        } else {
            // 2.1锁创建过
            try {
                Long oldVersion = dslock.getVersion();
                dslock.setVersion(dslock.getVersion() + 1);
                dslock.setState(STATE_UNLOCK);
                dslock.setMessage(lock.getMessage());
                dslock.setModifier(lock.getModifier());

                DistributeLockExample updateExample = new DistributeLockExample();
                DistributeLockExample.Criteria criteria = updateExample.createCriteria();
                criteria.andIdEqualTo(dslock.getId());
                criteria.andVersionEqualTo(oldVersion);
                int result = distributeLockMapper.updateByExample(dslock, updateExample);
                if (1 != result) {
                    return false;
                } else {
                    return true;
                }
            } catch (Exception e) {
                logger.error("DSLockComponent.unlock数据库释放锁异常", e);
                return false;
            }
        }
    }

}
