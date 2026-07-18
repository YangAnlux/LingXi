#!/bin/bash
# MySQL 初始化脚本
# 按指定顺序执行 database 目录下的所有 SQL 文件

set -e

echo "Starting MySQL initialization..."

# 等待 MySQL 完全启动
until mysql -uroot -p"${MYSQL_ROOT_PASSWORD}" -e "SELECT 1"; do
    echo "Waiting for MySQL to be ready..."
    sleep 2
done

echo "MySQL is ready, executing SQL files..."

# SQL 文件目录
SQL_BASE_DIR="/docker-init-sql/base"
SQL_NEW_DIR="/docker-init-sql/new"

# 1. 先执行 quartz.sql
echo "Executing: quartz.sql"
mysql -uroot -p"${MYSQL_ROOT_PASSWORD}" "${MYSQL_DATABASE}" < "${SQL_BASE_DIR}/quartz.sql"

# 2. 再执行 ruoyi-vue-pro.sql
echo "Executing: ruoyi-vue-pro.sql"
mysql -uroot -p"${MYSQL_ROOT_PASSWORD}" "${MYSQL_DATABASE}" < "${SQL_BASE_DIR}/ruoyi-vue-pro.sql"

# 3. 执行 base 目录下其余 SQL 文件（按字母顺序，排除已执行的）
for sql_file in "${SQL_BASE_DIR}"/*.sql; do
    filename=$(basename "$sql_file")
    if [[ "$filename" != "quartz.sql" && "$filename" != "ruoyi-vue-pro.sql" ]]; then
        echo "Executing: ${filename}"
        mysql -uroot -p"${MYSQL_ROOT_PASSWORD}" "${MYSQL_DATABASE}" < "$sql_file"
    fi
done

# 4. 执行 new 目录下的 SQL 文件（按依赖顺序）
if [ -d "$SQL_NEW_DIR" ]; then
    # 4.1 先执行 new-i18n.sql（创建基础表 system_menu_i18n）
    if [ -f "${SQL_NEW_DIR}/new-i18n.sql" ]; then
        echo "Executing: new-i18n.sql"
        mysql -uroot -p"${MYSQL_ROOT_PASSWORD}" "${MYSQL_DATABASE}" < "${SQL_NEW_DIR}/new-i18n.sql"
    fi

    # 4.2 再执行 new-mall-i18n.sql（创建 promotion_diy_menu_i18n 表，修改 system_tenant 表添加 currency_code）
    if [ -f "${SQL_NEW_DIR}/new-mall-i18n.sql" ]; then
        echo "Executing: new-mall-i18n.sql"
        mysql -uroot -p"${MYSQL_ROOT_PASSWORD}" "${MYSQL_DATABASE}" < "${SQL_NEW_DIR}/new-mall-i18n.sql"
    fi

    # 4.3 执行 new-i18n-ar.sql（插入多语言数据，依赖 system_menu_i18n 表存在）
    if [ -f "${SQL_NEW_DIR}/new-i18n-ar.sql" ]; then
        echo "Executing: new-i18n-ar.sql"
        mysql -uroot -p"${MYSQL_ROOT_PASSWORD}" "${MYSQL_DATABASE}" < "${SQL_NEW_DIR}/new-i18n-ar.sql"
    fi

    # 4.4 执行 new-product-category-i18n.sql
    if [ -f "${SQL_NEW_DIR}/new-product-category-i18n.sql" ]; then
        echo "Executing: new-product-category-i18n.sql"
        mysql -uroot -p"${MYSQL_ROOT_PASSWORD}" "${MYSQL_DATABASE}" < "${SQL_NEW_DIR}/new-product-category-i18n.sql"
    fi

    # 4.5 执行 new-large-file-upload.sql
    if [ -f "${SQL_NEW_DIR}/new-large-file-upload.sql" ]; then
        echo "Executing: new-large-file-upload.sql"
        mysql -uroot -p"${MYSQL_ROOT_PASSWORD}" "${MYSQL_DATABASE}" < "${SQL_NEW_DIR}/new-large-file-upload.sql"
    fi

    # [ADD START] 执行 CRM 业务模块 SQL 文件 - 2026-07-18 - 23软4胡伟-202305566535-修改于2026.07.17
    # 固定顺序执行，保证依赖关系（如字典表依赖基础表）
    for crm_sql in \
        "crm_expense_init.sql" \
        "crm_invoice_init.sql" \
        "crm_reimbursement_init.sql" \
        "crm_refund_init.sql" \
        "crm_receivable_overdue_notify_init.sql" \
        "crm_bpmn_process_init.sql" \
        "patch_2026-07-17_add_invoice_menu_ar_i18n.sql"; do
        if [ -f "${SQL_NEW_DIR}/${crm_sql}" ]; then
            echo "Executing: ${crm_sql}"
            mysql -uroot -p"${MYSQL_ROOT_PASSWORD}" "${MYSQL_DATABASE}" < "${SQL_NEW_DIR}/${crm_sql}"
        fi
    done
    # [ADD END] CRM SQL 文件 - 2026-07-18 - 23软4胡伟-202305566535-修改于2026.07.17
fi

echo "MySQL initialization completed successfully!"