-- CRM 通知模板：任务分配通知
INSERT INTO `system_notify_template` (`id`, `name`, `code`, `nickname`, `content`, `type`, `params`, `status`, `remark`, `create_time`, `creator`, `update_time`, `updater`, `deleted`)
VALUES (NULL, '任务分配通知', 'crm_task_assign', 'CRM系统', '您有一个新任务【{taskName}】，截止时间：{endTime}，请及时处理。', 2, '["taskName","endTime"]', 0, '任务分配给用户时发送', NOW(), '1', NOW(), '1', b'0');

-- CRM 通知模板：任务截止提醒
INSERT INTO `system_notify_template` (`id`, `name`, `code`, `nickname`, `content`, `type`, `params`, `status`, `remark`, `create_time`, `creator`, `update_time`, `updater`, `deleted`)
VALUES (NULL, '任务截止提醒', 'crm_task_due', 'CRM系统', '您的任务【{taskName}】即将到期，截止时间：{endTime}，请尽快完成。', 2, '["taskName","endTime"]', 0, '任务即将到期时定时发送提醒', NOW(), '1', NOW(), '1', b'0');

-- CRM 通知模板：日程变更通知
INSERT INTO `system_notify_template` (`id`, `name`, `code`, `nickname`, `content`, `type`, `params`, `status`, `remark`, `create_time`, `creator`, `update_time`, `updater`, `deleted`)
VALUES (NULL, '日程变更通知', 'crm_schedule_change', 'CRM系统', '您的日程【{scheduleTitle}】已{action}，时间：{startTime}。', 2, '["scheduleTitle","action","startTime"]', 0, '日程创建/更新时发送通知', NOW(), '1', NOW(), '1', b'0');

-- 注册任务截止日期提醒定时任务 (默认每小时执行一次)
INSERT INTO `infra_job` (`id`, `name`, `status`, `handler_name`, `handler_param`, `cron_expression`, `retry_count`, `retry_interval`, `monitor_timeout`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (NULL, 'CRM任务截止日期提醒', 0, 'crmTaskRemindJob', NULL, '0 0 * * * ?', 0, 0, 0, '1', NOW(), '1', NOW(), b'0');
