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
CREATE TABLE `sys_config`
(
    `id`          bigint(20)                                              NOT NULL AUTO_INCREMENT,
    `name`        varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL COMMENT '名称',
    `code`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NOT NULL COMMENT '键',
    `value`       varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '值',
    `type`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NOT NULL DEFAULT '0' COMMENT '类型',
    `sort`        int(11)                                                 NOT NULL DEFAULT 10 COMMENT '排序号',
    `create_time` datetime(0)                                             NULL     DEFAULT NULL COMMENT '创建时间',
    `create_id`   bigint(20)                                              NULL     DEFAULT 0 COMMENT '创建者',
    `update_time` datetime(0)                                             NULL     DEFAULT NULL COMMENT '更新时间',
    `update_id`   bigint(20)                                              NULL     DEFAULT 0 COMMENT '更新人',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 22
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '系统配置表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config`
VALUES (1, 'API是否开启', 'API.FLAG', 'true', 'bool', 110, '2016-12-17 23:12:26', 1, '2016-12-17 23:12:26', 1);
INSERT INTO `sys_config`
VALUES (2, 'ip黑名单，逗号分隔', 'API.IP.BLACK', '127.0.0.122,localhost22', 'txt', 111, '2016-12-17 23:16:29', 1,
        '2016-12-17 23:16:29', 1);
INSERT INTO `sys_config`
VALUES (3, '支持的版本，逗号分隔', 'API.VERSIONS', '1.0.0,1.0.1', 'txt', 112, '2016-12-17 23:17:00', 1, '2016-12-17 23:17:00', 1);
INSERT INTO `sys_config`
VALUES (4, '登陆验证是否开启', 'API.LOGIN.VALID', 'false', 'bool', 114, '2016-12-17 23:17:23', 1, '2016-12-17 23:17:23', 1);
INSERT INTO `sys_config`
VALUES (5, 'API参数加密是否开启', 'API.PARAM.ENCRYPT', 'false', 'bool', 115, '2017-03-17 23:17:23', 1, '2017-03-17 23:17:23',
        1);
INSERT INTO `sys_config`
VALUES (6, '腾讯云秘钥ID', 'CLOUD.SECRET.ID', 'xxx', 'txt', 10, '2017-04-05 22:59:24', 1, '2017-04-05 22:59:24', 1);
INSERT INTO `sys_config`
VALUES (7, '腾讯云秘钥KEY', 'CLOUD.SECRET.KEY', 'xxx', 'txt', 10, '2017-04-05 22:59:51', 1, '2017-04-05 22:59:51', 1);

-- ----------------------------
-- Table structure for t_api_log
-- ----------------------------
DROP TABLE IF EXISTS `t_api_log`;
CREATE TABLE `t_api_log`
(
    `id`          bigint(20)                                             NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `app_id`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT 'APPID',
    `user_id`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '用户ID',
    `method`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '调用方法',
    `version`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '版本号',
    `biz_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin         NULL COMMENT '业务参数',
    `ip`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT 'IP',
    `log_type`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '日志类型',
    `resp_msg`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '响应信息',
    `stack_trace` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin     NULL COMMENT '堆栈跟踪',
    `time_cost`   bigint(20)                                             NULL DEFAULT 0 COMMENT '耗时-毫秒',
    `create_time` datetime(0)                                            NULL DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = 'api调用记录'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_app_user
-- ----------------------------
DROP TABLE IF EXISTS `t_app_user`;
CREATE TABLE `t_app_user`
(
    `id`              bigint(20)                                              NOT NULL AUTO_INCREMENT,
    `account`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '账号',
    `phone`           varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NULL DEFAULT '' COMMENT '手机号',
    `password`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '登录密码',
    `dealpwd`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '交易密码',
    `user_type`       int(11)                                                 NULL DEFAULT NULL COMMENT '用户类型',
    `realname`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NULL DEFAULT '' COMMENT '真实姓名',
    `nickname`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NULL DEFAULT '' COMMENT '昵称',
    `avatar`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '头像',
    `gender`          varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NULL DEFAULT '' COMMENT '性别',
    `age`             int(11)                                                 NULL DEFAULT 0 COMMENT '年龄',
    `address`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '地址',
    `invite_code`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '邀请码',
    `invite_link`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT NULL COMMENT '邀请二维码连接',
    `pid`             bigint(20)                                              NULL DEFAULT NULL COMMENT '上级',
    `pids`            varchar(2550) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '所有上级ID线路，上级在左',
    `level`           int(11)                                                 NULL DEFAULT 0 COMMENT '用户等级',
    `status`          int(11)                                                 NULL DEFAULT 0 COMMENT '状态：0未激活 1正常 2暂停使用 3永久停号',
    `union_id`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NULL DEFAULT '' COMMENT '开放平台获取的unionid,解决这个同一个企业的不同APP和不同公众号之间的帐号共通',
    `access_ip`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NULL DEFAULT '' COMMENT '最后登录访问IP',
    `area_code`       varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin   NULL DEFAULT '' COMMENT '区域码',
    `last_login_time` datetime(0)                                             NULL DEFAULT NULL COMMENT '最后登录时间',
    `create_time`     datetime(0)                                             NULL DEFAULT CURRENT_TIMESTAMP(0),
    `update_time`     datetime(0)                                             NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT = '用户表'
  ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
