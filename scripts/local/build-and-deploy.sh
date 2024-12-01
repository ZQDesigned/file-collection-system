#!/bin/bash

# 设置颜色输出
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

# 检查服务状态和日志的函数
check_service() {
    local service=$1
    echo -e "${GREEN}检查 ${service} 服务状态...${NC}"
    
    # 等待服务启动
    sleep 5
    
    # 检查容器是否运行
    if ! docker-compose -f docker/docker-compose.yml ps | grep "${service}" | grep "Up"; then
        echo -e "${RED}${service} 服务启动失败${NC}"
        echo -e "${GREEN}查看 ${service} 日志:${NC}"
        docker-compose -f docker/docker-compose.yml logs "${service}"
        return 1
    fi
    
    return 0
}

# 构建所有服务
echo -e "${GREEN}开始构建服务...${NC}"

# 进入项目根目录
cd "$(dirname "$0")/../.."

# 清理并构建所有服务
./gradlew clean build

if [ $? -ne 0 ]; then
    echo -e "${RED}构建失败，请检查错误信息${NC}"
    exit 1
fi

# 构建 Docker 镜像
echo -e "${GREEN}开始构建 Docker 镜像...${NC}"

services=("discovery-service" "auth-service" "file-service" "task-service" "notification-service" "gateway-service")

for service in "${services[@]}"; do
    echo -e "${GREEN}构建 ${service} 镜像...${NC}"
    cd "services/${service}"
    docker build -t "${service}:latest" .
    if [ $? -ne 0 ]; then
        echo -e "${RED}${service} 镜像构建失败${NC}"
        exit 1
    fi
    cd ../..
done

echo -e "${GREEN}开始部署基础服务...${NC}"

# 启动基础服务
docker-compose -f docker/docker-compose.yml up -d postgres rabbitmq redis minio

# 等待基础服务就绪
echo -e "${GREEN}等待基础服务就绪...${NC}"
sleep 30

echo -e "${GREEN}开始部署应用服务...${NC}"

# 按顺序启动服务并检查状态
docker-compose -f docker/docker-compose.yml up -d discovery-service
check_service "discovery-service" || exit 1
sleep 20

docker-compose -f docker/docker-compose.yml up -d auth-service
check_service "auth-service" || exit 1

docker-compose -f docker/docker-compose.yml up -d file-service
check_service "file-service" || exit 1

docker-compose -f docker/docker-compose.yml up -d task-service
check_service "task-service" || exit 1

docker-compose -f docker/docker-compose.yml up -d notification-service
check_service "notification-service" || exit 1

docker-compose -f docker/docker-compose.yml up -d gateway-service
check_service "gateway-service" || exit 1

echo -e "${GREEN}所有服务部署完成${NC}" 