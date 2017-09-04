package com.ipaynow.serial.pub.itf;

/**
 * @ClassName: IGetSerialCodeService
 * @Description: 获取流水号服务
 * @Author mengliangliang
 * @Date 2016/3/23 10:13
 */
public interface IGetSerialCodeService {

    /**
     * 根据系统编号和业务员编号生成流水号
     * @param sysNo  非空且长度固定为4
     * @param bizNo  非空且长度固定为2
     * @return 返回类似000101201603231050320000031(sysNo + bizNo + timestamp + 8位整数)
     *         如果参数不合法，或者系统出错，返回空“”
     */
    String getSerialCode(String sysNo, String bizNo);

    /**
     * posp系统流水号
     * @param sysNo
     * @return
     */
    String getPospSerialCode(String sysNo);


    /**
     * 生成10位流水号
     * @param sysNo
     * @return
     */
    String getCMBSerialCode(String sysNo);


    /**
     * 生成7位流水号
     * @return
     */
    String getSevenDigitsSerialCode();

}
