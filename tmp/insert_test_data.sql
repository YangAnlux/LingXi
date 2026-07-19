SET NAMES utf8mb4;

-- 商机管理
INSERT INTO crm_business (name, customer_id, owner_user_id, status_type_id, status_id, total_product_price, discount_percent, total_price, remark, creator, updater, tenant_id)
VALUES ('测试商机-数据验证001', 12, 1, 6, 9, 10000.00, 100.00, 10000.00, '自动化测试数据', 'admin', 'admin', 1);

-- 营销活动
INSERT INTO crm_campaign (name, status, start_time, end_time, budget, actual_cost, channel, description, owner_user_id, remark, tenant_id, create_user, update_user)
VALUES ('测试营销活动-数据验证001', 1, NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY), 5000.00, 0.00, 1, '自动化测试数据', 1, '测试备注', 1, 1, 1);

-- 发票管理
INSERT INTO crm_invoice (no, customer_id, owner_user_id, invoice_no, type, amount, title, tax_no, status, issue_date, remark, creator, updater, tenant_id)
VALUES ('INV-TEST-001', 12, 1, 'INVOICE-TEST-001', 1, 1000.00, '测试发票抬头', '91310000TEST001', 1, CURDATE(), '自动化测试数据', 'admin', 'admin', 1);

-- 费用管理
INSERT INTO crm_expense (no, customer_id, owner_user_id, type, amount, expense_date, remark, creator, updater, tenant_id)
VALUES ('EXP-TEST-001', 12, 1, 1, 500.00, CURDATE(), '自动化测试数据', 'admin', 'admin', 1);

-- 报销管理
INSERT INTO crm_reimbursement (no, customer_id, owner_user_id, reimbursement_date, total_amount, type, status, remark, creator, updater, tenant_id)
VALUES ('RMB-TEST-001', 12, 1, CURDATE(), 800.00, 1, 0, '自动化测试数据', 'admin', 'admin', 1);

-- 退款管理
INSERT INTO crm_refund (no, customer_id, owner_user_id, refund_amount, refund_date, refund_reason, refund_type, remark, creator, updater, tenant_id)
VALUES ('REF-TEST-001', 12, 1, 2000.00, CURDATE(), '客户申请退款', 1, '自动化测试数据', 'admin', 'admin', 1);

-- 工单管理
INSERT INTO crm_work_order (title, type, priority, status, customer_id, assignee_id, content, creator, updater, tenant_id)
VALUES ('测试工单-数据验证001', 'BUG', 'HIGH', 'PENDING', 12, 1, '自动化测试数据：验证工单管理功能', 'admin', 'admin', 1);

-- 任务管理
INSERT INTO report_task (tenant_id, title, description, type, priority, status, assignee_id, assignee_name, creator_id, creator_name, dept_id, dept_name, start_date, end_date, progress, remark, creator, updater)
VALUES (1, '测试任务-数据验证001', '自动化测试数据', 1, 2, 0, 1, 'admin', 1, 'admin', 1, '研发部', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 7 DAY), 0, '测试备注', 'admin', 'admin');

-- 工作报表
INSERT INTO report_work_report (tenant_id, type, title, report_date, end_date, content, achievements, problems, plan, reporter_id, reporter_name, dept_id, dept_name, status, creator, updater)
VALUES (1, 1, '测试工作报表-数据验证001', CURDATE(), CURDATE(), '自动化测试数据', '完成测试数据录入', '暂无问题', '继续验证其他模块', 1, 'admin', 1, '研发部', 0, 'admin', 'admin');
