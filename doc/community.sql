/*
 Navicat Premium Data Transfer

 Source Server         : tencetMySQL
 Source Server Type    : MySQL
 Source Server Version : 50718
 Source Host           : gz-cynosdbmysql-grp-klzx8wmp.sql.tencentcdb.com:27240
 Source Schema         : community

 Target Server Type    : MySQL
 Target Server Version : 50718
 File Encoding         : 65001

 Date: 23/12/2022 09:51:35
*/

CREATE DATABASE community;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for transfer_data_task
-- ----------------------------
DROP TABLE IF EXISTS `transfer_data_task`;
CREATE TABLE `transfer_data_task`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `task_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务主键',
  `transfer_mode` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '0: 库到文件到库（稍慢）; 1: 库到库',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '0: 无效; 1: 有效',
  `create_uname` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_uname` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of transfer_data_task
-- ----------------------------
INSERT INTO `transfer_data_task` VALUES (1, 'blog_transfer_data', '1', '1', 'echo', '2022-11-10 20:00:00', 'echo', '2022-11-10 20:00:00');

-- ----------------------------
-- Table structure for transfer_database_config
-- ----------------------------
DROP TABLE IF EXISTS `transfer_database_config`;
CREATE TABLE `transfer_database_config`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `task_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务主键',
  `database_direction` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库方向: source target',
  `database_url` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库地址',
  `database_username` varchar(225) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `database_password` varchar(225) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `status` bigint(1) NULL DEFAULT 1 COMMENT '0: 无效; 1: 有效',
  `create_uname` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_uname` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of transfer_database_config
-- ----------------------------
INSERT INTO `transfer_database_config` VALUES (1, 'blog_transfer_data', 'target', 'jdbc:mysql://gz-cynosdbmysql-grp-klzx8wmp.sql.tencentcdb.com:27240/blogbak?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=UTC&allowMultiQueries=true', 'echo', 'QWer1234', 1, 'echo', '2022-11-10 19:00:00', 'echo', '2022-11-10 19:00:00');
INSERT INTO `transfer_database_config` VALUES (2, 'blog_transfer_data', 'source', 'jdbc:mysql://gz-cynosdbmysql-grp-klzx8wmp.sql.tencentcdb.com:27240/blog?useUnicode=true&characterEncoding=utf-8&useSSL=true&serverTimezone=UTC&allowMultiQueries=true', 'echo', 'QWer1234', 1, 'echo', '2022-11-10 19:00:00', 'echo', '2022-11-10 19:00:00');

-- ----------------------------
-- Table structure for transfer_insert_config
-- ----------------------------
DROP TABLE IF EXISTS `transfer_insert_config`;
CREATE TABLE `transfer_insert_config`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `task_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务主键',
  `target_config` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目标库配置ID',
  `table_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目标表名',
  `table_cloumn_name` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '目标表字段名cloumn1,cloumn2...',
  `limit_size` bigint(5) NULL DEFAULT 1000 COMMENT '目标表数据插入限制，默认一次插入1000条 ',
  `status` bigint(1) NULL DEFAULT 1 COMMENT '0: 无效; 1: 有效',
  `create_uname` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_uname` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of transfer_insert_config
-- ----------------------------
INSERT INTO `transfer_insert_config` VALUES (1, 'blog_transfer_data', 'blog_bak_config', 'books_bak', 'id, book_name, book_img_link, clounnm01, clounnm02, clounnm03, clounnm04, clounnm05, clounnm06, clounnm07, clounnm08, clounnm09, clounnm10, clounnm11, clounnm12, clounnm13, clounnm14, clounnm15, clounnm16, clounnm17, clounnm18, clounnm19, clounnm20, clounnm21, clounnm22, clounnm23, clounnm24, clounnm25, clounnm26, clounnm27, clounnm28, clounnm29, clounnm30, description, create_time, update_time', 2000, 1, 'echo', '2022-11-10 19:00:00', 'echo', '2022-11-10 19:00:00');

-- ----------------------------
-- Table structure for transfer_log
-- ----------------------------
DROP TABLE IF EXISTS `transfer_log`;
CREATE TABLE `transfer_log`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `task_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务主键',
  `trace_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `task_begin_date` datetime NULL DEFAULT NULL COMMENT '定时任务开始时间',
  `task_end_date` datetime NULL DEFAULT NULL COMMENT '定时任务结束时间',
  `task_transfer_data_size` bigint(11) NULL DEFAULT NULL COMMENT '定时任务迁移数据条数',
  `task_batch_num` int(5) NULL DEFAULT 0 COMMENT '分成多少批',
  `task_exec_batch_num` int(5) NULL DEFAULT 0 COMMENT '执行了多少批',
  `task_file_num` int(5) NULL DEFAULT 0 COMMENT '文件个数',
  `task_write_file_num` int(5) NULL DEFAULT 0 COMMENT '写了多少个文件',
  `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '2' COMMENT '执行状态: 0: 失败; 1: 成功, 2: 处理中',
  `create_uname` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_uname` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `trace_id`(`trace_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of transfer_log
