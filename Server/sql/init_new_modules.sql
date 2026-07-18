-- ============================================================================
-- 新增模块数据库初始化脚本
-- 包含：营销活动(subai21)、工单管理(cailei)、发票/费用/报销(waithooo)
-- 使用方法：mysql -u root -p mitedtsm_database < init_new_modules.sql
-- ============================================================================

-- ====================  1. 新建表结构  ====================

-- 1.1 营销活动表 (subai21)
CREATE TABLE IF NOT EXISTS `crm_campaign` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '活动名称',
  `status` int DEFAULT NULL COMMENT '活动状态(1草稿 2进行中 3已结束 4已取消)',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `budget` decimal(24,6) DEFAULT NULL COMMENT '预算金额',
  `actual_cost` decimal(24,6) DEFAULT NULL COMMENT '实际成本',
  `owner_user_id` bigint DEFAULT NULL COMMENT '负责人编号',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '活动描述',
  `channel` int DEFAULT NULL COMMENT '渠道',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CRM 营销活动';

-- 1.2 工单表 (cailei)
CREATE TABLE IF NOT EXISTS `crm_work_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '工单标题',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '工单类型',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '工单状态(待处理 处理中 已完结 已退回)',
  `priority` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '优先级(LOW NORMAL HIGH URGENT)',
  `customer_id` bigint DEFAULT NULL COMMENT '关联客户编号',
  `assignee_id` bigint DEFAULT NULL COMMENT '处理人编号',
  `creator_user_id` bigint DEFAULT NULL COMMENT '创建人编号',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '工单描述',
  `sla_deadline` datetime DEFAULT NULL COMMENT 'SLA截止时间',
  `is_sla_breached` bit(1) DEFAULT NULL COMMENT '是否超时',
  `satisfaction_score` int DEFAULT NULL COMMENT '满意度评分',
  `resolve_time` datetime DEFAULT NULL COMMENT '完结时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CRM 工单';

-- 1.3 工单处理记录表 (cailei)
CREATE TABLE IF NOT EXISTS `crm_work_order_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `work_order_id` bigint DEFAULT NULL COMMENT '关联工单编号',
  `from_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '变更前状态',
  `to_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '变更后状态',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CRM 工单处理记录';

-- 1.4 发票表 (waithooo)
CREATE TABLE IF NOT EXISTS `crm_invoice` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发票编号',
  `customer_id` bigint DEFAULT NULL COMMENT '客户编号',
  `contract_id` bigint DEFAULT NULL COMMENT '合同编号',
  `order_id` bigint DEFAULT NULL COMMENT '订单编号(预留)',
  `owner_user_id` bigint DEFAULT NULL COMMENT '负责人编号',
  `invoice_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发票号码',
  `type` int DEFAULT NULL COMMENT '发票类型',
  `amount` decimal(24,6) DEFAULT NULL COMMENT '开票金额',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发票抬头',
  `tax_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '税号',
  `status` int DEFAULT NULL COMMENT '发票状态',
  `issue_date` date DEFAULT NULL COMMENT '开票日期',
  `process_instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '工作流编号',
  `audit_status` int DEFAULT NULL COMMENT '审批状态',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CRM 发票';

-- 1.5 费用表 (waithooo)
CREATE TABLE IF NOT EXISTS `crm_expense` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '费用编号',
  `customer_id` bigint DEFAULT NULL COMMENT '客户编号',
  `contract_id` bigint DEFAULT NULL COMMENT '合同编号',
  `order_id` bigint DEFAULT NULL COMMENT '订单编号(预留)',
  `owner_user_id` bigint DEFAULT NULL COMMENT '负责人编号',
  `type` int DEFAULT NULL COMMENT '费用类型',
  `amount` decimal(24,6) DEFAULT NULL COMMENT '费用金额',
  `expense_date` date DEFAULT NULL COMMENT '费用日期',
  `audit_status` int DEFAULT NULL COMMENT '审批状态',
  `process_instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '工作流编号',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CRM 费用';

