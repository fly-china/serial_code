-- ----------------------------
-- Table structure for `app`
-- ----------------------------
DROP TABLE IF EXISTS `app`;
CREATE TABLE `app` (
  `app_id` varchar(64) NOT NULL DEFAULT '',
  `app_name` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of app
-- ----------------------------
INSERT INTO `app` VALUES ('1000', '业务系统1');
INSERT INTO `app` VALUES ('2000', '业务系统2');


-- ----------------------------
-- Table structure for `distribute_lock`
-- ----------------------------
DROP TABLE IF EXISTS `distribute_lock`;
CREATE TABLE `distribute_lock` (
  `id` varchar(128) NOT NULL COMMENT '主键',
  `message` varchar(256) DEFAULT NULL COMMENT '消息',
  `owner` varchar(40) DEFAULT NULL COMMENT '锁获取者',
  `state` varchar(20) DEFAULT NULL COMMENT '锁定状态1:锁定状态，0：未锁定状态',
  `version` bigint(20) DEFAULT NULL COMMENT '版本号',
  `creator` varchar(64) DEFAULT NULL COMMENT '创建人',
  `created_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modifier` varchar(40) DEFAULT NULL COMMENT '修改者',
  `modifiedtime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分布式锁';

-- ----------------------------
-- 示例而已
-- ----------------------------
INSERT INTO `distribute_lock` VALUES ('20160801serialcode_1000', null, null, '0', '111', null, '2016-08-01 15:35:03', null, '2016-08-01 20:11:47');
INSERT INTO `distribute_lock` VALUES ('20160801serialcode_2000', null, null, '0', '111', null, '2016-08-01 15:35:09', null, '2016-08-01 20:11:49');
INSERT INTO `distribute_lock` VALUES ('NpSerialCodeJob_20160801', null, null, '0', '68', null, '2016-08-01 14:35:01', null, '2016-08-01 15:30:00');

-- ----------------------------
-- Table structure for `increase_record`
-- ----------------------------
DROP TABLE IF EXISTS `increase_record`;
CREATE TABLE `increase_record` (
  `id` int(12) NOT NULL AUTO_INCREMENT,
  `version` int(12) NOT NULL DEFAULT '0',
  `sys_no` varchar(10) NOT NULL COMMENT '系统编号',
  `current_val` int(12) NOT NULL DEFAULT '0',
  `created_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COMMENT='序列号生成记录表';

-- ----------------------------
-- Records of increase_record
-- ----------------------------
INSERT INTO `increase_record` VALUES ('1', '0', '1000', '0', '2017-03-22 10:01:46');
INSERT INTO `increase_record` VALUES ('6', '0', '2000', '0', '2017-03-22 10:01:47');


-- ----------------------------
-- Table structure for `serial_number_record`
-- ----------------------------
DROP TABLE IF EXISTS `serial_number_record`;
CREATE TABLE `serial_number_record` (
  `id` int(12) NOT NULL AUTO_INCREMENT,
  `version` int(12) unsigned NOT NULL DEFAULT '0',
  `key` varchar(20) NOT NULL COMMENT '系统编号+currentDay',
  `current_val` int(12) unsigned NOT NULL DEFAULT '0',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key` (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='序列号生成记录表';


DROP TABLE IF EXISTS `serialcode_1000`;
CREATE TABLE `serialcode_1000` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '业务系统ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='测试表1';

DROP TABLE IF EXISTS `serialcode_2000`;
CREATE TABLE `serialcode_2000` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '业务系统ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='测试表2';
