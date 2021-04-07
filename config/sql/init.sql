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
INSERT INTO `sys_config` VALUES (1, '系统参数', 'systemParam', '0', '0', 10, '2016-12-17 22:32:35', 1, '2016-12-17 22:32:35', 1);
INSERT INTO `sys_config` VALUES (2, 'API参数', 'apiParam', '0', '0', 11, '2016-12-17 22:33:41', 1, '2016-12-17 22:33:41', 1);
INSERT INTO `sys_config` VALUES (4, '版权', 'copyright', '©广西杉网科技有限公司 版权所有', '1', 10, '2016-12-17 23:07:21', 1, '2016-12-17 23:07:21', 1);
INSERT INTO `sys_config` VALUES (5, 'API是否开启', 'API.FLAG', 'true', '2', 110, '2016-12-17 23:12:26', 1, '2016-12-17 23:12:26', 1);
INSERT INTO `sys_config` VALUES (6, 'ip黑名单，逗号分隔', 'API.IP.BLACK', '127.0.0.122,localhost22', '2', 111, '2016-12-17 23:16:29', 1, '2016-12-17 23:16:29', 1);
INSERT INTO `sys_config` VALUES (7, '支持的版本，逗号分隔', 'API.VERSIONS', '1.0.0,1.0.1', '2', 112, '2016-12-17 23:17:00', 1, '2016-12-17 23:17:00', 1);
INSERT INTO `sys_config` VALUES (8, '登陆验证是否开启', 'API.LOGIN.VALID', 'false', '2', 114, '2016-12-17 23:17:23', 1, '2016-12-17 23:17:23', 1);
INSERT INTO `sys_config` VALUES (9, '站点参数', 'siteParam', '0', '0', 12, '2016-12-31 16:27:12', 1, '2016-12-31 16:27:12', 1);
INSERT INTO `sys_config` VALUES (10, '多站点标示', 'SITE.MULTI.FLAG', 'false', '9', 211, '2016-12-31 16:28:02', 1, '2016-12-31 16:28:02', 1);
INSERT INTO `sys_config` VALUES (11, '站点根目录', 'SITE.TEMPLATE.PATH', '/template/', '9', 212, '2016-12-31 16:28:43', 1, '2016-12-31 16:28:43', 1);
INSERT INTO `sys_config` VALUES (12, 'Session站点列表', 'SITE.SESSION.SITES', 'sites', '9', 213, '2016-12-31 16:30:17', 1, '2016-12-31 16:30:17', 1);
INSERT INTO `sys_config` VALUES (13, 'Session站点', 'SITE.SESSION.SITE', 'site', '9', 214, '2016-12-31 16:30:38', 1, '2016-12-31 16:30:38', 1);
INSERT INTO `sys_config` VALUES (14, 'API参数加密是否开启', 'API.PARAM.ENCRYPT', 'false', '2', 115, '2017-03-17 23:17:23', 1, '2017-03-17 23:17:23', 1);
INSERT INTO `sys_config` VALUES (15, '文件备份参数', 'backupParam', '0', '0', 10, '2017-04-05 03:49:21', 1, '2017-04-05 03:49:21', 1);
INSERT INTO `sys_config` VALUES (16, '文件备份类型名称', 'backup.name', 'filemanger', '15', 10, '2017-04-05 03:50:12', 1, '2017-04-05 03:50:12', 1);
INSERT INTO `sys_config` VALUES (17, '文件系统备份路径', 'backup.filemanger.path', 'D:\\test', '15', 10, '2017-04-05 03:50:42', 1, '2017-04-05 03:50:42', 1);
INSERT INTO `sys_config` VALUES (18, '阿里云存储bucketname', 'backup.oss.bucketname', 'jflyfox', '15', 10, '2017-04-05 22:58:05', 1, '2017-04-05 22:58:05', 1);
INSERT INTO `sys_config` VALUES (19, '阿里云存储endpoint', 'backup.oss.endpoint', 'http://oss-cn-beijing.aliyuncs.com', '15', 10, '2017-04-05 22:59:01', 1, '2017-04-05 22:59:01', 1);
INSERT INTO `sys_config` VALUES (20, '阿里云存储ID', 'backup.oss.id', '', '15', 10, '2017-04-05 22:59:24', 1, '2017-04-05 22:59:24', 1);
INSERT INTO `sys_config` VALUES (21, '阿里云存储KEY', 'backup.oss.key', '', '15', 10, '2017-04-05 22:59:51', 1, '2017-04-05 22:59:51', 1);

-- ----------------------------
-- Table structure for t_api_log
-- ----------------------------
DROP TABLE IF EXISTS `t_api_log`;
CREATE TABLE `t_api_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `app_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'APPID',
  `user_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户ID',
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '调用方法',
  `version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '版本号',
  `biz_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '业务参数',
  `log_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '日志类型',
  `resp_msg` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '响应信息',
  `time_cost` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '耗时-毫秒',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_api_log
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