-- 1.6 报销表 (waithooo)
CREATE TABLE IF NOT EXISTS `crm_reimbursement` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '报销编号',
  `customer_id` bigint DEFAULT NULL COMMENT '客户编号',
  `contract_id` bigint DEFAULT NULL COMMENT '合同编号',
  `owner_user_id` bigint DEFAULT NULL COMMENT '负责人编号',
  `reimbursement_date` date DEFAULT NULL COMMENT '报销日期',
  `total_amount` decimal(24,6) DEFAULT NULL COMMENT '报销总金额',
  `type` int DEFAULT NULL COMMENT '报销类型',
  `status` int DEFAULT NULL COMMENT '报销状态(0=待提交 1=审批中 2=已通过 3=已驳回)',
  `process_instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '工作流编号',
  `audit_status` int DEFAULT NULL COMMENT '审批状态',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CRM 报销';

-- ====================  2. 菜单与权限  ====================

-- 2.1 插入菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
-- 营销活动 (subai21)
(5047, '营销活动', '', 2, 14, 2397, 'campaign', 'ep:flag', 'crm/campaign/index', 'CrmCampaign', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5048, '营销活动查询', 'crm:campaign:query', 3, 1, 5047, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5049, '营销活动创建', 'crm:campaign:create', 3, 2, 5047, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5050, '营销活动更新', 'crm:campaign:update', 3, 3, 5047, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5051, '营销活动删除', 'crm:campaign:delete', 3, 4, 5047, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5052, '营销活动导出', 'crm:campaign:export', 3, 5, 5047, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
-- 工单管理 (cailei)
(2925, '工单管理', '', 2, 70, 2397, 'workorder', 'ep:tickets', 'crm/workorder/index', 'CrmWorkOrder', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(2926, '工单查询', 'crm:work-order:query', 3, 1, 2925, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(2927, '工单创建', 'crm:work-order:create', 3, 2, 2925, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(2928, '工单更新', 'crm:work-order:update', 3, 3, 2925, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(2929, '工单删除', 'crm:work-order:delete', 3, 4, 2925, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
-- 发票管理 (waithooo) - 放在 CRM 菜单下
(5054, '发票管理', '', 2, 71, 2397, 'invoice', 'ep:document', 'crm/invoice/index', 'CrmInvoice', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5055, '发票查询', 'crm:invoice:query', 3, 1, 5054, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5056, '发票创建', 'crm:invoice:create', 3, 2, 5054, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5057, '发票更新', 'crm:invoice:update', 3, 3, 5054, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5058, '发票删除', 'crm:invoice:delete', 3, 4, 5054, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
-- 费用管理 (waithooo) - 放在 CRM 菜单下
(5059, '费用管理', '', 2, 72, 2397, 'expense', 'ep:credit-card', 'crm/expense/index', 'CrmExpense', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5060, '费用查询', 'crm:expense:query', 3, 1, 5059, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5061, '费用创建', 'crm:expense:create', 3, 2, 5059, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5062, '费用更新', 'crm:expense:update', 3, 3, 5059, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5063, '费用删除', 'crm:expense:delete', 3, 4, 5059, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
-- 报销管理 (waithooo) - 放在 CRM 菜单下
(5064, '报销管理', '', 2, 73, 2397, 'reimbursement', 'ep:receipt', 'crm/reimbursement/index', 'CrmReimbursement', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5065, '报销查询', 'crm:reimbursement:query', 3, 1, 5064, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5066, '报销创建', 'crm:reimbursement:create', 3, 2, 5064, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5067, '报销更新', 'crm:reimbursement:update', 3, 3, 5064, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(5068, '报销删除', 'crm:reimbursement:delete', 3, 4, 5064, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 2.2 授权给超级管理员(role_id=1)
INSERT IGNORE INTO `system_role_menu` (`role_id`, `menu_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(1, 5047, '1', NOW(), '1', NOW(), b'0'),
(1, 5048, '1', NOW(), '1', NOW(), b'0'),
(1, 5049, '1', NOW(), '1', NOW(), b'0'),
(1, 5050, '1', NOW(), '1', NOW(), b'0'),
(1, 5051, '1', NOW(), '1', NOW(), b'0'),
(1, 5052, '1', NOW(), '1', NOW(), b'0'),
(1, 2925, '1', NOW(), '1', NOW(), b'0'),
(1, 2926, '1', NOW(), '1', NOW(), b'0'),
(1, 2927, '1', NOW(), '1', NOW(), b'0'),
(1, 2928, '1', NOW(), '1', NOW(), b'0'),
(1, 2929, '1', NOW(), '1', NOW(), b'0'),
(1, 5054, '1', NOW(), '1', NOW(), b'0'),
(1, 5055, '1', NOW(), '1', NOW(), b'0'),
(1, 5056, '1', NOW(), '1', NOW(), b'0'),
(1, 5057, '1', NOW(), '1', NOW(), b'0'),
(1, 5058, '1', NOW(), '1', NOW(), b'0'),
(1, 5059, '1', NOW(), '1', NOW(), b'0'),
(1, 5060, '1', NOW(), '1', NOW(), b'0'),
(1, 5061, '1', NOW(), '1', NOW(), b'0'),
(1, 5062, '1', NOW(), '1', NOW(), b'0'),
(1, 5063, '1', NOW(), '1', NOW(), b'0'),
(1, 5064, '1', NOW(), '1', NOW(), b'0'),
(1, 5065, '1', NOW(), '1', NOW(), b'0'),
(1, 5066, '1', NOW(), '1', NOW(), b'0'),
(1, 5067, '1', NOW(), '1', NOW(), b'0'),
(1, 5068, '1', NOW(), '1', NOW(), b'0');

-- ====================  3. 字典数据  ====================

