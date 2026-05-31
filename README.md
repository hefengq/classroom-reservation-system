# Classroom Reservation System

教室预约系统全栈实现，包含 Spring Boot 3 后端、Vue3 + Element Plus 前端、H2 内存数据库、JWT 登录鉴权、预约审批、WebSocket 审批通知和操作日志。

## 目录

- `backend`: Spring Boot 3 后端服务
- `frontend`: Vue3 前端工程
- `backend/src/main/resources/schema.sql`: H2/MySQL 兼容建表脚本
- `backend/src/main/resources/data.sql`: 初始化账号、教室和预约数据
- `docs`: 报告材料

## 环境要求

- JDK 21
- Maven 3.8+
- Node.js 18+

## 快速启动

后端默认使用 H2 内存数据库，不需要单独安装 MySQL，也不需要手动执行 SQL。启动时会自动加载 `schema.sql` 和 `data.sql`。

1. 启动后端：

   ```bash
   cd backend
   mvn spring-boot:run
   ```

   后端服务端口为 `8080`，H2 控制台地址为 `http://localhost:8080/h2-console`。

2. 启动前端：

   ```bash
   cd frontend
   npm install
   npm run dev
   ```

   前端默认运行在 `http://localhost:5173`，接口和 WebSocket 会代理到 `http://localhost:8080`。

## 默认账号

种子账号密码均为 `123456`：

- 管理员：`admin / 123456`
- 普通用户：`student01 / 123456`

## 主要功能

- 用户登录和 JWT 鉴权
- 教室预约看板，支持按教室筛选和直接提交预约
- 我的预约列表，显示审批状态和驳回原因
- 管理员审批预约，通过或驳回后向申请人发送 WebSocket 通知
- 管理员维护教室和创建用户
- 操作日志异步记录
