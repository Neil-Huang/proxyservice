/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : 127.0.0.1:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 03/01/2020 10:30:47
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for Cfg_Proxy
-- ----------------------------
DROP TABLE IF EXISTS `Cfg_Proxy`;
CREATE TABLE `Cfg_Proxy` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '代理自增ID',
  `ip` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '使用ip和port作为主键避免重复插入',
  `port` int(11) NOT NULL COMMENT '使用ip和port作为主键避免重复插入',
  `anonymousType` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'transparent、anonymous、distorting、elite',
  `protocolType` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'http、https、socks4、socks5、socks',
  `country` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `area` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `valid` bit(1) NOT NULL,
  `invalidTime` bigint(20) DEFAULT NULL COMMENT 'ms级别时间戳',
  `lastSurviveTime` bigint(20) DEFAULT NULL COMMENT 'ms级时间',
  `checkTime` bigint(20) DEFAULT NULL COMMENT 'ms级别时间戳',
  `checkStatus` int(11) NOT NULL COMMENT '0:未验证；1:已验证',
  `score` float DEFAULT NULL,
  `sourceSite` varbinary(50) NOT NULL,
  `validTime` int(11) DEFAULT NULL,
  `crawlTime` bigint(20) NOT NULL COMMENT 'ms级别时间戳',
  `responseTime` bigint(20) DEFAULT NULL COMMENT 'ms级时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx-ip-port` (`ip`,`port`) USING BTREE COMMENT 'ip和端口唯一索引'
) ENGINE=InnoDB AUTO_INCREMENT=160 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS = 1;