-- ----------------------------
INSERT INTO `transfer_log` VALUES (1, 'blog_transfer_data', '32fe7de7-9394-4d0f-ad52-729aaa32512c', '2022-12-20 16:29:01', '2022-12-20 16:29:02', 0, 0, 0, 0, 0, '1', 'sys', '2022-12-20 16:29:01', 'sys', '2022-12-20 16:29:02');
INSERT INTO `transfer_log` VALUES (2, 'blog_transfer_data', 'c6ca0289-a16c-4148-868e-7ae28638abf6', '2022-12-21 00:00:01', '2022-12-21 01:00:52', 757659, 0, 0, 0, 0, '1', 'sys', '2022-12-21 00:00:01', 'sys', '2022-12-21 01:00:52');
INSERT INTO `transfer_log` VALUES (3, 'blog_transfer_data', 'dd702860-e1cc-402c-8e14-d302561645fb', '2022-12-22 00:00:01', '2022-12-22 01:30:51', 1014681, 0, 0, 0, 0, '1', 'sys', '2022-12-22 00:00:01', 'sys', '2022-12-22 01:30:51');
INSERT INTO `transfer_log` VALUES (4, 'blog_transfer_data', '87b5dc97-588a-4808-9cb6-6984454b9c94', '2022-12-23 00:00:01', '2022-12-23 01:47:51', 1188587, 0, 0, 0, 0, '1', 'sys', '2022-12-23 00:00:01', 'sys', '2022-12-23 01:47:51');

-- ----------------------------
-- Table structure for transfer_select_config
-- ----------------------------
DROP TABLE IF EXISTS `transfer_select_config`;
CREATE TABLE `transfer_select_config`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `task_id` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务主键',
  `source_config` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '源库配置ID',
  `table_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '源表名',
  `table_cloumn_name` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '源表字段名cloumn1,cloumn2...',
  `table_where` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '源表数据查询条件: cloumn1=1 and cloumn2=2...',
  `limit_size` bigint(5) NULL DEFAULT 300 COMMENT '源表数据查询条件: cloumn1=1 and cloumn2=2...',
  `status` bigint(1) NULL DEFAULT 1 COMMENT '0: 无效; 1: 有效',
  `create_uname` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_uname` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '修改人',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of transfer_select_config
-- ----------------------------
INSERT INTO `transfer_select_config` VALUES (1, 'blog_transfer_data', 'blog_config', 'books', 'id, book_name, book_img_link, clounnm01, clounnm02, clounnm03, clounnm04, clounnm05, clounnm06, clounnm07, clounnm08, clounnm09, clounnm10, clounnm11, clounnm12, clounnm13, clounnm14, clounnm15, clounnm16, clounnm17, clounnm18, clounnm19, clounnm20, clounnm21, clounnm22, clounnm23, clounnm24, clounnm25, clounnm26, clounnm27, clounnm28, clounnm29, clounnm30, description, create_time, update_time', 'id > 1', 5000, 1, 'echo', '2022-11-10 18:00:00', 'echo', '2022-11-10 18:00:00');

SET FOREIGN_KEY_CHECKS = 1;
