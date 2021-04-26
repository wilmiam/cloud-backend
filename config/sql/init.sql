/*
 Navicat Premium Data Transfer

 Source Server         : 测试库
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : 47.107.148.253:3306
 Source Schema         : cloud

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 07/04/2021 18:39:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '名称',
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '键',
  `value` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '值',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '类型',
  `sort` int(11) NOT NULL DEFAULT 10 COMMENT '排序号',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `create_id` bigint(20) NULL DEFAULT 0 COMMENT '创建者',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `update_id` bigint(20) NULL DEFAULT 0 COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, 'API是否开启', 'API.FLAG', 'true', 'bool', 110, '2016-12-17 23:12:26', 1, '2016-12-17 23:12:26', 1);
INSERT INTO `sys_config` VALUES (2, 'ip黑名单，逗号分隔', 'API.IP.BLACK', '127.0.0.122,localhost22', 'txt', 111, '2016-12-17 23:16:29', 1, '2016-12-17 23:16:29', 1);
INSERT INTO `sys_config` VALUES (3, '支持的版本，逗号分隔', 'API.VERSIONS', '1.0.0,1.0.1', 'txt', 112, '2016-12-17 23:17:00', 1, '2016-12-17 23:17:00', 1);
INSERT INTO `sys_config` VALUES (4, '登陆验证是否开启', 'API.LOGIN.VALID', 'false', 'bool', 114, '2016-12-17 23:17:23', 1, '2016-12-17 23:17:23', 1);
INSERT INTO `sys_config` VALUES (5, 'API参数加密是否开启', 'API.PARAM.ENCRYPT', 'false', 'bool', 115, '2017-03-17 23:17:23', 1, '2017-03-17 23:17:23', 1);
INSERT INTO `sys_config` VALUES (6, '腾讯云秘钥ID', 'CLOUD.SECRET.ID', 'AKIDwi1CB9h9HDex4yaahdbVHzVXN3XpqzEH', 'txt', 10, '2017-04-05 22:59:24', 1, '2017-04-05 22:59:24', 1);
INSERT INTO `sys_config` VALUES (7, '腾讯云秘钥KEY', 'CLOUD.SECRET.KEY', 'SghR2ECxh5D3rsmiTH2XoqrbXGl5A0a0', 'txt', 10, '2017-04-05 22:59:51', 1, '2017-04-05 22:59:51', 1);

-- ----------------------------
-- Table structure for t_api_log
-- ----------------------------
DROP TABLE IF EXISTS `t_api_log`;
CREATE TABLE `t_api_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `app_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT 'APPID',
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '用户ID',
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '调用方法',
  `version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '版本号',
  `biz_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '业务参数',
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'IP',
  `log_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '日志类型',
  `resp_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '响应信息',
  `error_info` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '错误信息',
  `time_cost` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '耗时-毫秒',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = 'api调用记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_api_log
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
