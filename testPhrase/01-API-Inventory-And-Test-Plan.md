# BPM 工作流程模块 — API 清单与测试计划

## 项目信息

| 字段 | 内容 |
|------|------|
| 项目名称 | 密讯ETM企业管理系统 (mitedtsm) — BPM 工作流程子系统 |
| 模块 | mitedtsm-module-bpm |
| 文档类型 | API 清单与测试计划 |
| 版本 | V1.0 |
| 日期 | 2026-07-13 |
| 工作流引擎 | Flowable 7.x (flowable-spring-boot-starter-process) |
| 覆盖率目标 | 行覆盖率 ≥ 70%，分支覆盖率 ≥ 60%，方法覆盖率 ≥ 75% |

---

## 1. API 端点总览

### 1.1 模块统计

| 子模块 | Controller | 端点数 | 优先级 |
|--------|-----------|:------:|:------:|
| 流程任务 (Task) | BpmTaskController | 15 | P0 |
| 流程实例 (ProcessInstance) | BpmProcessInstanceController | 10 | P0 |
| 流程抄送 (Copy) | BpmProcessInstanceCopyController | 1 | P1 |
| 流程模型 (Model) | BpmModelController | 12 | P0 |
| 流程定义 (Definition) | BpmProcessDefinitionController | 2 | P0 |
| 流程分类 (Category) | BpmCategoryController | 7 | P1 |
| 动态表单 (Form) | BpmFormController | 6 | P1 |
| 用户组 (UserGroup) | BpmUserGroupController | 6 | P1 |
| 流程监听器 (Listener) | BpmProcessListenerController | 5 | P2 |
| 流程表达式 (Expression) | BpmProcessExpressionController | 5 | P2 |
| OA请假示例 | BpmOALeaveController | 3 | P1 |
| **合计** | **11** | **72** | - |

---

## 2. 流程任务 API (Task) — P0 最高优先级

**Controller**: `BpmTaskController` (`/bpm/task`)

| # | 方法 | URL | 权限 | 说明 |
|---|:----:|-----|------|------|
| T01 | GET | `/bpm/task/todo-page` | `bpm:task:query` | 待办任务分页 |
| T02 | GET | `/bpm/task/done-page` | `bpm:task:query` | 已办任务分页 |
| T03 | GET | `/bpm/task/manager-page` | `bpm:task:manager-query` | 全部任务分页(管理员) |
| T04 | GET | `/bpm/task/list-by-process-instance-id` | `bpm:task:query` | 按流程实例查任务 |
| T05 | PUT | `/bpm/task/approve` | `bpm:task:update` | **通过任务** |
| T06 | PUT | `/bpm/task/reject` | `bpm:task:update` | **驳回任务** |
| T07 | GET | `/bpm/task/list-by-return` | `bpm:task:update` | 可退回节点列表 |
| T08 | PUT | `/bpm/task/return` | `bpm:task:update` | **退回任务** |
| T09 | PUT | `/bpm/task/delegate` | `bpm:task:update` | **委派任务** |
| T10 | PUT | `/bpm/task/transfer` | `bpm:task:update` | **转派任务** |
| T11 | PUT | `/bpm/task/create-sign` | `bpm:task:update` | **加签** |
| T12 | DELETE | `/bpm/task/delete-sign` | `bpm:task:update` | **减签** |
| T13 | PUT | `/bpm/task/copy` | `bpm:task:update` | **抄送** |
| T14 | PUT | `/bpm/task/withdraw` | `bpm:task:update` | **撤回任务** |
| T15 | GET | `/bpm/task/list-by-parent-task-id` | `bpm:task:query` | 子任务列表 |

---

## 3. 流程实例 API (ProcessInstance) — P0

**Controller**: `BpmProcessInstanceController` (`/bpm/process-instance`)

| # | 方法 | URL | 权限 | 说明 |
|---|:----:|-----|------|------|
| P01 | GET | `/bpm/process-instance/my-page` | `bpm:process-instance:query` | 我的流程分页 |
| P02 | GET | `/bpm/process-instance/manager-page` | `bpm:process-instance:manager-query` | 管理流程分页 |
| P03 | POST | `/bpm/process-instance/create` | `bpm:process-instance:query` | **发起流程** |
| P04 | GET | `/bpm/process-instance/get` | `bpm:process-instance:query` | 流程实例详情 |
| P05 | DELETE | `/bpm/process-instance/cancel-by-start-user` | `bpm:process-instance:cancel` | 发起人取消 |
| P06 | DELETE | `/bpm/process-instance/cancel-by-admin` | `bpm:process-instance:cancel-by-admin` | 管理员取消 |
| P07 | GET | `/bpm/process-instance/get-approval-detail` | `bpm:process-instance:query` | **审批详情** |
| P08 | GET | `/bpm/process-instance/get-next-approval-nodes` | `bpm:process-instance:query` | 下一审批节点 |
| P09 | GET | `/bpm/process-instance/get-bpmn-model-view` | - | BPMN模型视图 |
| P10 | GET | `/bpm/process-instance/get-print-data` | `bpm:process-instance:query` | 打印数据 |

