CREATE TABLE IF NOT EXISTS `crm_task` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '编号',
    `name` VARCHAR(200) NOT NULL COMMENT '任务名称',
    `description` TEXT NULL COMMENT '任务描述',
    `status` INT NOT NULL DEFAULT 0 COMMENT '状态 0-待办 1-进行中 2-已完成 3-已取消',
    `priority` INT NOT NULL DEFAULT 1 COMMENT '优先级 0-低 1-中 2-高 3-紧急',
    `start_time` DATETIME NULL COMMENT '计划开始时间',
    `end_time` DATETIME NULL COMMENT '截止时间',
    `completed_time` DATETIME NULL COMMENT '实际完成时间',
    `owner_user_id` BIGINT NULL COMMENT '负责人用户编号',
    `assigner_user_id` BIGINT NULL COMMENT '分配人用户编号',
    `related_campaign_id` BIGINT NULL COMMENT '关联营销活动编号',
    `related_customer_id` BIGINT NULL COMMENT '关联客户编号',
    `related_business_id` BIGINT NULL COMMENT '关联商机编号',
    `remark` VARCHAR(500) NULL COMMENT '备注',
    `tenant_id` BIGINT NOT NULL DEFAULT 0 COMMENT '租户编号',
    `creator` VARCHAR(64) NULL DEFAULT '' COMMENT '创建人',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) NULL DEFAULT '' COMMENT '更新人',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_priority` (`priority`),
    INDEX `idx_owner_user_id` (`owner_user_id`),
    INDEX `idx_start_time` (`start_time`),
    INDEX `idx_end_time` (`end_time`),
    INDEX `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CRM 任务表';

INSERT IGNORE INTO `system_dict_type` (`name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES ('CRM任务状态', 'crm_task_status', 0, '任务状态', '', NOW(), '', NOW(), b'0');

INSERT IGNORE INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(1, '待办', '0', 'crm_task_status', 0, '待办', '', NOW(), '', NOW(), b'0'),
(2, '进行中', '1', 'crm_task_status', 0, '进行中', '', NOW(), '', NOW(), b'0'),
(3, '已完成', '2', 'crm_task_status', 0, '已完成', '', NOW(), '', NOW(), b'0'),
(4, '已取消', '3', 'crm_task_status', 0, '已取消', '', NOW(), '', NOW(), b'0');

INSERT IGNORE INTO `system_dict_type` (`name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES ('CRM任务优先级', 'crm_task_priority', 0, '任务优先级', '', NOW(), '', NOW(), b'0');

INSERT IGNORE INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(1, '低', '0', 'crm_task_priority', 0, '低', '', NOW(), '', NOW(), b'0'),
(2, '中', '1', 'crm_task_priority', 0, '中', '', NOW(), '', NOW(), b'0'),
(3, '高', '2', 'crm_task_priority', 0, '高', '', NOW(), '', NOW(), b'0'),
(4, '紧急', '3', 'crm_task_priority', 0, '紧急', '', NOW(), '', NOW(), b'0');

INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(2820, '任务管理', '', 2, 17, 2397, 'task', 'ep:list', 'crm/task/index', 'CrmTask', 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0'),
(2821, '任务管理查询', 'crm:task:query', 3, 1, 2820, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0'),
(2822, '任务管理创建', 'crm:task:create', 3, 2, 2820, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0'),
(2823, '任务管理更新', 'crm:task:update', 3, 3, 2820, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0'),
(2824, '任务管理删除', 'crm:task:delete', 3, 4, 2820, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0'),
(2825, '任务管理导出', 'crm:task:export', 3, 5, 2820, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');
