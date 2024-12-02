# 贡献指南

感谢你考虑为文件收集系统做出贡献！这份文档提供了一些指导方针，帮助你更好地参与项目开发。

## 行为准则

本项目采用了贡献者公约（Contributor Covenant）作为其行为准则。我们希望项目参与者遵守此行为准则。请阅读[行为准则](CODE_OF_CONDUCT.md)以了解哪些行为是允许的，哪些是不允许的。

## 如何贡献

### 报告 Bug

如果你发现了 bug，请通过 GitHub Issues 报告。在创建 issue 之前：

1. 搜索现有的 Issues，确保没有重复报告
2. 确保你使用的是最新版本的代码
3. 创建 issue 时请包含以下信息：
   - 问题的简要描述
   - 复现步骤
   - 期望的行为
   - 实际的行为
   - 环境信息（操作系统、JDK版本等）
   - 相关的日志输出
   - 可能的解决方案

### 提出新功能

1. 首先通过 Issues 提出新功能建议
2. 说明新功能的使用场景和预期效果
3. 等待维护者的反馈和讨论
4. 在得到积极反馈后，可以开始实现相关功能

### 提交代码

1. Fork 项目到你的 GitHub 账号
2. 创建你的特性分支 (`git checkout -b feature/amazing-feature`)
3. 提交你的修改 (`git commit -m 'Add some amazing feature'`)
4. 推送到你的分支 (`git push origin feature/amazing-feature`)
5. 创建一个 Pull Request

### 分支命名规范

- `feature/*`: 新功能
- `fix/*`: Bug 修复
- `docs/*`: 文档更新
- `style/*`: 代码格式修改
- `refactor/*`: 代码重构
- `test/*`: 测试用例
- `chore/*`: 构建过程或辅助工具的变动

### Commit 消息规范

我们使用 [Conventional Commits](https://www.conventionalcommits.org/) 规范：

```
<type>(<scope>): <subject>

<body>

<footer>
```

类型（type）包括：
- feat: 新功能
- fix: Bug 修复
- docs: 文档更新
- style: 代码格式修改
- refactor: 代码重构
- test: 测试用例
- chore: 构建过程或辅助工具的变动

### Pull Request 规范

1. PR 标题要简洁明了，描述本次改动的主要内容
2. PR 描述中需要说明：
   - 改动的原因
   - 改动的内容
   - 可能的影响
3. 确保所有测试通过
4. 确保代码符合项目的代码规范
5. 如果需要，更新相关文档

## 代码规范

### Kotlin 代码规范

- 遵循 [Kotlin 官方编码规范](https://kotlinlang.org/docs/coding-conventions.html)
- 使用 ktlint 进行代码格式化
- 类名使用 PascalCase
- 函数名使用 camelCase
- 常量使用大写字母和下划线

### TypeScript 代码规范

- 遵循项目配置的 ESLint 规则
- 使用 Prettier 进行代码格式化
- 使用 TypeScript 的严格模式
- 优先使用函数式组件和 Hooks
- 组件文件使用 PascalCase
- 工具函数文件使用 camelCase

## 测试指南

> 目前该项目尚未编写完整的测试用例，欢迎贡献测试代码！

1. 单元测试
   - 后端使用 JUnit 5
   - 前端使用 Jest 和 React Testing Library
   - 测试文件以 `.test.ts(x)` 或 `.spec.ts(x)` 结尾

2. 集成测试
   - 后端使用 Spring Boot Test
   - 使用 TestContainers 进行依赖服务的测试

3. 端到端测试
   - 使用 Cypress 进行端到端测试

## 文档

- 如果你的改动影响了 API，请更新 OpenAPI 文档
- 如果你添加了新功能，请在相应的 README 中添加说明
- 保持文档的简洁性和可读性

## 许可证

通过贡献代码，你同意你的贡献将按照项目的 [AGPL-3.0 许可证](LICENSE.txt) 进行许可。
