-- =============================================
-- CRM 营销活动群发 & 客户关怀 相关表
-- =============================================

-- 1. crm_customer 增加生日字段（如已存在则跳过）
ALTER TABLE `crm_customer` ADD COLUMN IF NOT EXISTS `birthday` DATE NULL COMMENT '生日' AFTER `email`;

-- 2. 群发发送日志表
CREATE TABLE IF NOT EXISTS `crm_campaign_send_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '编号',
    `campaign_id` BIGINT NOT NULL COMMENT '营销活动编号',
    `channel` INT NOT NULL COMMENT '发送渠道 1-短信 2-邮件',
    `template_code` VARCHAR(100) NOT NULL COMMENT '模板编码',
    `total_count` INT NOT NULL DEFAULT 0 COMMENT '总发送数',
    `success_count` INT NOT NULL DEFAULT 0 COMMENT '成功数',
    `fail_count` INT NOT NULL DEFAULT 0 COMMENT '失败数',
    `open_count` INT NOT NULL DEFAULT 0 COMMENT '邮件打开次数（邮件跟踪回写）',
    `click_count` INT NOT NULL DEFAULT 0 COMMENT '邮件点击次数（邮件跟踪回写）',
    `send_time` DATETIME NOT NULL COMMENT '发送时间',
    `remark` VARCHAR(500) NULL COMMENT '备注',
    `tenant_id` BIGINT NOT NULL DEFAULT 0 COMMENT '租户编号',
    `creator` VARCHAR(64) NULL DEFAULT '' COMMENT '创建人',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) NULL DEFAULT '' COMMENT '更新人',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    INDEX `idx_campaign_id` (`campaign_id`),
    INDEX `idx_channel` (`channel`),
    INDEX `idx_send_time` (`send_time`),
    INDEX `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CRM 群发发送日志表';

-- 3. 客户关怀发送日志表
CREATE TABLE IF NOT EXISTS `crm_care_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '编号',
    `customer_id` BIGINT NULL COMMENT '客户编号',
    `campaign_id` BIGINT NULL COMMENT '关联营销活动编号',
    `care_type` INT NOT NULL COMMENT '关怀类型 1-生日关怀 2-节日关怀 3-手动关怀',
    `channel` INT NOT NULL COMMENT '发送渠道 1-短信 2-邮件',
    `template_code` VARCHAR(100) NOT NULL COMMENT '模板编码',
    `content` VARCHAR(1000) NULL COMMENT '发送内容',
    `status` INT NOT NULL DEFAULT 0 COMMENT '状态 0-成功 1-失败',
    `send_time` DATETIME NOT NULL COMMENT '发送时间',
    `remark` VARCHAR(500) NULL COMMENT '备注',
    `tenant_id` BIGINT NOT NULL DEFAULT 0 COMMENT '租户编号',
    `creator` VARCHAR(64) NULL DEFAULT '' COMMENT '创建人',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) NULL DEFAULT '' COMMENT '更新人',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    INDEX `idx_customer_id` (`customer_id`),
    INDEX `idx_care_type` (`care_type`),
    INDEX `idx_channel` (`channel`),
    INDEX `idx_send_time` (`send_time`),
    INDEX `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CRM 客户关怀日志表';