-- 3.1 字典类型
INSERT IGNORE INTO system_dict_type (id, name, type, status, remark, creator, create_time, updater, update_time, deleted) VALUES
(2012, 'CRM 发票类型', 'crm_invoice_type', 0, '发票类型枚举', '1', NOW(), '1', NOW(), b'0'),
(2013, 'CRM 发票状态', 'crm_invoice_status', 0, '发票状态枚举', '1', NOW(), '1', NOW(), b'0'),
(2014, 'CRM 费用类型', 'crm_expense_type', 0, '费用类型枚举', '1', NOW(), '1', NOW(), b'0'),
(2015, 'CRM 报销状态', 'crm_reimbursement_status', 0, '报销状态枚举', '1', NOW(), '1', NOW(), b'0'),
(2016, 'CRM 营销活动状态', 'crm_campaign_status', 0, '营销活动状态枚举', '1', NOW(), '1', NOW(), b'0'),
(2017, 'CRM 营销活动渠道', 'crm_campaign_channel', 0, '营销活动渠道枚举', '1', NOW(), '1', NOW(), b'0');

-- 3.2 字典数据 - 发票类型
INSERT IGNORE INTO system_dict_data (id, sort, label, value, dict_type, status, color_type, creator, create_time, updater, update_time, deleted) VALUES
(3054, 1, '增值税专用发票', '1', 'crm_invoice_type', 0, 'primary', '1', NOW(), '1', NOW(), b'0'),
(3055, 2, '增值税普通发票', '2', 'crm_invoice_type', 0, 'primary', '1', NOW(), '1', NOW(), b'0'),
(3056, 3, '电子发票', '3', 'crm_invoice_type', 0, 'primary', '1', NOW(), '1', NOW(), b'0');

-- 3.3 字典数据 - 发票状态
INSERT IGNORE INTO system_dict_data (id, sort, label, value, dict_type, status, color_type, creator, create_time, updater, update_time, deleted) VALUES
(3057, 1, '待开票', '1', 'crm_invoice_status', 0, 'warning', '1', NOW(), '1', NOW(), b'0'),
(3058, 2, '已开票', '2', 'crm_invoice_status', 0, 'success', '1', NOW(), '1', NOW(), b'0'),
(3059, 3, '已作废', '3', 'crm_invoice_status', 0, 'danger', '1', NOW(), '1', NOW(), b'0');

-- 3.4 字典数据 - 费用类型
INSERT IGNORE INTO system_dict_data (id, sort, label, value, dict_type, status, color_type, creator, create_time, updater, update_time, deleted) VALUES
(3060, 1, '差旅费', '1', 'crm_expense_type', 0, 'primary', '1', NOW(), '1', NOW(), b'0'),
(3061, 2, '办公费', '2', 'crm_expense_type', 0, 'primary', '1', NOW(), '1', NOW(), b'0'),
(3062, 3, '招待费', '3', 'crm_expense_type', 0, 'primary', '1', NOW(), '1', NOW(), b'0'),
(3063, 4, '通讯费', '4', 'crm_expense_type', 0, 'primary', '1', NOW(), '1', NOW(), b'0'),
(3064, 5, '培训费', '5', 'crm_expense_type', 0, 'primary', '1', NOW(), '1', NOW(), b'0');

-- 3.5 字典数据 - 报销状态
INSERT IGNORE INTO system_dict_data (id, sort, label, value, dict_type, status, color_type, creator, create_time, updater, update_time, deleted) VALUES
(3065, 1, '待提交', '0', 'crm_reimbursement_status', 0, 'info', '1', NOW(), '1', NOW(), b'0'),
(3066, 2, '审批中', '1', 'crm_reimbursement_status', 0, 'primary', '1', NOW(), '1', NOW(), b'0'),
(3067, 3, '已通过', '2', 'crm_reimbursement_status', 0, 'success', '1', NOW(), '1', NOW(), b'0'),
(3068, 4, '已驳回', '3', 'crm_reimbursement_status', 0, 'danger', '1', NOW(), '1', NOW(), b'0');

-- 3.6 字典数据 - 营销活动状态
INSERT IGNORE INTO system_dict_data (id, sort, label, value, dict_type, status, color_type, creator, create_time, updater, update_time, deleted) VALUES
(3069, 1, '草稿', '1', 'crm_campaign_status', 0, 'info', '1', NOW(), '1', NOW(), b'0'),
(3070, 2, '进行中', '2', 'crm_campaign_status', 0, 'primary', '1', NOW(), '1', NOW(), b'0'),
(3071, 3, '已结束', '3', 'crm_campaign_status', 0, 'success', '1', NOW(), '1', NOW(), b'0'),
(3072, 4, '已取消', '4', 'crm_campaign_status', 0, 'danger', '1', NOW(), '1', NOW(), b'0');

-- 3.7 字典数据 - 营销活动渠道
INSERT IGNORE INTO system_dict_data (id, sort, label, value, dict_type, status, color_type, creator, create_time, updater, update_time, deleted) VALUES
(3073, 1, '线下', '1', 'crm_campaign_channel', 0, 'primary', '1', NOW(), '1', NOW(), b'0'),
(3074, 2, '线上', '2', 'crm_campaign_channel', 0, 'primary', '1', NOW(), '1', NOW(), b'0');

-- 说明：发票/费用/报销放在 CRM 菜单(parent_id=2397)下
-- CRM 父菜单 id=2397 是系统原有菜单
-- 如果某菜单已存在会跳过(INSERT IGNORE)
