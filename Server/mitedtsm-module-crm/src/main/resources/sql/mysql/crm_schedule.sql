CREATE TABLE IF NOT EXISTS `crm_schedule` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '编号',
    `title` VARCHAR(200) NOT NULL COMMENT '日程标题',
    `description` TEXT NULL COMMENT '日程描述',
    `start_time` DATETIME NOT NULL COMMENT '开始时间',
    `end_time` DATETIME NOT NULL COMMENT '结束时间',
    `all_day` TINYINT NOT NULL DEFAULT 0 COMMENT '是否全天 0-否 1-是',
    `color` VARCHAR(20) NULL COMMENT '颜色标记',
    `schedule_type` INT NOT NULL DEFAULT 1 COMMENT '日程类型 1-个人日程 2-任务关联 3-会议',
    `status` INT NOT NULL DEFAULT 0 COMMENT '状态 0-正常 1-已取消',
    `owner_user_id` BIGINT NULL COMMENT '所属用户编号',
    `related_task_id` BIGINT NULL COMMENT '关联任务编号',
    `remark` VARCHAR(500) NULL COMMENT '备注',
    `tenant_id` BIGINT NOT NULL DEFAULT 0 COMMENT '租户编号',
    `creator` VARCHAR(64) NULL DEFAULT '' COMMENT '创建人',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) NULL DEFAULT '' COMMENT '更新人',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    INDEX `idx_owner_user_id` (`owner_user_id`),
    INDEX `idx_start_time` (`start_time`),
    INDEX `idx_end_time` (`end_time`),
    INDEX `idx_status` (`status`),
    INDEX `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CRM 日程表';

INSERT IGNORE INTO `system_dict_type` (`name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES ('CRM日程类型', 'crm_schedule_type', 0, '日程类型', '', NOW(), '', NOW(), b'0');

INSERT IGNORE INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(1, '个人日程', '1', 'crm_schedule_type', 0, '个人日程', '', NOW(), '', NOW(), b'0'),
(2, '任务关联', '2', 'crm_schedule_type', 0, '任务关联', '', NOW(), '', NOW(), b'0'),
(3, '会议', '3', 'crm_schedule_type', 0, '会议', '', NOW(), '', NOW(), b'0');

INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(2830, '日程管理', '', 2, 18, 2397, 'schedule', 'ep:calendar', 'crm/schedule/index', 'CrmSchedule', 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0'),
(2831, '日程查询', 'crm:schedule:query', 3, 1, 2830, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0'),
(2832, '日程创建', 'crm:schedule:create', 3, 2, 2830, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0'),
(2833, '日程更新', 'crm:schedule:update', 3, 3, 2830, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0'),
(2834, '日程删除', 'crm:schedule:delete', 3, 4, 2830, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');

INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(2835, '任务看板', '', 2, 19, 2397, 'task-kanban', 'ep:grid', 'crm/task/kanban', 'CrmTaskKanban', 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0'),
(2836, '任务看板查询', 'crm:task-kanban:query', 3, 1, 2835, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');