---

## 4. 流程定义与模型 API — P0/P1

### 4.1 流程模型 (`/bpm/model`)

| # | 方法 | URL | 权限 |
|---|:----:|-----|------|
| M01 | GET | `/bpm/model/list` | - |
| M02 | GET | `/bpm/model/get` | `bpm:model:query` |
| M03 | POST | `/bpm/model/create` | `bpm:model:create` |
| M04 | PUT | `/bpm/model/update` | `bpm:model:update` |
| M05 | PUT | `/bpm/model/update-sort-batch` | - |
| M06 | POST | `/bpm/model/deploy` | `bpm:model:deploy` |
| M07 | PUT | `/bpm/model/update-state` | `bpm:model:update` |
| M08 | DELETE | `/bpm/model/delete` | `bpm:model:delete` |
| M09 | DELETE | `/bpm/model/clean` | `bpm:model:clean` |
| M10 | GET | `/bpm/model/simple/get` | - |

### 4.2 流程定义 (`/bpm/process-definition`)

| # | 方法 | URL | 权限 |
|---|:----:|-----|------|
| D01 | GET | `/bpm/process-definition/page` | `bpm:process-definition:query` |
| D02 | GET | `/bpm/process-definition/list` | - |

### 4.3 流程分类 (`/bpm/category`)

| # | 方法 | URL | 测试维度 |
|---|:----:|-----|---------|
| C01-C07 | CRUD + page + simple-list | 全 CRUD 覆盖 |

### 4.4 动态表单 (`/bpm/form`)

| # | 方法 | URL | 测试维度 |
|---|:----:|-----|---------|
| F01-F06 | CRUD + page + simple-list | 全 CRUD 覆盖 |

---

## 5. 测试策略

### 5.1 测试金字塔

```
         ┌──────────────┐
         │  E2E 流程测试  │  ~10% — 请假/审批全流程
         │  ~8 用例      │
        ┌┴──────────────┴┐
        │  接口测试       │  ~25% — 72 端点 CRUD+审批动作
        │  ~80 用例      │
       ┌┴────────────────┴┐
       │   集成测试        │  ~20% — Service+Flowable API
       │   ~40 用例       │
      ┌┴──────────────────┴┐
      │   单元测试          │  ~45% — Service 层 + 工具类
      │   ~150 用例        │
      └────────────────────┘
```

### 5.2 BPM 特有测试维度

| 维度 | 占比 | BPM 专项关注 |
|------|:---:|------------|
| 正向测试 | 45% | 流程发起→审批→通过完整链路 |
| 参数校验 | 15% | 流程变量、审批意见、加签/减签参数 |
| 权限校验 | 15% | 待办人权限、管理员越权 |
| 状态流转 | 15% | 审批通过/驳回/退回/撤销/委派/转派/加签/减签 |
| 异常场景 | 10% | 重复审批、流程不存在、节点不存在 |

### 5.3 核心业务流程测试

```
1. 流程定义 → 部署 → 发起实例 → 待办 → 通过 → 下一节点 → 通过 → 完成
2. 发起 → 待办 → 驳回 → 发起人重新编辑 → 重新提交 → 通过 → 完成
3. 发起 → 待办 → 退回 → 上一节点重新审批 → 通过 → 完成
4. 发起 → 待办 → 委派他人 → 被委派人审批 → 回到委托人 → 通过
5. 发起 → 待办 → 转派他人 → 被转派人审批 → 完成
6. 发起 → 待办 → 加签(前/后) → 被加签人审批 → 回到加签人 → 通过
7. 发起 → 待办 → 抄送 → 被抄送人可查看
8. 发起 → 发起人撤回 → 流程取消
```

---

## 6. Mock 策略

| 被测层 | Mock 对象 | 原因 |
|--------|----------|------|
| Service 层 | Mapper (MyBatis-Plus) | 隔离数据库 |
| Service 层 | Flowable API (RuntimeService/TaskService/HistoryService/RepositoryService) | 隔离工作流引擎 |
| Service 层 | AdminUserApi / DeptApi | 隔离系统模块 RPC |
| Service 层 | BpmMessageService | 隔离消息发送 |
| Controller 层 | Service | 隔离业务逻辑 |

---

## 7. 测试环境

| 组件 | 版本 | 用途 |
|------|------|------|
| JUnit 5 | 5.10.x | 测试框架 |
| Mockito | 5.x (mockito-inline) | Mock 依赖 |
| Spring Boot Test | 3.5.9 | 集成测试基类 (BaseDbUnitTest) |
| H2 Database | 2.x | 内存测试数据库 |
| Podam | - | 随机 POJO 生成 |

---

## 8. 覆盖率目标

| 指标 | 质量门禁 | BPM 模块目标 |
|------|:------:|:----------:|
| 行覆盖率 | ≥ 70% | ≥ 75% |
| 分支覆盖率 | ≥ 60% | ≥ 65% |
| 方法覆盖率 | ≥ 75% | ≥ 80% |
| 类覆盖率 | ≥ 80% | ≥ 85% |
