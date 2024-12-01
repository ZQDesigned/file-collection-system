#!/bin/bash

# 设置颜色输出
GREEN='\033[0;32m'
NC='\033[0m'

echo -e "${GREEN}停止并删除所有容器...${NC}"
docker-compose -f docker/docker-compose.yml down

echo -e "${GREEN}删除所有卷...${NC}"
docker-compose -f docker/docker-compose.yml down -v

echo -e "${GREEN}清理完成${NC}" 