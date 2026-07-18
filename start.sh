#!/bin/bash
# ============================================
# 密讯CRM 项目 - 一键启动脚本
# ============================================
set -e

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
echo "========================================"
echo "  密讯CRM 项目启动"
echo "  $PROJECT_DIR"
echo "========================================"

# 1. 启动 Docker 中间件
echo "[1/4] 启动 Docker 中间件..."
cd "$PROJECT_DIR/dev"
docker compose up -d
echo "  等待 MySQL 就绪..."
until docker exec mitedtsm-dev-mysql mysqladmin ping -h localhost --silent 2>/dev/null; do
  sleep 2
done
echo "  ✅ MySQL / Redis / RabbitMQ 已就绪"

# 2. 启动后端 (后台)
echo "[2/4] 启动后端 Spring Boot..."
cd "$PROJECT_DIR/Server"
JAVA_TOOL_OPTIONS="-Xms512m -Xmx1536m -XX:+UseG1GC -XX:MaxGCPauseMillis=200" \
  nohup mvn spring-boot:run -pl mitedtsm-server -Dspring.profiles.active=local -Dmaven.test.skip=true \
  > /tmp/mitedtsm-backend.log 2>&1 &
echo "  后端 PID: $! (日志: /tmp/mitedtsm-backend.log)"

# 3. 启动前端 (后台)
echo "[3/4] 启动前端 Vite..."
cd "$PROJECT_DIR/Web"
nohup pnpm dev > /tmp/mitedtsm-frontend.log 2>&1 &
echo "  前端 PID: $! (日志: /tmp/mitedtsm-frontend.log)"

# 4. 等待后端就绪
echo "[4/4] 等待后端启动 (约60秒)..."
for i in $(seq 1 60); do
  if curl -s -o /dev/null http://localhost:8080/admin-api/system/captcha/get 2>/dev/null; then
    echo "  ✅ 后端已就绪!"
    break
  fi
  sleep 2
done

echo ""
echo "========================================"
echo "  启动完成!"
echo "  前端: http://localhost:5173/"
echo "  后端: http://localhost:8080/"
echo "========================================"
