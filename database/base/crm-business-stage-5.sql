-- ============================================================
-- 商机阶段状态机（5阶段+流失标记）初始化脚本
-- ============================================================

-- 1. 为 crm_business 表新增流失原因字段
ALTER TABLE `crm_business` ADD COLUMN `lose_reason` varchar(500) NULL DEFAULT NULL COMMENT '流失原因' AFTER `end_remark`;

-- 2. 插入标准5阶段状态组数据
-- 如果已存在则先删除
DELETE FROM `crm_business_status` WHERE `type_id` = 100;
DELETE FROM `crm_business_status_type` WHERE `id` = 100;

-- 插入状态组
INSERT INTO `crm_business_status_type` (`id`, `name`, `dept_ids`, `creator`, `create_time`, `updater`, `update_time`, `tenant_id`, `deleted`) 
VALUES (100, '标准5阶段', '', '1', NOW(), '1', NOW(), 1, b'0');

-- 插入5个阶段状态
INSERT INTO `crm_business_status` (`type_id`, `name`, `percent`, `sort`, `creator`, `create_time`, `updater`, `update_time`, `tenant_id`, `deleted`) 
VALUES 
(100, '初步接触', 10, 0, '1', NOW(), '1', NOW(), 1, b'0'),
(100, '需求分析', 20, 1, '1', NOW(), '1', NOW(), 1, b'0'),
(100, '方案制定', 30, 2, '1', NOW(), '1', NOW(), 1, b'0'),
(100, '商务谈判', 50, 3, '1', NOW(), '1', NOW(), 1, b'0'),
(100, '签约成交', 80, 4, '1', NOW(), '1', NOW(), 1, b'0');
