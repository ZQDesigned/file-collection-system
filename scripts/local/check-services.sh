#!/bin/bash

# 设置颜色输出
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

# 检查服务健康状态
check_health() {
    local service=$1
    local port=$2
    echo -e "检查 $service 服务状态..."
    curl -s http://localhost:$port/actuator/health | grep -q "UP"
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}$service 运行正常${NC}"
    else
        echo -e "${RED}$service 可能存在问题${NC}"
    fi
}

# 检查各个服务
check_health "Discovery Service" 8761
check_health "Gateway Service" 8080
check_health "Auth Service" 8081
check_health "Task Service" 8082
check_health "File Service" 8083
check_health "Notification Service" 8084

# 检查基础服务
echo -e "\n检查基础服务..."
docker-compose -f docker/docker-compose.yml ps 