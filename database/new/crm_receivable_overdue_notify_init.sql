-- 23软4胡伟-202305566535-修改于2026.07.16
-- [ADD START] 回款逾期通知模板 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16

-- 站内信模板：回款逾期提醒
INSERT IGNORE INTO `system_notify_template` (`id`, `name`, `code`, `nickname`, `content`, `type`, `params`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (16, '回款逾期提醒', 'crm_receivable_overdue', '系统通知', '<p>回款计划【{period}】已逾期 {overdueDays} 天，金额 {price} 元，请及时处理。</p>', 2, '["period","overdueDays","price"]', 0, 'CRM 回款计划逾期站内信通知', '1', NOW(), '1', NOW(), b'0');

-- 邮件模板：回款逾期告警
INSERT IGNORE INTO `system_mail_template` (`id`, `name`, `code`, `account_id`, `nickname`, `title`, `content`, `params`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (16, '回款逾期告警', 'crm-receivable-overdue-mail', 1, 'CRM系统', '{level}：回款计划已逾期 {overdueDays} 天', '<h3>回款逾期告警</h3><p>合同【{contractNo}】第 {period} 期回款计划已逾期 {overdueDays} 天。</p><p>计划回款金额：{price} 元</p><p>计划回款日期：{returnTime}</p><p>告警等级：{level}</p><p>请尽快跟进处理。</p>', '["period","overdueDays","price","contractNo","returnTime","level"]', 0, 'CRM 回款计划逾期邮件告警', '1', NOW(), '1', NOW(), b'0');

-- [ADD END] 回款逾期通知模板 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
