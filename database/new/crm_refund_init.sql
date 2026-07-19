-- 23软4胡伟-202305566535-修改于2026.07.17
-- [ADD START] 退款表与菜单初始化 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17

-- 创建退款表
CREATE TABLE IF NOT EXISTS `crm_refund` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
    `no` varchar(32) NOT NULL COMMENT '退款编号（自动生成）',
    `customer_id` bigint DEFAULT NULL COMMENT '客户编号',
    `contract_id` bigint DEFAULT NULL COMMENT '合同编号',
    `order_id` bigint DEFAULT NULL COMMENT '关联订单ID（预留）',
    `owner_user_id` bigint DEFAULT NULL COMMENT '负责人编号',
    `refund_amount` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '退款金额',
    `refund_date` date DEFAULT NULL COMMENT '退款日期',
    `refund_reason` varchar(512) DEFAULT NULL COMMENT '退款原因',
    `refund_type` int DEFAULT NULL COMMENT '退款类型',
    `process_instance_id` varchar(64) DEFAULT NULL COMMENT '工作流编号（BPM 关联）',
    `audit_status` int DEFAULT '0' COMMENT '审批状态',
    `status` int DEFAULT '0' COMMENT '退款状态（0=待提交 1=审批中 2=已通过 3=已驳回）',
    `remark` varchar(512) DEFAULT NULL COMMENT '备注',
    `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_no` (`no`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CRM 退款表';

-- 创建退款表序列（如果需要）
-- CREATE SEQUENCE IF NOT EXISTS `crm_refund_seq` START WITH 1 INCREMENT BY 1;

-- 注册系统菜单（中文）
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (6030, '退款管理', '', 2, 77, 2397, 'refund', 'ep:back', 'crm/refund/index', 'CrmRefund', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 注册退款管理菜单权限按钮
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(6031, '退款查询', 'crm:refund:query', 3, 1, 6030, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(6032, '退款创建', 'crm:refund:create', 3, 2, 6030, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(6033, '退款修改', 'crm:refund:update', 3, 3, 6030, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(6034, '退款删除', 'crm:refund:delete', 3, 4, 6030, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(6035, '退款导出', 'crm:refund:export', 3, 5, 6030, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(6036, '退款提交', 'crm:refund:submit', 3, 6, 6030, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 菜单国际化
INSERT INTO `system_menu_i18n` (`menu_id`, `language`, `name`) VALUES
(6030, 'zh-CN', '退款管理'),
(6030, 'en', 'Refund Management'),
(6030, 'ar', 'إدارة المبالغ المستردة');

-- [ADD START] 退款字典数据 - 2026-07-18 - 23软4胡伟-202305566535-修改于2026.07.17
-- 退款类型
INSERT IGNORE INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`) VALUES (2016, '退款-退款类型', 'crm_refund_type', 0, '退款-退款类型', '1', NOW(), '1', NOW(), b'0', '1970-01-01 00:00:00');
INSERT IGNORE INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (3068, 1, '全额退款', '1', 'crm_refund_type', 0, 'default', '', '', '1', NOW(), '1', NOW(), b'0');
INSERT IGNORE INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (3069, 2, '部分退款', '2', 'crm_refund_type', 0, 'default', '', '', '1', NOW(), '1', NOW(), b'0');
-- 退款状态
INSERT IGNORE INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`) VALUES (2017, '退款-退款状态', 'crm_refund_status', 0, '退款-退款状态', '1', NOW(), '1', NOW(), b'0', '1970-01-01 00:00:00');
INSERT IGNORE INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (3070, 1, '待提交', '0', 'crm_refund_status', 0, 'info', '', '', '1', NOW(), '1', NOW(), b'0');
INSERT IGNORE INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (3071, 2, '审批中', '1', 'crm_refund_status', 0, 'warning', '', '', '1', NOW(), '1', NOW(), b'0');
INSERT IGNORE INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (3072, 3, '已通过', '2', 'crm_refund_status', 0, 'success', '', '', '1', NOW(), '1', NOW(), b'0');
INSERT IGNORE INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES (3073, 4, '已驳回', '3', 'crm_refund_status', 0, 'danger', '', '', '1', NOW(), '1', NOW(), b'0');
-- [ADD END] 退款字典数据 - 2026-07-18 - 23软4胡伟-202305566535-修改于2026.07.17

-- [ADD END] 退款表与菜单初始化 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
