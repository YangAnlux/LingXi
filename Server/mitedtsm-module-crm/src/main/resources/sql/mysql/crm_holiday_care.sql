-- =============================================
-- CRM 节日关怀配置 & 节日关怀任务
-- =============================================

-- 1. 节日配置表
CREATE TABLE IF NOT EXISTS `crm_holiday` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '编号',
    `name` VARCHAR(100) NOT NULL COMMENT '节日名称',
    `holiday_date` DATE NOT NULL COMMENT '节日日期',
    `template_code` VARCHAR(100) NULL DEFAULT 'crm_holiday_care' COMMENT '模板编码',
    `status` INT NOT NULL DEFAULT 0 COMMENT '状态 0-启用 1-禁用',
    `remark` VARCHAR(500) NULL COMMENT '备注',
    `tenant_id` BIGINT NOT NULL DEFAULT 0 COMMENT '租户编号',
    `creator` VARCHAR(64) NULL DEFAULT '' COMMENT '创建人',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) NULL DEFAULT '' COMMENT '更新人',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    INDEX `idx_holiday_date` (`holiday_date`),
    INDEX `idx_status` (`status`),
    INDEX `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CRM 节日配置表';

-- =============================================
-- 注意 (NOTICE):
-- 以下种子数据的 holiday_date 固定为 2026-2027 年日期。
-- 节日关怀定时任务仅在这些日期当天触发。
-- 如需持续使用，请每年更新节日日期（修改年份即可）。
-- 未来版本将支持 is_recurring 字段，按 MM-DD 格式实现年度自动重复。
-- =============================================

-- 2. 插入常见节日数据（公历日期）
INSERT IGNORE INTO `crm_holiday` (`id`, `name`, `holiday_date`, `template_code`, `status`, `remark`, `tenant_id`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(NULL, '元旦', '2026-01-01', 'crm_holiday_care', 0, '元旦新年', 0, '', NOW(), '', NOW(), b'0'),
(NULL, '春节', '2026-02-17', 'crm_holiday_care', 0, '春节', 0, '', NOW(), '', NOW(), b'0'),
(NULL, '元宵节', '2026-03-03', 'crm_holiday_care', 0, '元宵节', 0, '', NOW(), '', NOW(), b'0'),
(NULL, '清明节', '2026-04-05', 'crm_holiday_care', 0, '清明节', 0, '', NOW(), '', NOW(), b'0'),
(NULL, '端午节', '2026-06-19', 'crm_holiday_care', 0, '端午节', 0, '', NOW(), '', NOW(), b'0'),
(NULL, '中秋节', '2026-09-29', 'crm_holiday_care', 0, '中秋节', 0, '', NOW(), '', NOW(), b'0'),
(NULL, '国庆节', '2026-10-01', 'crm_holiday_care', 0, '国庆节', 0, '', NOW(), '', NOW(), b'0'),
(NULL, '重阳节', '2026-10-21', 'crm_holiday_care', 0, '重阳节', 0, '', NOW(), '', NOW(), b'0'),
(NULL, '腊八节', '2027-01-02', 'crm_holiday_care', 0, '腊八节', 0, '', NOW(), '', NOW(), b'0');

-- 3. 节日关怀菜单
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(2846, '节日配置', '', 2, 23, 2930, 'holiday-config', 'ep:present', 'crm/campaign/HolidayConfig', 'CrmHolidayConfig', 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0'),
(2847, '节日配置查询', 'crm:holiday:query', 3, 1, 2846, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0'),
(2848, '节日配置创建', 'crm:holiday:create', 3, 2, 2846, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0'),
(2849, '节日配置更新', 'crm:holiday:update', 3, 3, 2846, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0'),
(2850, '节日配置删除', 'crm:holiday:delete', 3, 4, 2846, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');

-- 4. 注册节日关怀定时任务 (每天上午9:00执行)
INSERT IGNORE INTO `infra_job` (`id`, `name`, `status`, `handler_name`, `handler_param`, `cron_expression`, `retry_count`, `retry_interval`, `monitor_timeout`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (NULL, 'CRM客户节日关怀任务', 0, 'crmCustomerHolidayCareJob', NULL, '0 0 9 * * ?', 0, 0, 0, '1', NOW(), '1', NOW(), b'0');
