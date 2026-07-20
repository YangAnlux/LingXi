-- 23软2张奎良-202305566305
SELECT id, name, path FROM mitedtsm_database.system_menu 
WHERE name LIKE '%工作报表%' OR name LIKE '%任务%' OR name LIKE '%发票%' OR name LIKE '%费用%' OR name LIKE '%报销%' OR name LIKE '%退款%' OR name LIKE '%工单%' OR name LIKE '%活动%' OR name LIKE '%商机%' OR name LIKE '%门票%' OR name LIKE '% Ticket%'
ORDER BY id;
