#!/bin/bash
# ============================================================
# 项目停止脚本
# 功能：优雅关闭后端(8080) -> 优雅关闭前端(5173) -> 验证
# ============================================================
set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

log_info()  { echo -e "${GREEN}[INFO]${NC}  $1"; }
log_warn()  { echo -e "${YELLOW}[WARN]${NC}  $1"; }
log_error() { echo -e "${RED}[ERROR]${NC} $1"; }

STOPPED_ANY=false

# ==================== 停止后端 ====================
log_info "停止后端服务..."

BACKEND_PID=$(lsof -t -i :8080 -sTCP:LISTEN 2>/dev/null || true)
if [ -n "$BACKEND_PID" ]; then
    # 使用 PID 文件辅助确认
    if [ -f /tmp/backend.pid ]; then
        PID_FILE_PID=$(cat /tmp/backend.pid)
        if kill -0 "$PID_FILE_PID" 2>/dev/null; then
            BACKEND_PID="$PID_FILE_PID"
            log_info "从 PID 文件确认后端进程: ${BACKEND_PID}"
        fi
    fi

    log_info "发送 SIGTERM 到后端进程 (PID: ${BACKEND_PID})..."
    kill "$BACKEND_PID" 2>/dev/null || true

    # 等待优雅关闭（最多 30 秒）
    SHUTDOWN_OK=false
    for i in $(seq 1 30); do
        if ! kill -0 "$BACKEND_PID" 2>/dev/null; then
            log_info "后端已优雅关闭"
            SHUTDOWN_OK=true
            STOPPED_ANY=true
            break
        fi
        sleep 1
    done

    if [ "$SHUTDOWN_OK" = false ]; then
        log_warn "后端未在 30 秒内退出，强制终止..."
        kill -9 "$BACKEND_PID" 2>/dev/null || true
        log_warn "后端已强制终止"
        STOPPED_ANY=true
    fi
else
    log_info "后端未运行，跳过"
fi

# ==================== 停止前端 ====================
log_info "停止前端服务..."

# 找到 vite 相关进程（父子进程一起处理）
VITE_PIDS=$(ps aux | grep -E "[v]ite.*mode env.local" | awk '{print $2}')
if [ -n "$VITE_PIDS" ]; then
    for pid in $VITE_PIDS; do
        log_info "发送 SIGTERM 到前端进程 (PID: ${pid})..."
        kill "$pid" 2>/dev/null || true
    done

    # 等待退出
    for i in $(seq 1 15); do
        REMAINING=$(ps aux | grep -E "[v]ite.*mode env.local" | awk '{print $2}' || true)
        if [ -z "$REMAINING" ]; then
            log_info "前端已优雅关闭"
            STOPPED_ANY=true
            break
        fi
        sleep 1
    done

    # 仍有残留则强制终止
    REMAINING=$(ps aux | grep -E "[v]ite.*mode env.local" | awk '{print $2}' || true)
    if [ -n "$REMAINING" ]; then
        log_warn "前端未完全退出，强制终止残留进程..."
        for pid in $REMAINING; do
            kill -9 "$pid" 2>/dev/null || true
        done
        STOPPED_ANY=true
    fi
else
    log_info "前端未运行，跳过"
fi

# ==================== 清理 PID 文件 ====================
rm -f /tmp/backend.pid /tmp/frontend.pid

# ==================== 验证 ====================
echo ""
log_info "验证端口释放..."

BACKEND_PORT_FREE=true
FRONTEND_PORT_FREE=true

if lsof -i :8080 -sTCP:LISTEN > /dev/null 2>&1; then
    log_warn "端口 8080 仍被占用"
    BACKEND_PORT_FREE=false
else
    log_info "端口 8080 已释放"
fi

if lsof -i :5173 -sTCP:LISTEN > /dev/null 2>&1; then
    log_warn "端口 5173 仍被占用"
    FRONTEND_PORT_FREE=false
else
    log_info "端口 5173 已释放"
fi

# ==================== 完成 ====================
echo ""
if [ "$STOPPED_ANY" = true ]; then
    echo -e "${GREEN}============================================${NC}"
    echo -e "${GREEN}         项目已停止${NC}"
    echo -e "${GREEN}============================================${NC}"
else
    echo -e "${YELLOW}============================================${NC}"
    echo -e "${YELLOW}         没有正在运行的服务${NC}"
    echo -e "${YELLOW}============================================${NC}"
fi

# 保留 Docker 中间件运行，不做任何操作
log_info "Docker 中间件保持运行，未受影响"
