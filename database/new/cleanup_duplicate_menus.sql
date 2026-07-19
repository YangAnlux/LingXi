-- 23软2张奎良-202305566305
-- 清理 CRM 系统中重复的菜单项
-- 问题：工作报表、任务管理、任务看板、退款管理在菜单中出现两次

-- ============================================================
-- 1. 删除旧的重复父菜单及其子菜单
-- ============================================================

-- 删除旧的工作报表菜单（id=6008）及其按钮权限
DELETE FROM system_menu WHERE parent_id = 6008;
DELETE FROM system_menu WHERE id = 6008;

-- 删除旧的任务管理菜单（id=6009）及其按钮权限
DELETE FROM system_menu WHERE parent_id = 6009;
DELETE FROM system_menu WHERE id = 6009;

-- 删除旧的任务看板菜单（id=6018）及其按钮权限
DELETE FROM system_menu WHERE parent_id = 6018;
DELETE FROM system_menu WHERE id = 6018;

-- 删除旧的退款管理菜单（id=5069）及其按钮权限，保留新的 6030
DELETE FROM system_menu WHERE parent_id = 5069;
DELETE FROM system_menu WHERE id = 5069;

-- ============================================================
-- 2. 清理角色菜单关联表中的脏数据
-- ============================================================

-- 删除已不存在的菜单关联
DELETE FROM system_role_menu WHERE menu_id IN (5069, 5070, 5071, 5072, 5073, 6008, 6009, 6010, 6011, 6012, 6013, 6014, 6015, 6016, 6017, 6018, 6019);

-- 去重新复 role 1 与 6004/6005/6006 的关联
DELETE FROM system_role_menu WHERE role_id = 1 AND menu_id IN (6004, 6005, 6006);
INSERT INTO system_role_menu (role_id, menu_id) VALUES (1, 6004), (1, 6005), (1, 6006);

-- ============================================================
-- 3. 为新菜单补充完整的按钮权限
-- ============================================================

-- 任务管理（parent_id=6004）
INSERT INTO system_menu (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `updater`)
VALUES
  (6107, '任务查询', 'report:task:query',   3, 1, 6004, '', '', '', '', 0, b'1', b'1', b'1', '1', '1'),
  (6108, '任务创建', 'report:task:create',  3, 2, 6004, '', '', '', '', 0, b'1', b'1', b'1', '1', '1'),
  (6109, '任务更新', 'report:task:update',  3, 3, 6004, '', '', '', '', 0, b'1', b'1', b'1', '1', '1'),
  (6110, '任务删除', 'report:task:delete',  3, 4, 6004, '', '', '', '', 0, b'1', b'1', b'1', '1', '1'),
  (6111, '任务分配', 'report:task:assign',  3, 5, 6004, '', '', '', '', 0, b'1', b'1', b'1', '1', '1'),
  (6112, '任务开始', 'report:task:start',   3, 6, 6004, '', '', '', '', 0, b'1', b'1', b'1', '1', '1'),
  (6113, '任务完成', 'report:task:complete',3, 7, 6004, '', '', '', '', 0, b'1', b'1', b'1', '1', '1'),
  (6114, '任务取消', 'report:task:cancel',  3, 8, 6004, '', '', '', '', 0, b'1', b'1', b'1', '1', '1');

-- 任务看板（parent_id=6005）
INSERT INTO system_menu (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `updater`)
VALUES (6115, '任务看板查询', 'report:task-board:query', 3, 1, 6005, '', '', '', '', 0, b'1', b'1', b'1', '1', '1');

-- 工作报表（parent_id=6006）
INSERT INTO system_menu (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `updater`)
VALUES
  (6116, '工作报表查询', 'report:work-report:query',   3, 1, 6006, '', '', '', '', 0, b'1', b'1', b'1', '1', '1'),
  (6117, '工作报表创建', 'report:work-report:create',  3, 2, 6006, '', '', '', '', 0, b'1', b'1', b'1', '1', '1'),
  (6118, '工作报表更新', 'report:work-report:update',  3, 3, 6006, '', '', '', '', 0, b'1', b'1', b'1', '1', '1'),
  (6119, '工作报表删除', 'report:work-report:delete',  3, 4, 6006, '', '', '', '', 0, b'1', b'1', b'1', '1', '1'),
  (6120, '工作报表提交', 'report:work-report:submit',  3, 5, 6006, '', '', '', '', 0, b'1', b'1', b'1', '1', '1'),
  (6121, '工作报表审批', 'report:work-report:approve', 3, 6, 6006, '', '', '', '', 0, b'1', b'1', b'1', '1', '1');

-- ============================================================
-- 4. 将新按钮权限分配给超级管理员角色
-- ============================================================

INSERT INTO system_role_menu (role_id, menu_id)
VALUES
  (1, 6107), (1, 6108), (1, 6109), (1, 6110), (1, 6111), (1, 6112), (1, 6113), (1, 6114),
  (1, 6115),
  (1, 6116), (1, 6117), (1, 6118), (1, 6119), (1, 6120), (1, 6121);
