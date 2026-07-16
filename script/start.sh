#!/bin/bash
# ============================================================
# 项目启动脚本
# 功能：检查环境 -> 启动后端(8080) -> 启动前端(5173) -> 验证
# ============================================================
set -e

PROJECT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
SERVER_DIR="${PROJECT_DIR}/Server"
WEB_DIR="${PROJECT_DIR}/Web"
BACKEND_LOG="/tmp/backend.log"
FRONTEND_LOG="/tmp/frontend.log"
BACKEND_PORT=8080
FRONTEND_PORT=5173

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

log_info()  { echo -e "${GREEN}[INFO]${NC}  $1"; }
log_warn()  { echo -e "${YELLOW}[WARN]${NC}  $1"; }
log_error() { echo -e "${RED}[ERROR]${NC} $1"; }

STARTED_BACKEND=false
STARTED_FRONTEND=false

# 清理函数：仅在脚本中途失败且已启动服务时，帮助停止
cleanup_on_failure() {
    echo ""
    log_warn "启动过程中断，清理已启动的服务..."
    if [ "$STARTED_FRONTEND" = true ]; then
        log_info "关闭前端..."
        kill "$FRONTEND_PID" 2>/dev/null || true
    fi
    if [ "$STARTED_BACKEND" = true ]; then
        log_info "关闭后端..."
        kill "$BACKEND_PID" 2>/dev/null || true
    fi
}

# ==================== 1. 检查 Docker 中间件 ====================
log_info "检查 Docker 中间件..."

check_docker_container() {
    local name=$1
    local status
    status=$(docker inspect -f '{{.State.Status}}' "$name" 2>/dev/null || echo "not_found")
    if [ "$status" != "running" ]; then
        log_error "Docker 容器 ${name} 未运行 (状态: ${status})"
        return 1
    fi
    return 0
}

# 必须的中间件
check_docker_container "mitedtsm-dev-mysql"   || exit 1
check_docker_container "mitedtsm-dev-redis"   || exit 1
check_docker_container "mitedtsm-dev-rabbitmq" || log_warn "RabbitMQ 未运行，非核心中间件"

# TDengine 健康检查 & 自动修复
log_info "检查 TDengine..."
if docker inspect -f '{{.State.Health.Status}}' mitedtsm-dev-tdengine 2>/dev/null | grep -q "healthy"; then
    log_info "TDengine 状态: healthy"
else
    log_warn "TDengine 不健康，尝试重启..."
    docker restart mitedtsm-dev-tdengine > /dev/null 2>&1
    sleep 8
    if docker exec mitedtsm-dev-tdengine taos -s "SELECT 1" > /dev/null 2>&1; then
        log_info "TDengine 已恢复"
    else
        log_error "TDengine 恢复失败"
        exit 1
    fi
fi

# ==================== 2. 检查端口是否已被占用 ====================
check_port() {
    local port=$1
    if lsof -i :"${port}" -sTCP:LISTEN > /dev/null 2>&1; then
        log_warn "端口 ${port} 已被占用，进程信息:"
        lsof -i :"${port}" -sTCP:LISTEN
        return 1
    fi
    return 0
}

check_port ${BACKEND_PORT} || exit 1
check_port ${FRONTEND_PORT} || exit 1

# ==================== 3. 启动后端 ====================
log_info "启动后端服务..."

BACKEND_JAR="${SERVER_DIR}/mitedtsm-server/target/mitedtsm-server.jar"

if [ ! -f "$BACKEND_JAR" ]; then
    log_warn "JAR 包不存在，开始编译..."
    cd "$SERVER_DIR"
    mvn clean package -DskipTests -q
    if [ ! -f "$BACKEND_JAR" ]; then
        log_error "编译失败，JAR 包未生成"
        exit 1
    fi
    log_info "编译完成"
fi

cd "$SERVER_DIR"
nohup java -jar "$BACKEND_JAR" --spring.profiles.active=local > "$BACKEND_LOG" 2>&1 &
BACKEND_PID=$!
STARTED_BACKEND=true
trap cleanup_on_failure EXIT
echo "$BACKEND_PID" > /tmp/backend.pid
log_info "后端进程已启动 (PID: ${BACKEND_PID})，等待服务就绪..."

# 轮询等待后端启动（最多等待 120 秒）
for i in $(seq 1 60); do
    if curl -s http://localhost:${BACKEND_PORT}/actuator/health 2>/dev/null | grep -q "UP"; then
        log_info "后端启动成功！端口: ${BACKEND_PORT}"
        break
    fi
    if [ $i -eq 60 ]; then
        log_error "后端启动超时，请查看日志: ${BACKEND_LOG}"
        exit 1
    fi
    sleep 2
done

# ==================== 4. 启动前端 ====================
log_info "启动前端服务..."

cd "$WEB_DIR"

# 检查依赖
if [ ! -d "node_modules" ]; then
    log_warn "node_modules 不存在，安装依赖..."
    npm install --silent
fi

# 使用 VITE_PORT 环境变量覆盖 .env 中的 80 端口，避免 root 权限问题
VITE_PORT=${FRONTEND_PORT} nohup npm run dev > "$FRONTEND_LOG" 2>&1 &
FRONTEND_PID=$!
STARTED_FRONTEND=true
echo "$FRONTEND_PID" > /tmp/frontend.pid
log_info "前端进程已启动 (PID: ${FRONTEND_PID})，等待服务就绪..."

# 轮询等待前端启动（最多等待 30 秒）
for i in $(seq 1 15); do
    if curl -s -o /dev/null -w "%{http_code}" http://localhost:${FRONTEND_PORT}/ 2>/dev/null | grep -q "200"; then
        log_info "前端启动成功！端口: ${FRONTEND_PORT}"
        break
    fi
    if [ $i -eq 15 ]; then
        log_error "前端启动超时，请查看日志: ${FRONTEND_LOG}"
        exit 1
    fi
    sleep 2
done

# 全部成功，移除 trap
trap - EXIT

# ==================== 5. 启动完成 ====================
echo ""
echo -e "${GREEN}============================================${NC}"
echo -e "${GREEN}         项目启动完成！${NC}"
echo -e "${GREEN}============================================${NC}"
echo -e "  后端 API:  http://localhost:${BACKEND_PORT}"
echo -e "  前端页面:  http://localhost:${FRONTEND_PORT}"
echo -e "  健康检查:  http://localhost:${BACKEND_PORT}/actuator/health"
echo -e "  后端日志:  ${BACKEND_LOG}"
echo -e "  前端日志:  ${FRONTEND_LOG}"
echo -e "${GREEN}============================================${NC}"
