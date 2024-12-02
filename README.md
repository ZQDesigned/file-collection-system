# 文件收集系统 (File Collection System)

一个基于微服务架构的文件收集系统，支持创建任务、提交文件、批量下载等功能。

## 系统架构

系统采用微服务架构，包含以下服务：

- **Gateway Service** (8080): API 网关，负责请求路由和认证
- **Auth Service** (8081): 认证服务，处理用户认证和授权
- **Task Service** (8082): 任务服务，管理任务和提交
- **File Service** (8083): 文件服务，处理文件上传和存储
- **Notification Service** (8084): 通知服务，处理邮件和实时通知
- **Discovery Service** (8761): 服务发现，基于 Eureka

## 技术栈

### 后端
- Kotlin + Spring Boot 3.2
- Spring Cloud Gateway
- Spring Security + JWT
- Spring Data JPA
- PostgreSQL
- MinIO (对象存储)
- RabbitMQ (消息队列)
- Redis (缓存)
- gRPC (服务间通信)

### 前端
- React + TypeScript
- Material-UI & Ant Design
- React Router
- Vite

## 主要功能

1. 任务管理
   - 创建任务
   - 设置截止时间
   - 配置文件类型限制
   - 自定义表单字段

2. 提交管理
   - 文件上传
   - 表单填写
   - 提交修改
   - 提交撤销

3. 文件管理
   - 单个/批量下载
   - 自定义打包规则
   - 异步下载任务

4. 通知系统
   - 邮件通知
   - WebSocket 实时通知

## 快速开始

### 环境要求
- JDK 17+
- Node.js 18+
- Docker & Docker Compose
- PostgreSQL 14+
- MinIO
- RabbitMQ
- Redis

### 本地开发环境搭建

当前该项目仍然处于开发阶段，尚未发布第一个版本。使用 IDEA 等 IDE 打开项目，确保本地环境存在 PostgreSQL、MinIO、RabbitMQ 和 Redis，并且配置正确，即可正常进行开发。


## API 文档

启动服务后，可以通过以下地址访问 Swagger UI：
- Gateway API: http://localhost:8080/swagger-ui.html
- Auth API: http://localhost:8081/swagger-ui.html
- Task API: http://localhost:8082/swagger-ui.html
- File API: http://localhost:8083/swagger-ui.html
- Notification API: http://localhost:8084/swagger-ui.html

## 项目结构

```text
├── docs/ # 项目文档
├── services/ # 后端服务
│ ├── auth-service/ # 认证服务
│ ├── common/ # 公共模块
│ ├── discovery-service/ # 服务发现
│ ├── file-service/ # 文件服务
│ ├── gateway-service/ # API网关
│ ├── notification-service/ # 通知服务
│ └── task-service/ # 任务服务
├── web/ # 前端应用
├── docker/ # Docker 配置
├── deploy/ # 与 K8S 部署相关的配置
├── scripts/ # 部署脚本
└── README.md
```

## 开发指南

待完善

## 贡献指南

欢迎提交 Issue 和 Pull Request！详情请参考 [CONTRIBUTING.md](CONTRIBUTING.md)

## 许可证

本项目采用 [AGPL-3.0 许可证](LICENSE.txt)


