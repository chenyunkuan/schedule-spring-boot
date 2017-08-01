/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2016-12-06 19:26:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for job
-- ----------------------------
DROP TABLE IF EXISTS `job`;
CREATE TABLE `job` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id自增',
  `job_name` varchar(255) DEFAULT NULL COMMENT 'job名称（key）',
  `group_name` varchar(255) DEFAULT NULL COMMENT 'group名称（key）',
  `trigger_name` varchar(255) DEFAULT NULL COMMENT 'trigger名称（key）',
  `cron_expression` varchar(255) DEFAULT NULL COMMENT '时间表达式',
  `url` varchar(255) DEFAULT NULL COMMENT '需要访问的url',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of job
-- ----------------------------
INSERT INTO `job` VALUES ('1', 'excuteExpAna', 'url-mission', 'excuteExpAnaTrigger', '0 0 2 * * ?', 'http://127.0.0.1:8080/expAna/expStatistics.do');
