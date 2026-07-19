CREATE TABLE `crm_business` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(255) NOT NULL COMMENT '商机名称',
  `customer_id` bigint NULL COMMENT '客户编号',
  `follow_up_status` tinyint NULL COMMENT '跟进状态',
  `contact_last_time` datetime NULL COMMENT '最后跟进时间',
  `contact_next_time` datetime NULL COMMENT '下次联系时间',
  `owner_user_id` bigint NULL COMMENT '负责人的用户编号',
  `status_type_id` bigint NULL COMMENT '商机状态组编号',
  `status_id` bigint NULL COMMENT '商机状态编号',
  `end_status` tinyint NULL COMMENT '结束状态',
  `end_remark` varchar(500) NULL COMMENT '结束时的备注',
  `lose_reason` varchar(500) NULL COMMENT '流失原因',
  `deal_time` datetime NULL COMMENT '预计成交日期',
  `total_product_price` decimal(18,4) NULL COMMENT '产品总金额',
  `discount_percent` decimal(5,2) NULL COMMENT '整单折扣',
  `total_price` decimal(18,4) NULL COMMENT '商机总金额',
  `remark` varchar(500) NULL COMMENT '备注',
  `competitor` varchar(255) NULL COMMENT '竞争对手',
  `creator` varchar(64) NULL COMMENT '创建者',
  `create_time` datetime NULL COMMENT '创建时间',
  `updater` varchar(64) NULL COMMENT '更新者',
  `update_time` datetime NULL COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_owner_user_id` (`owner_user_id`),
  KEY `idx_status_type_id` (`status_type_id`),
  KEY `idx_status_id` (`status_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='CRM商机表';

CREATE TABLE `crm_business_status_type` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) NOT NULL COMMENT '状态类型名',
  `dept_ids` varchar(500) NULL COMMENT '使用的部门编号',
  `creator` varchar(64) NULL COMMENT '创建者',
  `create_time` datetime NULL COMMENT '创建时间',
  `updater` varchar(64) NULL COMMENT '更新者',
  `update_time` datetime NULL COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='CRM商机状态组表';

CREATE TABLE `crm_business_status` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type_id` bigint NOT NULL COMMENT '状态类型编号',
  `name` varchar(64) NOT NULL COMMENT '状态名',
  `percent` int NULL COMMENT '赢单率',
  `sort` int NULL COMMENT '排序',
  `creator` varchar(64) NULL COMMENT '创建者',
  `create_time` datetime NULL COMMENT '创建时间',
  `updater` varchar(64) NULL COMMENT '更新者',
  `update_time` datetime NULL COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `idx_type_id` (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='CRM商机状态表';

CREATE TABLE `crm_business_product` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `business_id` bigint NOT NULL COMMENT '商机编号',
  `product_id` bigint NULL COMMENT '产品编号',
  `product_price` decimal(18,4) NULL COMMENT '产品单价',
  `business_price` decimal(18,4) NULL COMMENT '商机价格',
  `count` decimal(10,4) NULL COMMENT '数量',
  `total_price` decimal(18,4) NULL COMMENT '总计价格',
  `creator` varchar(64) NULL COMMENT '创建者',
  `create_time` datetime NULL COMMENT '创建时间',
  `updater` varchar(64) NULL COMMENT '更新者',
  `update_time` datetime NULL COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `idx_business_id` (`business_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='CRM商机产品关联表';

INSERT INTO `crm_business_status_type` (`id`, `name`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1, '标准销售流程', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `crm_business_status` (`id`, `type_id`, `name`, `percent`, `sort`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (1, 1, '初步接触', 10, 1, '1', NOW(), '1', NOW(), b'0');
INSERT INTO `crm_business_status` (`id`, `type_id`, `name`, `percent`, `sort`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (2, 1, '需求确认', 25, 2, '1', NOW(), '1', NOW(), b'0');
INSERT INTO `crm_business_status` (`id`, `type_id`, `name`, `percent`, `sort`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (3, 1, '方案制定', 40, 3, '1', NOW(), '1', NOW(), b'0');
INSERT INTO `crm_business_status` (`id`, `type_id`, `name`, `percent`, `sort`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (4, 1, '商务谈判', 60, 4, '1', NOW(), '1', NOW(), b'0');
INSERT INTO `crm_business_status` (`id`, `type_id`, `name`, `percent`, `sort`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (5, 1, '合同签订', 80, 5, '1', NOW(), '1', NOW(), b'0');
INSERT INTO `crm_business_status` (`id`, `type_id`, `name`, `percent`, `sort`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (6, 1, '成功交付', 100, 6, '1', NOW(), '1', NOW(), b'0');