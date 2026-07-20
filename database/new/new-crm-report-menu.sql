-- 23软2张奎良-202305566305
-- 为 CRM 菜单补充【任务管理】【任务看板】【工作报表】三个模块的菜单及按钮权限
-- 说明：后端代码位于 mitedtsm-module-report，前端页面位于 src/views/report/*，
--       但业务上需要在 CRM 系统菜单下展示，因此将菜单挂载到 CRM 系统（parent_id=2397）下。

-- ============================================================
-- 1. 插入 CRM 子菜单（type=2 菜单）
-- ============================================================

-- 任务管理（id=6004）
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `updater`)
VALUES (6004, '任务管理', '', 2, 90, 2397, 'task', 'fa-solid:tasks', 'report/task/index', 'Task', 0, b'1', b'1', b'1', '1', '1');

-- 任务看板（id=6005）
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `updater`)
VALUES (6005, '任务看板', '', 2, 91, 2397, 'task-board', 'fa-solid:columns', 'report/task/board', 'TaskBoard', 0, b'1', b'1', b'1', '1', '1');

-- 工作报表（id=6006）
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `updater`)
VALUES (6006, '工作报表', '', 2, 92, 2397, 'work-report', 'fa-solid:file-lines', 'report/workreport/index', 'WorkReport', 0, b'1', b'1', b'1', '1', '1');

-- ============================================================
-- 2. 插入任务管理按钮权限（type=3 按钮）
-- ============================================================
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `updater`)
VALUES
  (6007, '任务查询', 'report:task:query',   3, 1, 6004, '', '', '', '', 0, b'1', b'1', b'1', '1', '1'),
  (6008, '任务创建', 'report:task:create',  3, 2, 6004, '', '', '', '', 0, b'1', b'1', b'1', '1', '1'),
  (6009, '任务更新', 'report:task:update',  3, 3, 6004, '', '', '', '', 0, b'1', b'1', b'1', '1', '1'),
  (6010, '任务删除', 'report:task:delete',  3, 4, 6004, '', '', '', '', 0, b'1', b'1', b'1', '1', '1'),
  (6011, '任务分配', 'report:task:assign',  3, 5, 6004, '', '', '', '', 0, b'1', b'1', b'1', '1', '1'),
  (6012, '任务开始', 'report:task:start',   3, 6, 6004, '', '', '', '', 0, b'1', b'1', b'1', '1', '1'),
  (6013, '任务完成', 'report:task:complete',3, 7, 6004, '', '', '', '', 0, b'1', b'1', b'1', '1', '1'),
  (6014, '任务取消', 'report:task:cancel',  3, 8, 6004, '', '', '', '', 0, b'1', b'1', b'1', '1', '1');

-- ============================================================
-- 3. 插入任务看板按钮权限（type=3 按钮）
-- ============================================================
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `updater`)
VALUES (6015, '任务看板查询', 'report:task-board:query', 3, 1, 6005, '', '', '', '', 0, b'1', b'1', b'1', '1', '1');

-- ============================================================
-- 4. 插入工作报表按钮权限（type=3 按钮）
-- ============================================================
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `updater`)
VALUES
  (6016, '工作报表查询', 'report:work-report:query',   3, 1, 6006, '', '', '', '', 0, b'1', b'1', b'1', '1', '1'),
  (6017, '工作报表创建', 'report:work-report:create',  3, 2, 6006, '', '', '', '', 0, b'1', b'1', b'1', '1', '1'),
  (6018, '工作报表更新', 'report:work-report:update',  3, 3, 6006, '', '', '', '', 0, b'1', b'1', b'1', '1', '1'),
  (6019, '工作报表删除', 'report:work-report:delete',  3, 4, 6006, '', '', '', '', 0, b'1', b'1', b'1', '1', '1'),
  (6020, '工作报表提交', 'report:work-report:submit',  3, 5, 6006, '', '', '', '', 0, b'1', b'1', b'1', '1', '1'),
  (6021, '工作报表审批', 'report:work-report:approve', 3, 6, 6006, '', '', '', '', 0, b'1', b'1', b'1', '1', '1');
