# LingXi

LingXi 是一个基于 Spring Boot + Vue3 构建的企业级综合管理系统，提供 CRM、商城、IoT、报表等多种业务模块，支持多语言国际化，具备完整的权限管理和工作流引擎。

## 技术栈

### 后端技术
- **语言**: Java 17
- **框架**: Spring Boot 3.5.9
- **数据库**: MySQL 8.0+ / TDengine（时序数据）
- **ORM**: MyBatis-Plus 3.5.x
- **工作流**: Flowable 7.0.x
- **缓存**: Redis 7.0+
- **消息队列**: RabbitMQ
- **文件存储**: MinIO
- **定时任务**: Quartz

### 前端技术
- **框架**: Vue 3.5.12 + TypeScript 5.3.3
- **UI组件**: Element Plus 2.11.1
- **构建工具**: Vite 5.1.4
- **状态管理**: Pinia 2.1.7
- **路由**: Vue Router 4.4.5
- **样式**: SCSS + UnoCSS
- **国际化**: Vue I18n 9.10.2
- **图表**: ECharts 5.5.0

## V1.0 新增功能

### CRM 系统

#### 客户管理 (Customer Management)
- 客户信息录入与维护
- 客户分类与标签管理
- 客户跟进记录
- 客户转化率统计

#### 业务漏斗统计 (Business Funnel Statistics)
- 多维度业务数据分析
- 漏斗转化可视化图表
- 业务状态分布统计
- 销售业绩汇总报表

#### 工作订单 (Work Order)
- 工单创建与分配
- SLA 超时提醒
- 工单状态流转
- 满意度评价

#### 报销管理 (Reimbursement)
- 报销申请流程
- 多级审批机制
- 费用分类管理
- 报销记录查询

#### 发票管理 (Invoice)
- 发票信息管理
- 发票类型分类
- 发票状态追踪
- 电子发票归档

#### 退款管理 (Refund)
- 退款申请处理
- 退款流程审批
- 退款状态监控
- 退款记录统计

### 其他新增模块

#### 营销活动 (Campaign)
- 营销活动创建与管理
- 活动参与记录
- 活动效果统计

#### 报表管理 (Report)
- 任务看板
- 工作报告
- 数据统计分析

#### 流程评论 (Process Comment)
- 工作流节点评论
- 审批意见记录
- 评论历史查询

## 项目结构

```
LingXi/
├── Server/                    # 后端服务
│   ├── mitedtsm-server/       # 启动模块
│   ├── mitedtsm-dependencies/ # 依赖管理
│   ├── mitedtsm-framework/    # 框架基础模块
│   ├── mitedtsm-module-system/# 系统管理模块
│   ├── mitedtsm-module-infra/ # 基础设施模块
│   ├── mitedtsm-module-member/# 会员管理模块
│   ├── mitedtsm-module-bpm/   # 工作流模块
│   ├── mitedtsm-module-crm/   # CRM模块
│   ├── mitedtsm-module-iot/   # IoT模块
│   ├── mitedtsm-module-mall/  # 商城模块
│   ├── mitedtsm-module-report/# 报表模块
│   ├── mitedtsm-module-mp/    # 公众号模块
│   ├── mitedtsm-module-pay/   # 支付模块
│   ├── mitedtsm-module-erp/   # ERP模块
│   ├── mitedtsm-module-ai/    # AI模块
│   └── sql/                   # 数据库脚本
├── Web/                       # 管理后台前端
│   ├── src/
│   │   ├── api/               # API接口
│   │   ├── views/             # 页面组件
│   │   ├── locales/           # 国际化配置
│   │   └── store/             # 状态管理
│   └── package.json
├── MallFrontend/              # 商城前端（小程序）
├── docker-compose/            # Docker部署配置
│   ├── nginx/                 # Nginx配置
│   └── init/                  # 初始化脚本
└── database/                  # 数据库初始化文件
    ├── base/                  # 基础数据库
    └── new/                   # 新增模块脚本
```

## 快速开始

### 环境要求
- JDK 17+
- MySQL 8.0+
- Redis 7.0+
- Node.js 16+
- pnpm 8.6+

### 后端启动

```bash
cd Server

mvn clean package -DskipTests

cd mitedtsm-server
java -jar target/mitedtsm-server.jar
```

### 前端启动

```bash
cd Web

pnpm install

pnpm dev
```

### Docker 部署

```bash
cd docker-compose

docker-compose up -d
```

## 访问地址

| 服务 | 地址 |
|------|------|
| 管理后台 | http://localhost:5173 |
| API接口 | http://localhost:8080 |
| MySQL | localhost:3306 |
| Redis | localhost:6379 |

## 国际化支持

系统支持以下语言：
- 中文 (zh-CN)
- 英文 (en)
- 阿拉伯语 (ar)

## 许可证

MIT License

## 版权

Copyright (c) 2026 LingXi Team