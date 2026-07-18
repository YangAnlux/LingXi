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
    `creator` VARCHAR(64) NULL DEFAULT '' COMMENT '创建人',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) NULL DEFAULT '' COMMENT '更新人',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_owner_user_id` (`owner_user_id`),
    INDEX `idx_start_time` (`start_time`),
    INDEX `idx_end_time` (`end_time`),
    INDEX `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CRM 营销活动表';

INSERT IGNORE INTO `system_dict_type` (`name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES ('CRM营销活动状态', 'crm_campaign_status', 0, '营销活动状态', '', NOW(), '', NOW(), b'0');

INSERT IGNORE INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(1, '草稿', '1', 'crm_campaign_status', 0, '草稿状态', '', NOW(), '', NOW(), b'0'),
(2, '进行中', '2', 'crm_campaign_status', 0, '进行中', '', NOW(), '', NOW(), b'0'),
(3, '已结束', '3', 'crm_campaign_status', 0, '已结束', '', NOW(), '', NOW(), b'0'),
(4, '已取消', '4', 'crm_campaign_status', 0, '已取消', '', NOW(), '', NOW(), b'0');

INSERT IGNORE INTO `system_dict_type` (`name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES ('CRM营销活动渠道', 'crm_campaign_channel', 0, '营销活动渠道', '', NOW(), '', NOW(), b'0');

INSERT IGNORE INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(1, '线上广告', '1', 'crm_campaign_channel', 0, '线上广告', '', NOW(), '', NOW(), b'0'),
(2, '线下活动', '2', 'crm_campaign_channel', 0, '线下活动', '', NOW(), '', NOW(), b'0'),
(3, '邮件营销', '3', 'crm_campaign_channel', 0, '邮件营销', '', NOW(), '', NOW(), b'0'),
(4, '短信营销', '4', 'crm_campaign_channel', 0, '短信营销', '', NOW(), '', NOW(), b'0'),
(5, '社交媒体', '5', 'crm_campaign_channel', 0, '社交媒体', '', NOW(), '', NOW(), b'0');

-- 将原有的 2930 "营销活动" 从页面菜单(type=2)改为目录(type=1)
-- 使其作为分组目录，不再直接渲染页面，展示子菜单项
UPDATE `system_menu`
SET `type` = 1,
    `component` = '',
    `component_name` = '',
    `always_show` = b'1'
WHERE `id` = 2930;

-- 新增 "营销活动列表" 子菜单，放在营销活动目录下，与群发/关怀日志/发送统计/节日配置并列
INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (2936, '营销活动列表', '', 2, 10, 2930, 'list', 'ep:flag', 'crm/campaign/index', 'CrmCampaignList', 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');

-- 将原有的权限按钮迁移到 "营销活动列表" 页面下
UPDATE `system_menu` SET `parent_id` = 2936 WHERE `id` = 2931 AND `parent_id` = 2930;
UPDATE `system_menu` SET `parent_id` = 2936 WHERE `id` = 2932 AND `parent_id` = 2930;
UPDATE `system_menu` SET `parent_id` = 2936 WHERE `id` = 2933 AND `parent_id` = 2930;
UPDATE `system_menu` SET `parent_id` = 2936 WHERE `id` = 2934 AND `parent_id` = 2930;
UPDATE `system_menu` SET `parent_id` = 2936 WHERE `id` = 2935 AND `parent_id` = 2930;
