// 2023级软4蔡磊202305566515,2026年7月18日
// [ADD START] 工单管理完整初始化（建表 + 菜单 + 权限分配） - 2026-07-18 - 23软4蔡磊202305566515
package com.meession.etm.module.crm.framework.tableinit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * 工单管理模块完整初始化器
 * 启动时自动执行建表、菜单注册、角色权限分配。
 * 无需手动执行 SQL 脚本，别人拉代码即可开箱即用。
 *
 * @author 23软4蔡磊202305566515
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Order(1)
public class CrmWorkOrderTableInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    // ======================== 建表 DDL ========================
    private static final String SQL_CREATE_WORK_ORDER =
            "CREATE TABLE `crm_work_order` (\n" +
            "  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
            "  `title` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '工单标题',\n" +
            "  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '工单类型',\n" +
            "  `priority` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'NORMAL' COMMENT '优先级',\n" +
            "  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '状态',\n" +
            "  `customer_id` bigint DEFAULT NULL COMMENT '关联客户编号',\n" +
            "  `assignee_id` bigint DEFAULT NULL COMMENT '处理人编号',\n" +
            "  `sla_deadline` datetime DEFAULT NULL COMMENT 'SLA 截止时间',\n" +
            "  `is_sla_breached` tinyint DEFAULT '0' COMMENT '是否超SLA',\n" +
            "  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '工单内容',\n" +
            "  `solution` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '解决方案',\n" +
            "  `resolved_time` datetime DEFAULT NULL COMMENT '完结时间',\n" +
            "  `satisfaction_score` tinyint DEFAULT NULL COMMENT '满意度评分',\n" +
            "  `satisfaction_comment` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '满意度评价',\n" +
            "  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',\n" +
            "  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
            "  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',\n" +
            "  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',\n" +
            "  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',\n" +
            "  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',\n" +
            "  PRIMARY KEY (`id`) USING BTREE,\n" +
            "  KEY `idx_customer_id` (`customer_id`) USING BTREE,\n" +
            "  KEY `idx_assignee_id` (`assignee_id`) USING BTREE,\n" +
            "  KEY `idx_status` (`status`) USING BTREE\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CRM 工单表'";

    private static final String SQL_CREATE_WORK_ORDER_RECORD =
            "CREATE TABLE `crm_work_order_record` (\n" +
            "  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',\n" +
            "  `work_order_id` bigint NOT NULL COMMENT '关联工单编号',\n" +
            "  `from_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '变更前状态',\n" +
            "  `to_status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '变更后状态',\n" +
            "  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '创建者',\n" +
            "  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
            "  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '更新者',\n" +
            "  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',\n" +
            "  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',\n" +
            "  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',\n" +
            "  PRIMARY KEY (`id`) USING BTREE,\n" +
            "  KEY `idx_work_order_id` (`work_order_id`) USING BTREE\n" +
            ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='CRM 工单处理记录表'";

    @Override
    public void run(String... args) {
        // 1. 建表
        initTable("crm_work_order", SQL_CREATE_WORK_ORDER);
        initTable("crm_work_order_record", SQL_CREATE_WORK_ORDER_RECORD);

        // 2. 菜单注册
        initMenus();

        // 3. 角色权限分配
        initRoleMenus();

        // 4. 菜单国际化
        initMenuI18n();

        log.info("[CrmWorkOrderTableInitializer] 工单管理模块完整初始化完成");
    }

    // ======================== 建表 ========================
    private void initTable(String tableName, String createSql) {
        try {
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM information_schema.TABLES " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = ?",
                    Integer.class, tableName);
            if (count != null && count > 0) {
                log.info("[CrmWorkOrderTableInitializer] 表 {} 已存在，跳过", tableName);
                return;
            }
            jdbcTemplate.execute(createSql);
            log.info("[CrmWorkOrderTableInitializer] 表 {} 创建成功", tableName);
        } catch (Exception e) {
            log.warn("[CrmWorkOrderTableInitializer] 建表 {} 异常: {}", tableName, e.getMessage());
        }
    }

    // ======================== 菜单注册 ========================
    private void initMenus() {
        insertMenuIfNotExists(6100, "工单管理", "", 2, 77, 2397, "workorder", "ep:tickets",
                "crm/workorder/index", "CrmWorkOrder", 0);
        insertMenuIfNotExists(6101, "工单管理查询", "crm:work-order:query", 3, 1, 6100,
                "", "", "", null, 0);
        insertMenuIfNotExists(6102, "工单管理创建", "crm:work-order:create", 3, 2, 6100,
                "", "", "", null, 0);
        insertMenuIfNotExists(6103, "工单管理更新", "crm:work-order:update", 3, 3, 6100,
                "", "", "", null, 0);
        insertMenuIfNotExists(6104, "工单管理删除", "crm:work-order:delete", 3, 4, 6100,
                "", "", "", null, 0);
        insertMenuIfNotExists(6105, "工单管理分配", "crm:work-order:assign", 3, 5, 6100,
                "", "", "", null, 0);
        insertMenuIfNotExists(6106, "工单管理导出", "crm:work-order:export", 3, 6, 6100,
                "", "", "", null, 0);
    }

    private void insertMenuIfNotExists(int id, String name, String permission, int type, int sort,
                                       int parentId, String path, String icon,
                                       String component, String componentName, int status) {
        try {
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM system_menu WHERE id = ?", Integer.class, id);
            if (count != null && count > 0) {
                return;
            }
            jdbcTemplate.update("INSERT INTO system_menu " +
                    "(id, name, permission, type, sort, parent_id, path, icon, component, component_name, " +
                    " status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1, 1, 1, '1', NOW(), '1', NOW(), 0)",
                    id, name, permission, type, sort, parentId, path, icon, component,
                    componentName, status);
            log.info("[CrmWorkOrderTableInitializer] 菜单注册成功: id={}, name={}", id, name);
        } catch (Exception e) {
            log.warn("[CrmWorkOrderTableInitializer] 菜单 {} 注册异常: {}", id, e.getMessage());
        }
    }

    // ======================== 角色权限分配 ========================
    private void initRoleMenus() {
        // 给超级管理员(role_id=1)分配工单管理所有权限
        int[] menuIds = {6100, 6101, 6102, 6103, 6104, 6105, 6106};
        for (int menuId : menuIds) {
            insertRoleMenuIfNotExists(1, menuId);
        }
    }

    private void insertRoleMenuIfNotExists(int roleId, int menuId) {
        try {
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM system_role_menu WHERE role_id = ? AND menu_id = ?",
                    Integer.class, roleId, menuId);
            if (count != null && count > 0) {
                return;
            }
            jdbcTemplate.update("INSERT INTO system_role_menu (role_id, menu_id) VALUES (?, ?)",
                    roleId, menuId);
            log.info("[CrmWorkOrderTableInitializer] 权限分配成功: role_id={}, menu_id={}", roleId, menuId);
        } catch (Exception e) {
            log.warn("[CrmWorkOrderTableInitializer] 权限分配异常: role_id={}, menu_id={}, msg={}",
                    roleId, menuId, e.getMessage());
        }
    }

    // ======================== 菜单国际化 ========================
    private void initMenuI18n() {
        insertMenuI18nIfNotExists(6100, "zh-CN", "工单管理");
        insertMenuI18nIfNotExists(6100, "en", "Work Order");
    }

    private void insertMenuI18nIfNotExists(int menuId, String language, String name) {
        try {
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM system_menu_i18n WHERE menu_id = ? AND language = ?",
                    Integer.class, menuId, language);
            if (count != null && count > 0) {
                return;
            }
            jdbcTemplate.update("INSERT INTO system_menu_i18n (menu_id, language, name) VALUES (?, ?, ?)",
                    menuId, language, name);
            log.info("[CrmWorkOrderTableInitializer] 菜单国际化成功: menu_id={}, lang={}", menuId, language);
        } catch (Exception e) {
            log.warn("[CrmWorkOrderTableInitializer] 菜单国际化异常: menu_id={}, lang={}, msg={}",
                    menuId, language, e.getMessage());
        }
    }
}
// [ADD END] 工单管理完整初始化（建表 + 菜单 + 权限分配） - 2026-07-18 - 23软4蔡磊202305566515
