-- 23软2张奎良-202305566305
-- 为 mitedtsm-module-report 模块创建 MySQL 生产环境表结构
-- 说明：TaskDO、WorkReportDO 继承 BaseDO（非 TenantBaseDO），因此表中不包含 tenant_id 字段。

-- ============================================================
-- 1. 工作报表表
-- ============================================================
CREATE TABLE IF NOT EXISTS `report_work_report` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
    `type` int NOT NULL DEFAULT 1 COMMENT '报表类型（1-日报，2-周报，3-月报）',
    `title` varchar(200) NOT NULL COMMENT '报表标题',
    `report_date` date NOT NULL COMMENT '报表日期',
    `end_date` date COMMENT '周期结束日期',
    `content` text COMMENT '工作内容',
    `achievements` text COMMENT '工作成果',
    `problems` text COMMENT '问题与困难',
    `plan` text COMMENT '工作计划',
    `reporter_id` bigint COMMENT '报告人ID',
    `reporter_name` varchar(50) COMMENT '报告人姓名',
    `dept_id` bigint COMMENT '部门ID',
    `dept_name` varchar(100) COMMENT '部门名称',
    `status` int NOT NULL DEFAULT 0 COMMENT '状态（0-草稿，1-已提交，2-已审批，3-已驳回）',
    `approver_id` bigint COMMENT '审批人ID',
    `approver_name` varchar(50) COMMENT '审批人姓名',
    `approve_time` datetime COMMENT '审批时间',
    `approve_comment` text COMMENT '审批意见',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_report_work_report_type` (`type`),
    KEY `idx_report_work_report_report_date` (`report_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='工作报表表';

-- ============================================================
-- 2. 任务管理表
-- ============================================================
CREATE TABLE IF NOT EXISTS `report_task` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
    `title` varchar(200) NOT NULL COMMENT '任务标题',
    `description` text COMMENT '任务描述',
    `type` int NOT NULL DEFAULT 1 COMMENT '任务类型（1-日常任务，2-紧急任务，3-项目任务，4-临时任务）',
    `priority` int NOT NULL DEFAULT 2 COMMENT '优先级（1-低，2-中，3-高，4-紧急）',
    `status` int NOT NULL DEFAULT 0 COMMENT '状态（0-待分配，1-进行中，2-已完成，3-已取消）',
    `assignee_id` bigint COMMENT '负责人ID',
    `assignee_name` varchar(50) COMMENT '负责人姓名',
    `creator_id` bigint COMMENT '创建人ID',
    `creator_name` varchar(50) COMMENT '创建人姓名',
    `dept_id` bigint COMMENT '部门ID',
    `dept_name` varchar(100) COMMENT '部门名称',
    `start_date` date NOT NULL COMMENT '开始日期',
    `end_date` date NOT NULL COMMENT '截止日期',
    `complete_date` date COMMENT '完成日期',
    `progress` int NOT NULL DEFAULT 0 COMMENT '完成进度（0-100）',
    `remark` varchar(1000) COMMENT '备注',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_report_task_type` (`type`),
    KEY `idx_report_task_status` (`status`),
    KEY `idx_report_task_start_date` (`start_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务管理表';

-- ============================================================
-- 3. GoView 项目表
-- ============================================================
CREATE TABLE IF NOT EXISTS `report_go_view_project` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
    `name` varchar(200) NOT NULL COMMENT '项目名称',
    `pic_url` varchar(500) COMMENT '项目封面图片 URL',
    `content` longtext COMMENT '项目内容（JSON）',
    `status` tinyint NOT NULL DEFAULT 0 COMMENT '项目状态',
    `remark` varchar(500) COMMENT '备注',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='GoView 项目表';
