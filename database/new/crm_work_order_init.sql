-- 23软件工程4班蔡磊202305566515
-- 工单管理模块初始化脚本
-- 建表 + 菜单注册 + 权限分配

-- =============================================
-- 1. 创建工单表
-- =============================================
DROP TABLE IF EXISTS `crm_work_order`;
CREATE TABLE `crm_work_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '工单标题',
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '工单类型（字典）',
  `priority` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'NORMAL' COMMENT '优先级（LOW/NORMAL/HIGH/URGENT）',
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '状态（待处理/处理中/已完结/已退回）',
  `customer_id` bigint DEFAULT NULL COMMENT '关联客户编号',
  `assignee_id` bigint DEFAULT NULL COMMENT '处理人编号',
  `sla_deadline` datetime DEFAULT NULL COMMENT 'SLA 截止时间',
  `is_sla_breached` tinyint DEFAULT '0' COMMENT '是否超SLA（0=否 1=是）',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '工单内容',
  `solution` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '解决方案',
  `resolved_time` datetime DEFAULT NULL COMMENT '完结时间',
  `satisfaction_score` tinyint DEFAULT NULL COMMENT '满意度评分（1-5）',
  `satisfaction_comment` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '满意度评价内容',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_customer_id` (`customer_id`) USING BTREE,
  KEY `idx_assignee_id` (`assignee_id`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CRM 工单表';

-- =============================================
-- 2. 创建工单处理记录表
-- =============================================
DROP TABLE IF EXISTS `crm_work_order_record`;
CREATE TABLE `crm_work_order_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `work_order_id` bigint NOT NULL COMMENT '关联工单编号',
  `from_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '变更前状态',
  `to_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '变更后状态',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_work_order_id` (`work_order_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CRM 工单处理记录表';

-- =============================================
-- 3. 菜单注册（父菜单：CRM 2397，排序在回款计划61和发票管理70之间）
-- =============================================
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(6100, '工单管理', '', 2, 62, 2397, 'workorder', 'ep:tickets', 'crm/workorder/index', 'CrmWorkOrder', 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`) VALUES
(6101, '工单管理查询', 'crm:work-order:query', 3, 1, 6100, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(6102, '工单管理创建', 'crm:work-order:create', 3, 2, 6100, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(6103, '工单管理更新', 'crm:work-order:update', 3, 3, 6100, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(6104, '工单管理删除', 'crm:work-order:delete', 3, 4, 6100, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(6105, '工单管理分配', 'crm:work-order:assign', 3, 5, 6100, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0'),
(6106, '工单管理导出', 'crm:work-order:export', 3, 6, 6100, '', '', '', NULL, 0, b'1', b'1', b'1', '1', NOW(), '1', NOW(), b'0');

-- 菜单国际化
INSERT IGNORE INTO `system_menu_i18n` (`menu_id`, `language`, `name`) VALUES
(6100, 'zh-CN', '工单管理'),
(6100, 'en', 'Work Order');

-- =============================================
-- 4. 为超级管理员角色（role_id=1）分配权限
-- =============================================
INSERT IGNORE INTO `system_role_menu` (`role_id`, `menu_id`) VALUES
(1, 6100), (1, 6101), (1, 6102), (1, 6103), (1, 6104), (1, 6105), (1, 6106);
