-- 2023级软4蔡磊202305566515,2026年7月14日
-- ----------------------------
-- Table structure for crm_work_order (工单表)
-- ----------------------------
DROP TABLE IF EXISTS `crm_work_order`;
CREATE TABLE `crm_work_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '工单标题',
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '工单类型（字典）',
  `priority` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'NORMAL' COMMENT '优先级（LOW/NORMAL/HIGH/URGENT）',
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '状态（待处理/处理中/已完结/已退回）',
  `customer_id` bigint DEFAULT NULL COMMENT '关联客户编号',
  `assignee_id` bigint DEFAULT NULL COMMENT '处理人编号',
  `sla_deadline` datetime DEFAULT NULL COMMENT 'SLA 截止时间',
  `is_sla_breached` tinyint DEFAULT '0' COMMENT '是否超SLA（0=否 1=是）',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '工单内容',
  `solution` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '解决方案',
  `resolved_time` datetime DEFAULT NULL COMMENT '完结时间',
  `satisfaction_score` tinyint DEFAULT NULL COMMENT '满意度评分（1-5）',
  `satisfaction_comment` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '满意度评价内容',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_customer_id` (`customer_id`) USING BTREE,
  KEY `idx_assignee_id` (`assignee_id`) USING BTREE,
  KEY `idx_status` (`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CRM 工单表';