-- 3.1 客户关怀日志测试数据
INSERT IGNORE INTO `crm_care_log` (`id`, `customer_id`, `campaign_id`, `care_type`, `channel`, `template_code`, `content`, `status`, `send_time`, `remark`, `tenant_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(NULL, 1, NULL, 1, 1, 'crm_birthday_care', '尊敬的李明，祝您生日快乐！感谢您一直以来对我们的支持，愿您每天都充满阳光与欢笑！', 0, '2026-01-15 09:00:00', '客户生日当天自动发送', 0, 'system', NOW(), 'system', NOW(), b'0'),
(NULL, 2, NULL, 1, 2, 'crm_birthday_care', '尊敬的王芳，祝您生日快乐！感谢您一直以来对我们的支持，愿您每天都充满阳光与欢笑！', 0, '2026-01-22 09:00:00', '客户生日当天自动发送', 0, 'system', NOW(), 'system', NOW(), b'0'),
(NULL, 3, 1, 2, 1, 'crm_holiday_care', '尊敬的张伟，元旦快乐！愿您阖家幸福，万事如意！', 0, '2026-01-01 08:30:00', '元旦节日关怀', 0, 'system', NOW(), 'system', NOW(), b'0'),
(NULL, 4, 1, 2, 2, 'crm_holiday_care', '尊敬的刘洋，元旦快乐！愿您阖家幸福，万事如意！', 0, '2026-01-01 08:30:00', '元旦节日关怀', 0, 'system', NOW(), 'system', NOW(), b'0'),
(NULL, 5, NULL, 3, 1, 'crm_manual_care', '尊敬的陈静，感谢您长期以来的信任与合作，近期我们有新产品上线，欢迎了解！', 0, '2026-02-10 14:30:00', '手动发送产品推荐', 0, 'admin', NOW(), 'admin', NOW(), b'0'),
(NULL, 1, 2, 2, 1, 'crm_holiday_care', '尊敬的李明，春节快乐！愿您阖家幸福，万事如意！', 0, '2026-02-17 08:00:00', '春节节日关怀', 0, 'system', NOW(), 'system', NOW(), b'0'),
(NULL, 6, NULL, 1, 2, 'crm_birthday_care', '尊敬的赵丽，祝您生日快乐！感谢您一直以来对我们的支持，愿您每天都充满阳光与欢笑！', 1, '2026-02-20 09:00:00', '邮件发送失败-邮箱地址无效', 0, 'system', NOW(), 'system', NOW(), b'0'),
(NULL, 7, NULL, 3, 2, 'crm_manual_care', '尊敬的孙鹏，附件为本月账单明细，请查收。如有疑问请随时联系客服。', 0, '2026-03-05 10:15:00', '手动发送月度账单', 0, 'admin', NOW(), 'admin', NOW(), b'0'),
(NULL, 8, 3, 2, 1, 'crm_holiday_care', '尊敬的周杰，元宵节快乐！愿您阖家幸福，万事如意！', 0, '2026-03-03 08:30:00', '元宵节节日关怀', 0, 'system', NOW(), 'system', NOW(), b'0'),
(NULL, 2, NULL, 3, 1, 'crm_manual_care', '尊敬的王芳，您的会员等级已升级为VIP，享受更多专属优惠！', 0, '2026-03-18 16:00:00', '手动发送会员升级通知', 0, 'admin', NOW(), 'admin', NOW(), b'0'),
(NULL, 9, NULL, 1, 1, 'crm_birthday_care', '尊敬的吴强，祝您生日快乐！感谢您一直以来对我们的支持，愿您每天都充满阳光与欢笑！', 0, '2026-04-08 09:00:00', '客户生日当天自动发送', 0, 'system', NOW(), 'system', NOW(), b'0'),
(NULL, 10, 4, 2, 1, 'crm_holiday_care', '尊敬的郑秀，清明节安康。愿您平安顺遂，幸福常伴！', 0, '2026-04-05 08:00:00', '清明节节日关怀', 0, 'system', NOW(), 'system', NOW(), b'0'),
(NULL, 3, NULL, 3, 2, 'crm_manual_care', '尊敬的张伟，感谢您参加我们的客户满意度调查，您的反馈对我们非常重要。', 0, '2026-05-12 11:00:00', '手动发送满意度调查邀请', 0, 'admin', NOW(), 'admin', NOW(), b'0'),
(NULL, 11, NULL, 1, 2, 'crm_birthday_care', '尊敬的黄蕾，祝您生日快乐！感谢您一直以来对我们的支持，愿您每天都充满阳光与欢笑！', 0, '2026-05-25 09:00:00', '客户生日当天自动发送', 0, 'system', NOW(), 'system', NOW(), b'0'),
(NULL, 12, 5, 2, 1, 'crm_holiday_care', '尊敬的许峰，端午节安康！愿您阖家幸福，万事如意！', 0, '2026-06-19 08:00:00', '端午节节日关怀', 0, 'system', NOW(), 'system', NOW(), b'0'),
(NULL, 4, NULL, 3, 1, 'crm_manual_care', '尊敬的刘洋，您有一份专属优惠券即将过期，请及时使用！', 1, '2026-07-05 15:30:00', '短信发送失败-号码已停机', 0, 'admin', NOW(), 'admin', NOW(), b'0'),
(NULL, 13, NULL, 1, 1, 'crm_birthday_care', '尊敬的胡敏，祝您生日快乐！感谢您一直以来对我们的支持，愿您每天都充满阳光与欢笑！', 0, '2026-07-14 09:00:00', '客户生日当天自动发送', 0, 'system', NOW(), 'system', NOW(), b'0'),
(NULL, 5, 6, 2, 2, 'crm_holiday_care', '尊敬的陈静，中秋节快乐！愿您阖家幸福，万事如意！', 0, '2026-09-29 08:00:00', '中秋节节日关怀', 0, 'system', NOW(), 'system', NOW(), b'0'),
(NULL, 14, NULL, 3, 2, 'crm_manual_care', '尊敬的林涛，您订购的产品已发货，运单号：SF1234567890，请注意查收。', 0, '2026-10-15 13:45:00', '手动发送发货通知', 0, 'admin', NOW(), 'admin', NOW(), b'0'),
(NULL, 6, 7, 2, 1, 'crm_holiday_care', '尊敬的赵丽，国庆节快乐！愿您阖家幸福，万事如意！', 0, '2026-10-01 08:00:00', '国庆节节日关怀', 0, 'system', NOW(), 'system', NOW(), b'0');

-- 4. 群发功能菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(2840, '营销活动群发', '', 2, 20, 2930, 'send', 'ep:promotion', 'crm/campaign/send', 'CrmCampaignSend', 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0'),
(2841, '群发查询', 'crm:campaign-send:query', 3, 1, 2840, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');

-- 5. 关怀日志菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(2842, '客户关怀日志', '', 2, 21, 2930, 'care-log', 'ep:present', 'crm/campaign/CareLogList', 'CrmCareLog', 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0'),
(2843, '关怀日志查询', 'crm:care-log:query', 3, 1, 2842, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');

-- 6. 发送统计菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(2844, '发送统计', '', 2, 22, 2930, 'send-statistics', 'ep:data-analysis', 'crm/campaign/SendStatistics', 'CrmSendStatistics', 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0'),
(2845, '发送统计查询', 'crm:send-statistics:query', 3, 1, 2844, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');

-- 7. 发送日志菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(2851, '发送日志', '', 2, 24, 2930, 'send-log', 'ep:document', 'crm/campaign/SendLogList', 'CrmSendLogList', 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0'),
(2852, '发送日志查询', 'crm:campaign-send:query', 3, 1, 2851, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');

-- 8. 生日关怀通知模板
INSERT IGNORE INTO `system_notify_template` (`id`, `name`, `code`, `nickname`, `content`, `type`, `params`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (NULL, '生日关怀模板', 'crm_birthday_care', 'CRM系统', '尊敬的{name}，祝您生日快乐！感谢您一直以来对我们的支持，愿您每天都充满阳光与欢笑！', 2, '["name"]', 0, '客户生日自动发送', '1', NOW(), '1', NOW(), b'0');

-- 9. 节日关怀通知模板
INSERT IGNORE INTO `system_notify_template` (`id`, `name`, `code`, `nickname`, `content`, `type`, `params`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (NULL, '节日关怀模板', 'crm_holiday_care', 'CRM系统', '尊敬的{name}，{holidayName}快乐！愿您阖家幸福，万事如意！', 2, '["name","holidayName"]', 0, '节日自动发送关怀', '1', NOW(), '1', NOW(), b'0');

-- 10. 关怀类型字典
INSERT IGNORE INTO `system_dict_type` (`name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES ('CRM 关怀类型', 'crm_care_type', 0, 'CRM客户关怀类型', '', NOW(), '', NOW(), b'0');

INSERT IGNORE INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(1, '生日关怀', '1', 'crm_care_type', 0, '客户生日自动发送关怀消息', '', NOW(), '', NOW(), b'0'),
(2, '节日关怀', '2', 'crm_care_type', 0, '节日自动发送关怀消息', '', NOW(), '', NOW(), b'0'),
(3, '手动关怀', '3', 'crm_care_type', 0, '手动发送客户关怀消息', '', NOW(), '', NOW(), b'0');

-- 11. 发送渠道字典
INSERT IGNORE INTO `system_dict_type` (`name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES ('CRM 发送渠道', 'crm_send_channel', 0, 'CRM消息发送渠道', '', NOW(), '', NOW(), b'0');

INSERT IGNORE INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(1, '短信', '1', 'crm_send_channel', 0, '通过短信渠道发送', '', NOW(), '', NOW(), b'0'),
(2, '邮件', '2', 'crm_send_channel', 0, '通过邮件渠道发送', '', NOW(), '', NOW(), b'0');

-- 12. 注册生日关怀定时任务 (每天上午8:00执行)
INSERT IGNORE INTO `infra_job` (`id`, `name`, `status`, `handler_name`, `handler_param`, `cron_expression`, `retry_count`, `retry_interval`, `monitor_timeout`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (NULL, 'CRM客户生日关怀任务', 0, 'crmCustomerCareJob', NULL, '0 0 8 * * ?', 0, 0, 0, '1', NOW(), '1', NOW(), b'0');

-- 13. 发送任务表
CREATE TABLE IF NOT EXISTS `crm_send_task` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '编号',
    `activity_id` BIGINT NULL COMMENT '关联营销活动编号',
    `task_type` VARCHAR(20) NOT NULL DEFAULT 'MARKETING' COMMENT '任务类型 MARKETING/AUTO_CARE',
    `template_id` BIGINT NOT NULL COMMENT '模板ID',
    `template_code` VARCHAR(100) NOT NULL COMMENT '模板编码',
    `channel` INT NOT NULL COMMENT '发送渠道 1-短信 2-邮件',
    `customer_select_mode` VARCHAR(20) NULL COMMENT '客户选择模式 ALL_ACTIVE/CSV/BY_TAG',
    `customer_count` INT NOT NULL DEFAULT 0 COMMENT '目标客户数',
    `send_mode` VARCHAR(10) NOT NULL DEFAULT 'IMMEDIATE' COMMENT '发送模式 IMMEDIATE/SCHEDULED',
    `scheduled_time` DATETIME NULL COMMENT '定时发送时间',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态 PENDING/RUNNING/COMPLETED/FAILED',
    `missing_var_count` INT NOT NULL DEFAULT 0 COMMENT '缺失变量客户数',
    `tenant_id` BIGINT NOT NULL DEFAULT 0 COMMENT '租户编号',
    `creator` VARCHAR(64) NULL DEFAULT '' COMMENT '创建人',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) NULL DEFAULT '' COMMENT '更新人',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    INDEX `idx_activity_id` (`activity_id`),
    INDEX `idx_task_type` (`task_type`),
    INDEX `idx_channel` (`channel`),
    INDEX `idx_status` (`status`),
    INDEX `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CRM 发送任务表';

-- 14. 发送任务类型字典
INSERT IGNORE INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(1, '营销群发', 'MARKETING', 'crm_send_channel', 0, '营销活动群发', '', NOW(), '', NOW(), b'0'),
(2, '自动关怀', 'AUTO_CARE', 'crm_send_channel', 0, '自动化客户关怀', '', NOW(), '', NOW(), b'0');

-- 15. 发送任务管理菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(2860, '发送任务管理', '', 2, 25, 2930, 'send-task', 'ep:message', 'crm/sendTask/index', 'CrmSendTask', 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0'),
(2861, '发送任务查询', 'crm:send-task:query', 3, 1, 2860, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0'),
(2862, '发送任务创建', 'crm:send-task:create', 3, 2, 2860, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');

-- 16. 发送日志增加追踪字段
ALTER TABLE `crm_campaign_send_log`
    ADD COLUMN IF NOT EXISTS `open_time` DATETIME NULL COMMENT '打开时间',
    ADD COLUMN IF NOT EXISTS `click_time` DATETIME NULL COMMENT '点击时间';

-- 17. 自动关怀规则表
CREATE TABLE IF NOT EXISTS `crm_care_rule` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '编号',
    `name` VARCHAR(200) NOT NULL COMMENT '规则名称',
    `trigger_type` VARCHAR(20) NOT NULL COMMENT '触发类型 BIRTHDAY/HOLIDAY/MANUAL',
    `trigger_days_before` INT NOT NULL DEFAULT 0 COMMENT '提前N天触发（0=当天）',
    `holiday_id` BIGINT NULL COMMENT '关联节日ID',
    `template_id` BIGINT NOT NULL COMMENT '模板ID',
    `template_code` VARCHAR(100) NOT NULL COMMENT '模板编码',
    `channel` INT NOT NULL COMMENT '发送渠道 1-短信 2-邮件',
    `send_window_start` TIME NULL COMMENT '发送窗口开始',
    `send_window_end` TIME NULL COMMENT '发送窗口结束',
    `is_enabled` TINYINT NOT NULL DEFAULT 1 COMMENT '0-停用 1-启用',
    `last_exec_time` DATETIME NULL COMMENT '最近一次执行时间',
    `remark` VARCHAR(500) NULL COMMENT '备注',
    `tenant_id` BIGINT NOT NULL DEFAULT 0 COMMENT '租户编号',
    `creator` VARCHAR(64) NULL DEFAULT '' COMMENT '创建人',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) NULL DEFAULT '' COMMENT '更新人',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    INDEX `idx_trigger_type` (`trigger_type`),
    INDEX `idx_is_enabled` (`is_enabled`),
    INDEX `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CRM 自动关怀规则表';

-- 18. 自动关怀规则菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(2870, '自动关怀规则', '', 2, 22, 2930, 'care-rule', 'ep:bell', 'crm/care/careRule/index', 'CrmCareRule', 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0'),
(2871, '关怀规则查询', 'crm:care-rule:query', 3, 1, 2870, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0'),
(2872, '关怀规则创建', 'crm:care-rule:create', 3, 2, 2870, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0'),
(2873, '关怀规则更新', 'crm:care-rule:update', 3, 3, 2870, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0'),
(2874, '关怀规则删除', 'crm:care-rule:delete', 3, 4, 2870, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');
