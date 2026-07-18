#!/bin/bash
# env_check.sh - MITEDTSM 开发环境验证脚本

echo "========== MITEDTSM 环境验证 =========="
echo ""

echo "--- 基础工具 ---"
echo -n "Java:    "; java -version 2>&1 | head -1
echo -n "Maven:   "; mvn -version 2>&1 | head -1
echo -n "Node:    "; node -v
echo -n "npm:     "; npm -v
echo -n "pnpm:    "; pnpm -v 2>/dev/null || echo "未安装"
echo -n "MySQL:   "; mysql --version 2>/dev/null || echo "未安装 (使用Docker)"
echo -n "Redis:   "; redis-cli --version 2>/dev/null || echo "未安装 (使用Docker)"
echo -n "Docker:  "; docker --version
echo -n "Compose: "; docker compose version
echo -n "Git:     "; git --version

echo ""
echo "--- Docker 服务状态 ---"
docker ps --format "table {{.Names}}\t{{.Image}}\t{{.Status}}" 2>/dev/null | grep -E "mitedtsm|NAMES" || echo "无运行中的 mitedtsm 容器"

echo ""
echo "--- 网络连通性 ---"
ping -c 1 -W 2 mirrors.aliyun.com > /dev/null 2>&1 && echo "APT镜像 (aliyun)  : OK" || echo "APT镜像 (aliyun)  : FAIL"
ping -c 1 -W 2 gitee.com > /dev/null 2>&1 && echo "Gitee             : OK" || echo "Gitee             : FAIL"
ping -c 1 -W 2 registry.npmmirror.com > /dev/null 2>&1 && echo "NPM镜像 (npmmirror): OK" || echo "NPM镜像 (npmmirror): FAIL"
curl -s --connect-timeout 3 https://maven.aliyun.com > /dev/null 2>&1 && echo "Maven镜像 (aliyun) : OK" || echo "Maven镜像 (aliyun) : FAIL"

echo ""
echo "========== 验证完成 =========="
