# 任务服务

## 功能介绍

任务服务是文件收集系统的核心服务之一，提供以下功能：

- 任务管理
  - 创建任务
  - 查询任务
  - 更新任务
  - 删除任务
- 提交管理
  - 提交作业
  - 查询提交记录
  - 下载提交文件
- 批量下载
  - 创建下载任务
  - 异步处理
  - 生成下载链接

## 技术栈

- Spring Boot 3.2
- Spring Security
- Spring Data JPA
- PostgreSQL
- RabbitMQ
- gRPC
- OpenAPI 3.0

## 依赖服务

- auth-service：认证服务
- file-service：文件存储服务

## API 文档

启动服务后访问：http://localhost:8082/swagger-ui.html

## 监控指标

通过 Prometheus 和 Grafana 监控以下指标：

- 任务创建数量：task.created
- 提交成功率：submission.success / (submission.success + submission.failure)
- 文件上传速度：file.upload.duration
- 文件下载速度：file.download.duration
- API 响应时间：api.response.duration

监控端点：http://localhost:8082/actuator/prometheus

## 配置说明

主要配置项：
```properties
# 服务端口
server.port=8082
# 数据库配置
spring.datasource.url=jdbc:postgresql://localhost:5432/filecollect
spring.datasource.username=postgres
spring.datasource.password=postgres
# RabbitMQ配置
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
# gRPC配置
grpc.client.auth-service.address=static://localhost:9090
grpc.client.file-service.address=static://localhost:9092
```

## 部署说明

1. 准备环境
  - JDK 17+
  - PostgreSQL 14+
  - RabbitMQ 3.8+

2. 构建项目
   ```bash
   ./gradlew :services:task-service:build
   ```

3. 运行服务
   ```bash
   java -jar task-service.jar
   ```