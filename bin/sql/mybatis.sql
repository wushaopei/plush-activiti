/*
Navicat MySQL Data Transfer

Source Server         : 本地连接
Source Server Version : 80016
Source Host           : localhost:3333
Source Database       : mybatis

Target Server Type    : MYSQL
Target Server Version : 80016
File Encoding         : 65001

Date: 2020-04-21 21:51:18
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `last_name` varchar(50) DEFAULT NULL,
  `sex` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=gbk;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES ('2', 'jetty', '1');
INSERT INTO `t_user` VALUES ('3', 'tom', '1');
INSERT INTO `t_user` VALUES ('4', 'tom', '1');
INSERT INTO `t_user` VALUES ('5', 'tom', '1');
INSERT INTO `t_user` VALUES ('6', 'tom', '1');
INSERT INTO `t_user` VALUES ('7', 'tom', '1');
INSERT INTO `t_user` VALUES ('8', 'tom', '1');
INSERT INTO `t_user` VALUES ('9', 'tom', '1');
INSERT INTO `t_user` VALUES ('10', 'tom', '1');
INSERT INTO `t_user` VALUES ('11', 'tom', '1');
