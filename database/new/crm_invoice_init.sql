-- 23软4胡伟-202305566535-修改于2026.07.14
-- [ADD START] 发票表 + 菜单注册 + 字典数据 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14

-- =============================================
-- 1. 创建发票表
-- =============================================
CREATE TABLE `crm_invoice` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '发票编号（自动生成）',
  `customer_id` bigint NULL DEFAULT NULL COMMENT '客户ID',
  `contract_id` bigint NULL DEFAULT NULL COMMENT '合同ID',
  `order_id` bigint NULL DEFAULT NULL COMMENT '关联订单ID（预留）',
  `owner_user_id` bigint NULL DEFAULT NULL COMMENT '负责人编号',
  `invoice_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '发票号码',
  `type` tinyint NOT NULL COMMENT '发票类型（1=增值税普通发票 2=增值税专用发票）',
  `amount` decimal(24, 6) NOT NULL COMMENT '开票金额',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '发票抬头',
  `tax_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '税号',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '发票状态（0=待开票 1=已开票 2=已作废）',
  `issue_date` date NULL DEFAULT NULL COMMENT '开票日期',
  `process_instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '工作流编号（预留BPM）',
  `audit_status` tinyint NULL DEFAULT NULL COMMENT '审批状态（预留BPM）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_invoice_no` (`invoice_no`) USING BTREE,
  KEY `idx_customer_id` (`customer_id`) USING BTREE,
  KEY `idx_contract_id` (`contract_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'CRM 发票表';

-- =============================================
-- 2. 菜单注册（父菜单：CRM 2397）
-- =============================================
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (6004, '发票管理', '', 2, 70, 2397, 'invoice', 'ep:document', 'crm/invoice/index', 'CrmInvoice', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (6005, '发票管理查询', 'crm:invoice:query', 3, 1, 6004, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (6006, '发票管理创建', 'crm:invoice:create', 3, 2, 6004, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (6007, '发票管理更新', 'crm:invoice:update', 3, 3, 6004, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (6008, '发票管理删除', 'crm:invoice:delete', 3, 4, 6004, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (6009, '发票管理导出', 'crm:invoice:export', 3, 5, 6004, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 菜单国际化
INSERT INTO `system_menu_i18n` (`menu_id`, `language`, `name`) VALUES (6004, 'zh-CN', '发票管理');
INSERT INTO `system_menu_i18n` (`menu_id`, `language`, `name`) VALUES (6004, 'en', 'Invoice Management');
INSERT INTO `system_menu_i18n` (`menu_id`, `language`, `name`) VALUES (6004, 'ar', 'إدارة الفواتير');

-- =============================================
-- 3. 字典类型与数据（发票类型 / 发票状态）
-- =============================================
INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`) VALUES (2012, '发票-发票类型', 'crm_invoice_type', 0, '发票-发票类型', '1', NOW(), '1', NOW(), b'0', '1970-01-01 00:00:00');
INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`) VALUES (2013, '发票-发票状态', 'crm_invoice_status', 0, '发票-发票状态', '1', NOW(), '1', NOW(), b'0', '1970-01-01 00:00:00');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (3054, 1, '增值税普通发票', '1', 'crm_invoice_type', 0, 'default', '', '', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (3055, 2, '增值税专用发票', '2', 'crm_invoice_type', 0, 'default', '', '', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (3056, 1, '待开票', '0', 'crm_invoice_status', 0, 'warning', '', '', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (3057, 2, '已开票', '1', 'crm_invoice_status', 0, 'success', '', '', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (3058, 3, '已作废', '2', 'crm_invoice_status', 0, 'danger', '', '', '1', NOW(), '1', NOW(), b'0');

-- [ADD END] 发票表 + 菜单注册 + 字典数据 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
