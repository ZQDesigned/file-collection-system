#!/bin/bash

# 设置颜色输出
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

# 设置命名空间
NAMESPACE=file-collect

echo -e "${GREEN}创建命名空间...${NC}"
kubectl create namespace $NAMESPACE

echo -e "${GREEN}部署配置和密钥...${NC}"
kubectl apply -f deploy/kubernetes/config.yaml -n $NAMESPACE

echo -e "${GREEN}部署基础服务...${NC}"
kubectl apply -f deploy/kubernetes/postgres.yaml -n $NAMESPACE
kubectl apply -f deploy/kubernetes/rabbitmq.yaml -n $NAMESPACE
kubectl apply -f deploy/kubernetes/redis.yaml -n $NAMESPACE
kubectl apply -f deploy/kubernetes/minio.yaml -n $NAMESPACE

echo -e "${GREEN}等待基础服务就绪...${NC}"
kubectl wait --for=condition=ready pod -l app=postgres -n $NAMESPACE --timeout=300s
kubectl wait --for=condition=ready pod -l app=rabbitmq -n $NAMESPACE --timeout=300s
kubectl wait --for=condition=ready pod -l app=redis -n $NAMESPACE --timeout=300s
kubectl wait --for=condition=ready pod -l app=minio -n $NAMESPACE --timeout=300s

echo -e "${GREEN}部署应用服务...${NC}"
kubectl apply -f deploy/kubernetes/discovery-service.yaml -n $NAMESPACE
sleep 30
kubectl apply -f deploy/kubernetes/auth-service.yaml -n $NAMESPACE
kubectl apply -f deploy/kubernetes/file-service.yaml -n $NAMESPACE
kubectl apply -f deploy/kubernetes/task-service.yaml -n $NAMESPACE
kubectl apply -f deploy/kubernetes/notification-service.yaml -n $NAMESPACE
kubectl apply -f deploy/kubernetes/gateway-service.yaml -n $NAMESPACE

echo -e "${GREEN}等待所有服务就绪...${NC}"
kubectl wait --for=condition=ready pod -l app=discovery-service -n $NAMESPACE --timeout=300s
kubectl wait --for=condition=ready pod -l app=auth-service -n $NAMESPACE --timeout=300s
kubectl wait --for=condition=ready pod -l app=file-service -n $NAMESPACE --timeout=300s
kubectl wait --for=condition=ready pod -l app=task-service -n $NAMESPACE --timeout=300s
kubectl wait --for=condition=ready pod -l app=notification-service -n $NAMESPACE --timeout=300s
kubectl wait --for=condition=ready pod -l app=gateway-service -n $NAMESPACE --timeout=300s

echo -e "${GREEN}部署完成${NC}" 