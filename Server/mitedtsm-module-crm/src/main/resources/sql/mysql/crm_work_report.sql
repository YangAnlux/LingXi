CREATE TABLE IF NOT EXISTS `crm_work_report` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '编号',
    `title` VARCHAR(200) NOT NULL COMMENT '报告标题',
    `content` TEXT NULL COMMENT '报告内容',
    `report_date` DATE NULL COMMENT '报告日期',
    `report_type` INT NULL COMMENT '报告类型 1-日报 2-周报 3-月报 4-其他',
    `status` INT NOT NULL DEFAULT 0 COMMENT '审批状态 0-草稿 10-审批中 20-审核通过 30-审核不通过 40-已取消',
    `owner_user_id` BIGINT NULL COMMENT '提交人用户编号',
    `audit_user_id` BIGINT NULL COMMENT '审批人用户编号',
    `audit_time` DATETIME NULL COMMENT '审批时间',
    `audit_remark` VARCHAR(500) NULL COMMENT '审批意见',
    `related_campaign_id` BIGINT NULL COMMENT '关联营销活动编号',
    `related_customer_id` BIGINT NULL COMMENT '关联客户编号',
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
    INDEX `idx_report_date` (`report_date`),
    INDEX `idx_report_type` (`report_type`),
    INDEX `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CRM 工作报告表';

INSERT IGNORE INTO `system_dict_type` (`name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES ('CRM工作报告类型', 'crm_work_report_type', 0, '工作报告类型', '', NOW(), '', NOW(), b'0');

INSERT IGNORE INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(1, '日报', '1', 'crm_work_report_type', 0, '日报', '', NOW(), '', NOW(), b'0'),
(2, '周报', '2', 'crm_work_report_type', 0, '周报', '', NOW(), '', NOW(), b'0'),
(3, '月报', '3', 'crm_work_report_type', 0, '月报', '', NOW(), '', NOW(), b'0'),
(4, '其他', '4', 'crm_work_report_type', 0, '其他', '', NOW(), '', NOW(), b'0');

INSERT IGNORE INTO `system_dict_type` (`name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES ('CRM工作报告审批状态', 'crm_work_report_status', 0, '工作报告审批状态', '', NOW(), '', NOW(), b'0');

INSERT IGNORE INTO `system_dict_data` (`sort`, `label`, `value`, `dict_type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(1, '草稿', '0', 'crm_work_report_status', 0, '草稿', '', NOW(), '', NOW(), b'0'),
(2, '审批中', '10', 'crm_work_report_status', 0, '审批中', '', NOW(), '', NOW(), b'0'),
(3, '审核通过', '20', 'crm_work_report_status', 0, '审核通过', '', NOW(), '', NOW(), b'0'),
(4, '审核不通过', '30', 'crm_work_report_status', 0, '审核不通过', '', NOW(), '', NOW(), b'0'),
(5, '已取消', '40', 'crm_work_report_status', 0, '已取消', '', NOW(), '', NOW(), b'0');

INSERT IGNORE INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES
(2857, '工作报告', '', 2, 16, 2397, 'workreport', 'ep:document', 'crm/workreport/index', 'CrmWorkReport', 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0'),
(2858, '工作报告查询', 'crm:work-report:query', 3, 1, 2857, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0'),
(2859, '工作报告创建', 'crm:work-report:create', 3, 2, 2857, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0'),
(2860, '工作报告更新', 'crm:work-report:update', 3, 3, 2857, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0'),
(2861, '工作报告删除', 'crm:work-report:delete', 3, 4, 2857, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0'),
(2862, '工作报告导出', 'crm:work-report:export', 3, 5, 2857, '', '', '', NULL, 0, b'1', b'1', b'1', '', NOW(), '', NOW(), b'0');
