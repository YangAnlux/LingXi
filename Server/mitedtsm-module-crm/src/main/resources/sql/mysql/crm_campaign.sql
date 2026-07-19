CREATE TABLE IF NOT EXISTS `crm_campaign` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '编号',
    `name` VARCHAR(200) NOT NULL COMMENT '活动名称',
    `status` INT NOT NULL COMMENT '状态 1-草稿 2-进行中 3-已结束 4-已取消',
    `start_time` DATETIME NULL COMMENT '开始时间',
    `end_time` DATETIME NULL COMMENT '结束时间',
    `budget` DECIMAL(12,2) NULL DEFAULT 0 COMMENT '活动预算(元)',
    `actual_cost` DECIMAL(12,2) NULL DEFAULT 0 COMMENT '实际花费(元)',
    `channel` INT NULL COMMENT '渠道类型',
    `description` TEXT NULL COMMENT '活动描述',
    `owner_user_id` BIGINT NULL COMMENT '负责人用户编号',
    `remark` VARCHAR(500) NULL COMMENT '备注',
    `tenant_id` BIGINT NOT NULL DEFAULT 0 COMMENT '租户编号',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_user` BIGINT NOT NULL COMMENT '创建人',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `update_user` BIGINT NOT NULL COMMENT '更新人',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (`id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_owner_user_id` (`owner_user_id`),
    INDEX `idx_start_time` (`start_time`),
    INDEX `idx_end_time` (`end_time`),
    INDEX `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CRM 营销活动表';

INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `create_time`, `create_user`, `update_time`, `update_user`, `deleted`)
VALUES (NULL, 'CRM营销活动状态', 'crm_campaign_status', 1, '营销活动状态', NOW(), 1, NOW(), 1, 0);

INSERT INTO `system_dict_data` (`id`, `type`, `label`, `value`, `sort`, `status`, `remark`, `create_time`, `create_user`, `update_time`, `update_user`, `deleted`)
VALUES (NULL, 'crm_campaign_status', '草稿', 1, 1, 1, '草稿状态', NOW(), 1, NOW(), 1, 0);
INSERT INTO `system_dict_data` (`id`, `type`, `label`, `value`, `sort`, `status`, `remark`, `create_time`, `create_user`, `update_time`, `update_user`, `deleted`)
VALUES (NULL, 'crm_campaign_status', '进行中', 2, 2, 1, '进行中', NOW(), 1, NOW(), 1, 0);
INSERT INTO `system_dict_data` (`id`, `type`, `label`, `value`, `sort`, `status`, `remark`, `create_time`, `create_user`, `update_time`, `update_user`, `deleted`)
VALUES (NULL, 'crm_campaign_status', '已结束', 3, 3, 1, '已结束', NOW(), 1, NOW(), 1, 0);
INSERT INTO `system_dict_data` (`id`, `type`, `label`, `value`, `sort`, `status`, `remark`, `create_time`, `create_user`, `update_time`, `update_user`, `deleted`)
VALUES (NULL, 'crm_campaign_status', '已取消', 4, 4, 1, '已取消', NOW(), 1, NOW(), 1, 0);

INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `create_time`, `create_user`, `update_time`, `update_user`, `deleted`)
VALUES (NULL, 'CRM营销活动渠道', 'crm_campaign_channel', 1, '营销活动渠道', NOW(), 1, NOW(), 1, 0);

INSERT INTO `system_dict_data` (`id`, `type`, `label`, `value`, `sort`, `status`, `remark`, `create_time`, `create_user`, `update_time`, `update_user`, `deleted`)
VALUES (NULL, 'crm_campaign_channel', '线上广告', 1, 1, 1, '线上广告', NOW(), 1, NOW(), 1, 0);
INSERT INTO `system_dict_data` (`id`, `type`, `label`, `value`, `sort`, `status`, `remark`, `create_time`, `create_user`, `update_time`, `update_user`, `deleted`)
VALUES (NULL, 'crm_campaign_channel', '线下活动', 2, 2, 1, '线下活动', NOW(), 1, NOW(), 1, 0);
INSERT INTO `system_dict_data` (`id`, `type`, `label`, `value`, `sort`, `status`, `remark`, `create_time`, `create_user`, `update_time`, `update_user`, `deleted`)
VALUES (NULL, 'crm_campaign_channel', '邮件营销', 3, 3, 1, '邮件营销', NOW(), 1, NOW(), 1, 0);
INSERT INTO `system_dict_data` (`id`, `type`, `label`, `value`, `sort`, `status`, `remark`, `create_time`, `create_user`, `update_time`, `update_user`, `deleted`)
VALUES (NULL, 'crm_campaign_channel', '短信营销', 4, 4, 1, '短信营销', NOW(), 1, NOW(), 1, 0);
INSERT INTO `system_dict_data` (`id`, `type`, `label`, `value`, `sort`, `status`, `remark`, `create_time`, `create_user`, `update_time`, `update_user`, `deleted`)
VALUES (NULL, 'crm_campaign_channel', '社交媒体', 5, 5, 1, '社交媒体', NOW(), 1, NOW(), 1, 0);

INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (2800, '营销活动', '', 2, 15, 2397, 'campaign', 'ep:flag', 'crm/campaign/index', 'CrmCampaign', 0, b'1', b'1', b'1', '', NOW(), 1, NOW(), b'0');

INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (2801, '营销活动查询', 'crm:campaign:query', 3, 1, 2800, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');

INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (2802, '营销活动创建', 'crm:campaign:create', 3, 2, 2800, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');

INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (2803, '营销活动更新', 'crm:campaign:update', 3, 3, 2800, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');

INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (2804, '营销活动删除', 'crm:campaign:delete', 3, 4, 2800, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');

INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (2805, '营销活动导出', 'crm:campaign:export', 3, 5, 2800, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');
