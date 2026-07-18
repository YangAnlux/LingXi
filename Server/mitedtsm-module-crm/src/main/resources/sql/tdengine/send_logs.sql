-- =============================================
-- CRM 发送日志 TDengine 时序数据库
-- 需要在 TDengine 容器中手动执行或通过 InitRunner 自动创建
-- =============================================

CREATE DATABASE IF NOT EXISTS crm_logs KEEP 365 DAYS 10;
USE crm_logs;

-- 超级表：每条发送记录对应一条子表数据
-- TAGS: customer_id（客户编号）, task_id（任务编号）, biz_type（业务类型）
CREATE STABLE IF NOT EXISTS send_logs (
    ts TIMESTAMP,
    send_status VARCHAR(20),    -- SUCCESS/FAIL/OPEN/CLICK
    channel VARCHAR(10),        -- SMS/EMAIL
    fail_reason VARCHAR(500),
    template_code VARCHAR(100)
) TAGS (
    customer_id BIGINT,
    task_id BIGINT,
    biz_type VARCHAR(20)        -- MARKETING/AUTO_CARE
);
