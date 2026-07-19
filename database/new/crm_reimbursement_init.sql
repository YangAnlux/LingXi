-- 23软4胡伟-202305566535-修改于2026.07.16
-- [ADD START] 报销表 + 菜单注册 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16

-- =============================================
-- 1. 创建报销表
-- =============================================
CREATE TABLE `crm_reimbursement` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '报销编号（自动生成）',
  `customer_id` bigint NULL DEFAULT NULL COMMENT '客户ID',
  `contract_id` bigint NULL DEFAULT NULL COMMENT '合同ID',
  `owner_user_id` bigint NULL DEFAULT NULL COMMENT '负责人编号',
  `reimbursement_date` date NULL DEFAULT NULL COMMENT '报销日期',
  `total_amount` decimal(24, 6) NULL DEFAULT NULL COMMENT '报销总金额',
  `type` tinyint NULL DEFAULT NULL COMMENT '报销类型（1=差旅 2=交通 3=餐饮 4=办公 5=其他）',
  `status` tinyint NULL DEFAULT NULL COMMENT '状态（0=待提交 1=审批中 2=已通过 3=已驳回）',
  `process_instance_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '工作流编号',
  `audit_status` tinyint NULL DEFAULT NULL COMMENT '审批状态',
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
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'CRM 报销表';

-- =============================================
-- 2. 菜单注册（父菜单：CRM 2397）
-- =============================================
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (6020, '报销管理', '', 2, 76, 2397, 'reimbursement', 'ep:wallet', 'crm/reimbursement/index', 'CrmReimbursement', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (6021, '报销管理查询', 'crm:reimbursement:query', 3, 1, 6020, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (6022, '报销管理创建', 'crm:reimbursement:create', 3, 2, 6020, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (6023, '报销管理更新', 'crm:reimbursement:update', 3, 3, 6020, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (6024, '报销管理删除', 'crm:reimbursement:delete', 3, 4, 6020, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (6025, '报销管理导出', 'crm:reimbursement:export', 3, 5, 6020, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (6026, '报销管理提交', 'crm:reimbursement:submit', 3, 6, 6020, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

INSERT INTO `system_menu_i18n` (`menu_id`, `language`, `name`) VALUES (6020, 'zh-CN', '报销管理');
INSERT INTO `system_menu_i18n` (`menu_id`, `language`, `name`) VALUES (6020, 'en', 'Reimbursement');

-- [ADD START] 报销状态字典数据 - 2026-07-18 - 23软4胡伟-202305566535-修改于2026.07.17
INSERT IGNORE INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`) VALUES (2015, 'CRM 报销状态', 'crm_reimbursement_status', 0, '报销-报销状态', '1', NOW(), '1', NOW(), b'0', '1970-01-01 00:00:00');
INSERT IGNORE INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (3064, 1, '待提交', '0', 'crm_reimbursement_status', 0, 'info', '', NULL, '1', NOW(), '1', NOW(), b'0');
INSERT IGNORE INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (3065, 2, '审批中', '1', 'crm_reimbursement_status', 0, 'warning', '', NULL, '1', NOW(), '1', NOW(), b'0');
INSERT IGNORE INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (3066, 3, '已通过', '2', 'crm_reimbursement_status', 0, 'success', '', NULL, '1', NOW(), '1', NOW(), b'0');
INSERT IGNORE INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (3067, 4, '已驳回', '3', 'crm_reimbursement_status', 0, 'danger', '', NULL, '1', NOW(), '1', NOW(), b'0');
-- [ADD END] 报销状态字典数据 - 2026-07-18 - 23软4胡伟-202305566535-修改于2026.07.17

-- [ADD END] 报销表 + 菜单注册 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
