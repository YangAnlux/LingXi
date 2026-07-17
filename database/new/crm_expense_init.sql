-- 23软4胡伟-202305566535-修改于2026.07.16
-- [ADD START] 费用表 + 菜单注册 + 字典数据 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16

-- =============================================
-- 1. 创建费用表
-- =============================================
CREATE TABLE `crm_expense` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '费用编号（自动生成）',
  `customer_id` bigint NULL DEFAULT NULL COMMENT '客户ID',
  `contract_id` bigint NULL DEFAULT NULL COMMENT '合同ID',
  `order_id` bigint NULL DEFAULT NULL COMMENT '关联订单ID（预留）',
  `owner_user_id` bigint NULL DEFAULT NULL COMMENT '负责人编号',
  `type` tinyint NOT NULL COMMENT '费用类型（1=差旅 2=交通 3=餐饮 4=办公 5=其他）',
  `amount` decimal(24, 6) NOT NULL COMMENT '费用金额',
  `expense_date` date NULL DEFAULT NULL COMMENT '费用日期',
  `audit_status` tinyint NULL DEFAULT NULL COMMENT '审批状态（预留BPM，仅记录状态）',
  `process_instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '工作流编号（预留BPM）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_customer_id` (`customer_id`) USING BTREE,
  KEY `idx_contract_id` (`contract_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'CRM 费用表';

-- =============================================
-- 2. 菜单注册（父菜单：CRM 2397）
-- =============================================
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (6010, '费用管理', '', 2, 75, 2397, 'expense', 'ep:money', 'crm/expense/index', 'CrmExpense', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (6011, '费用管理查询', 'crm:expense:query', 3, 1, 6010, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (6012, '费用管理创建', 'crm:expense:create', 3, 2, 6010, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (6013, '费用管理更新', 'crm:expense:update', 3, 3, 6010, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (6014, '费用管理删除', 'crm:expense:delete', 3, 4, 6010, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (6015, '费用管理导出', 'crm:expense:export', 3, 5, 6010, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 菜单国际化
INSERT INTO `system_menu_i18n` (`menu_id`, `language`, `name`) VALUES (6010, 'zh-CN', '费用管理');
INSERT INTO `system_menu_i18n` (`menu_id`, `language`, `name`) VALUES (6010, 'en', 'Expense Management');

-- =============================================
-- 3. 字典类型与数据（费用类型）
-- =============================================
INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`) VALUES (2014, '费用-费用类型', 'crm_expense_type', 0, '费用-费用类型', '1', NOW(), '1', NOW(), b'0', '1970-01-01 00:00:00');

INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (3059, 1, '差旅', '1', 'crm_expense_type', 0, 'default', '', '', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (3060, 2, '交通', '2', 'crm_expense_type', 0, 'default', '', '', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (3061, 3, '餐饮', '3', 'crm_expense_type', 0, 'default', '', '', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (3062, 4, '办公', '4', 'crm_expense_type', 0, 'default', '', '', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (3063, 5, '其他', '5', 'crm_expense_type', 0, 'default', '', '', '1', NOW(), '1', NOW(), b'0');

-- [ADD END] 费用表 + 菜单注册 + 字典数据 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
