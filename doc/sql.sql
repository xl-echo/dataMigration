CREATE TABLE `transfer_data_task` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `task_id` varchar(45) DEFAULT NULL COMMENT '任务主键',
  `transfer_mode` char(1) DEFAULT '0' COMMENT '0: 库到库; 1: 库到文件到库（稍慢）',
  `status` char(1) DEFAULT '1' COMMENT '0: 无效; 1: 有效',
  `create_uname` varchar(45) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_uname` varchar(45) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3424 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT

CREATE TABLE `transfer_database_config` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `task_id` varchar(45) DEFAULT NULL COMMENT '任务主键',
  `database_direction` varchar(45) DEFAULT NULL COMMENT '数据库方向: source target',
  `database_url` varchar(1000) DEFAULT NULL COMMENT '数据库地址',
  `database_username` varchar(225) DEFAULT NULL COMMENT '用户名',
  `database_password` varchar(225) DEFAULT NULL COMMENT '密码',
  `status` bigint(1) DEFAULT '1' COMMENT '0: 无效; 1: 有效',
  `create_uname` varchar(45) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_uname` varchar(45) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT

CREATE TABLE `transfer_insert_config` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `task_id` varchar(45) DEFAULT NULL COMMENT '任务主键',
  `target_config` varchar(45) DEFAULT NULL COMMENT '目标库配置ID',
  `table_name` varchar(100) DEFAULT NULL COMMENT '目标表名',
  `table_cloumn_name` varchar(1000) DEFAULT NULL COMMENT '目标表字段名cloumn1,cloumn2...',
  `limit_size` bigint(5) DEFAULT '1000' COMMENT '目标表数据插入限制，默认一次插入1000条 ',
  `status` bigint(1) DEFAULT '1' COMMENT '0: 无效; 1: 有效',
  `create_uname` varchar(45) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_uname` varchar(45) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT

CREATE TABLE `transfer_log` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `task_id` varchar(45) DEFAULT NULL COMMENT '任务主键',
  `trace_id` varchar(36) DEFAULT NULL,
  `task_begin_date` datetime DEFAULT NULL COMMENT '定时任务开始时间',
  `task_end_date` datetime DEFAULT NULL COMMENT '定时任务结束时间',
  `task_transfer_data_size` bigint(11) DEFAULT NULL COMMENT '定时任务迁移数据条数',
  `task_batch_num` int(5) DEFAULT '0' COMMENT '分成多少批',
  `task_exec_batch_num` int(5) DEFAULT '0' COMMENT '执行了多少批',
  `task_file_num` int(5) DEFAULT '0' COMMENT '文件个数',
  `task_write_file_num` int(5) DEFAULT '0' COMMENT '写了多少个文件',
  `status` char(1) DEFAULT '2' COMMENT '执行状态: 0: 失败; 1: 成功, 2: 处理中',
  `create_uname` varchar(45) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_uname` varchar(45) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `trace_id` (`trace_id`)
) ENGINE=InnoDB AUTO_INCREMENT=177 DEFAULT CHARSET=utf8

CREATE TABLE `transfer_select_config` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `task_id` varchar(45) DEFAULT NULL COMMENT '任务主键',
  `source_config` varchar(45) DEFAULT NULL COMMENT '源库配置ID',
  `table_name` varchar(100) DEFAULT NULL COMMENT '源表名',
  `table_cloumn_name` varchar(1000) DEFAULT NULL COMMENT '源表字段名cloumn1,cloumn2...',
  `table_where` varchar(1000) DEFAULT NULL COMMENT '源表数据查询条件: cloumn1=1 and cloumn2=2...',
  `limit_size` bigint(5) DEFAULT '300' COMMENT '源表数据查询条件: cloumn1=1 and cloumn2=2...',
  `status` bigint(1) DEFAULT '1' COMMENT '0: 无效; 1: 有效',
  `create_uname` varchar(45) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_uname` varchar(45) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT

INSERT INTO community.transfer_data_task (id,task_id,transfer_mode,status,create_uname,create_time,update_uname,update_time) VALUES (1,'blog_transfer_data','1','1','echo',now(),'echo',now());

INSERT INTO community.transfer_database_config (id,task_id,database_direction,database_url,database_username,database_password,status,create_uname,create_time,update_uname,update_time) VALUES
(1,'blog_transfer_data','target','jdbc:mysql://127.0.0.1:3306/blogbak','echo','123456',1,'echo',now(),'echo',now())
,(2,'blog_transfer_data','source','jdbc:mysql://127.0.0.1:3306/blog','echo','123456',1,'echo',now(),'echo',now())
;

INSERT INTO community.transfer_insert_config (id,task_id,target_config,table_name,table_cloumn_name,limit_size,status,create_uname,create_time,update_uname,update_time) VALUES
(1,'blog_transfer_data','blog_bak_config','books_bak','id, book_name, book_img_link, clounnm01, clounnm02, clounnm03, clounnm04, clounnm05, clounnm06, clounnm07, clounnm08, clounnm09, clounnm10, clounnm11, clounnm12, clounnm13, clounnm14, clounnm15, clounnm16, clounnm17, clounnm18, clounnm19, clounnm20, clounnm21, clounnm22, clounnm23, clounnm24, clounnm25, clounnm26, clounnm27, clounnm28, clounnm29, clounnm30, description, create_time, update_time',600,1,'echo',now(),'echo',now())
;

INSERT INTO community.transfer_select_config (id,task_id,source_config,table_name,table_cloumn_name,table_where,limit_size,status,create_uname,create_time,update_uname,update_time) VALUES
(1,'blog_transfer_data','blog_config','books','id, book_name, book_img_link, clounnm01, clounnm02, clounnm03, clounnm04, clounnm05, clounnm06, clounnm07, clounnm08, clounnm09, clounnm10, clounnm11, clounnm12, clounnm13, clounnm14, clounnm15, clounnm16, clounnm17, clounnm18, clounnm19, clounnm20, clounnm21, clounnm22, clounnm23, clounnm24, clounnm25, clounnm26, clounnm27, clounnm28, clounnm29, clounnm30, description, create_time, update_time','id > 3000000',5000,1,'echo',STR_TO_DATE('2022-11-10 19:00:00','%Y-%m-%d %H:%i:%s'),'echo',STR_TO_DATE('2022-11-10 19:00:00','%Y-%m-%d %H:%i:%s'))
;