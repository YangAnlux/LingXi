CREATE TABLE IF NOT EXISTS `bpm_process_comment` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '编号',
  `tenant_id` BIGINT NOT NULL DEFAULT 0 COMMENT '租户编号',
  `process_instance_id` VARCHAR(64) NOT NULL COMMENT '流程实例编号',
  `task_id` VARCHAR(64) COMMENT '任务编号',
  `user_id` BIGINT NOT NULL COMMENT '用户编号',
  `content` TEXT NOT NULL COMMENT '评论内容',
  `creator` VARCHAR(64) DEFAULT '' COMMENT '创建人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` VARCHAR(64) DEFAULT '' COMMENT '更新人',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  INDEX `idx_process_instance_id` (`process_instance_id`),
  INDEX `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='流程评论表';