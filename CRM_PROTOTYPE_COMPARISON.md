# CRM + OA 系统原型与现有项目对比分析报告

## 1. 概述

本文档对比分析用户提供的 **CRM + OA 综合管理系统原型**（约 200+ HTML 页面）与项目中已有的 **mitedtsm-module-crm** 模块，识别功能差距、架构差异及优化建议。

---

## 2. 原型业务域与现有模块映射

### 2.1 原型菜单结构

| 原型菜单 | 英文 | 现有模块覆盖情况 | 状态 |
|---------|------|-----------------|------|
| 客户管理 | Customer | mitedtsm-module-crm/customer | ✅ 完整 |
| 商机管理 | Opportunity | mitedtsm-module-crm/business | ✅ 完整 |
| 订单管理 | Order | 无（CRM 中用 Contract 替代） | ❌ 缺失 |
| 财务管理 | Finance | 部分（Receivable） | ⚠️ 部分覆盖 |
| 统计报表 | Reports | mitedtsm-module-crm/statistics | ✅ 完整 |
| 工单管理 | Work Order | 无 | ❌ 缺失 |
| 营销工具 | Marketing | 无 | ❌ 缺失 |
| 审批管理 | Approval | mitedtsm-module-bpm | ✅ 完整 |
| 办公管理 | Office | mitedtsm-module-bpm/oa | ⚠️ 部分覆盖 |

### 2.2 业务域详细对比

#### 客户域 (Customer)

| 原型功能 | 现有实现 | 状态 | 优化建议 |
|---------|---------|------|---------|
| 客户列表/创建/详情 | [CrmCustomerController.java](file:///home/yyh/workspace/product/Server/mitedtsm-module-crm/src/main/java/com/meession/etm/module/crm/controller/admin/customer/CrmCustomerController.java) | ✅ | - |
| 公海客户 | [CrmCustomerPoolConfigController.java](file:///home/yyh/workspace/product/Server/mitedtsm-module-crm/src/main/java/com/meession/etm/module/crm/controller/admin/customer/CrmCustomerPoolConfigController.java) | ✅ | - |
| 客户查重 | 无 | ❌ | 在 CustomerService 中增加查重接口 |
| 客户转移 | [CrmCustomerTransferReqVO.java](file:///home/yyh/workspace/product/Server/mitedtsm-module-crm/src/main/java/com/meession/etm/module/crm/controller/admin/customer/vo/customer/CrmCustomerTransferReqVO.java) | ✅ | - |
| 联系人管理 | [CrmContactController.java](file:///home/yyh/workspace/product/Server/mitedtsm-module-crm/src/main/java/com/meession/etm/module/crm/controller/admin/contact/CrmContactController.java) | ✅ | - |
| 客户分析 | [CrmStatisticsCustomerController.java](file:///home/yyh/workspace/product/Server/mitedtsm-module-crm/src/main/java/com/meession/etm/module/crm/controller/admin/statistics/CrmStatisticsCustomerController.java) | ✅ | - |

#### 商机域 (Opportunity)

| 原型功能 | 现有实现 | 状态 | 优化建议 |
|---------|---------|------|---------|
| 商机列表/创建/详情 | [CrmBusinessController.java](file:///home/yyh/workspace/product/Server/mitedtsm-module-crm/src/main/java/com/meession/etm/module/crm/controller/admin/business/CrmBusinessController.java) | ✅ | - |
| 报价管理 | CrmBusinessProductDO | ✅ | 可增强为独立报价单模块 |
| 销售漏斗 | [CrmStatisticsFunnelController.java](file:///home/yyh/workspace/product/Server/mitedtsm-module-crm/src/main/java/com/meession/etm/module/crm/controller/admin/statistics/CrmStatisticsFunnelController.java) | ✅ | - |
| 销售预测 | 无 | ❌ | 增加预测分析接口 |

#### 订单域 (Order)

| 原型功能 | 现有实现 | 状态 | 优化建议 |
|---------|---------|------|---------|
| 订单管理 | 无（使用 Contract） | ❌ | 新建订单模块，与合同分离 |
| 产品报价 | CrmBusinessProductDO | ⚠️ | 需独立报价管理 |
| 订单审批 | BPM 工作流 | ✅ | 接入 BPM |

#### 财务域 (Finance)

| 原型功能 | 现有实现 | 状态 | 优化建议 |
|---------|---------|------|---------|
| 回款管理 | [CrmReceivableController.java](file:///home/yyh/workspace/product/Server/mitedtsm-module-crm/src/main/java/com/meession/etm/module/crm/controller/admin/receivable/CrmReceivableController.java) | ✅ | - |
| 回款计划 | [CrmReceivablePlanController.java](file:///home/yyh/workspace/product/Server/mitedtsm-module-crm/src/main/java/com/meession/etm/module/crm/controller/admin/receivable/CrmReceivablePlanController.java) | ✅ | - |
| 发票管理 | 无 | ❌ | 新建发票模块 |
| 报销管理 | 无（BPM/OA 部分覆盖） | ⚠️ | 扩展 BPM OA 报销流程 |
| 退款管理 | 无 | ❌ | 接入 pay 模块退款功能 |

#### 工单域 (Work Order)

| 原型功能 | 现有实现 | 状态 | 优化建议 |
|---------|---------|------|---------|
| 工单管理 | 无 | ❌ | 新建工单模块 |

#### 营销域 (Marketing)

| 原型功能 | 现有实现 | 状态 | 优化建议 |
|---------|---------|------|---------|
| 营销活动 | 无 | ❌ | 新建营销活动模块 |
| 短信群发 | 系统模块有短信管理 | ⚠️ | 集成系统短信模块 |
| 邮件群发 | 系统模块有邮件管理 | ⚠️ | 集成系统邮件模块 |
| 客户关怀 | 无 | ❌ | 新建客户关怀模块 |

#### OA 域 (Office Automation)

| 原型功能 | 现有实现 | 状态 | 优化建议 |
|---------|---------|------|---------|
| 审批中心 | [bpm/processInstance](file:///home/yyh/workspace/product/Web/src/views/bpm/processInstance) | ✅ | - |
| 请假申请 | [bpm/oa/leave](file:///home/yyh/workspace/product/Web/src/views/bpm/oa/leave) | ✅ | - |
| 出差申请 | 无 | ❌ | 扩展 BPM OA |
| 借款申请 | 无 | ❌ | 扩展 BPM OA |
| 客户拜访 | 无 | ❌ | 扩展 BPM OA |
| 公告 | 系统模块有公告管理 | ✅ | 集成系统公告 |
| 任务管理 | 无 | ❌ | 新建任务管理模块 |
| 日程管理 | 无 | ❌ | 新建日程管理模块 |

---

## 3. 实体关系对比

### 3.1 原型实体关系

```
Customer ──1:N── Contact
     │
     ├──1:N── Opportunity ──1:N── ProductQuotation
     │
     ├──1:N── Order ──1:N── OrderLineItem
     │       ├──1:N── Invoice
     │       └──1:N── Receipt
     │
     ├──1:N── WorkOrder
     │
     └──1:N── CustomerCare

Marketing:
  Campaign ──1:N── 目标客户
  SMSBroadcast / EmailBroadcast

OA:
  Reimbursement / Loan / Leave / Trip / Visit ── Approval
```

### 3.2 现有实体关系

```
CrmCustomerDO ──1:N── CrmContactDO
     │
     ├──1:N── CrmBusinessDO ──1:N── CrmBusinessProductDO
     │
     ├──1:N── CrmContractDO ──1:N── CrmContractProductDO
     │       └──1:N── CrmReceivableDO ──1:N── CrmReceivablePlanDO
     │
     ├──1:N── CrmClueDO
     │
     └──1:N── CrmFollowUpRecordDO

BPM:
  ProcessInstance ──1:N── Task
```

### 3.3 实体差距分析

| 原型实体 | 现有实体 | 差距说明 |
|---------|---------|---------|
| Order | CrmContractDO | 原型中订单与合同是分离的，现有系统合同即订单 |
| OrderLineItem | CrmContractProductDO | 类似但语义不同 |
| Invoice | 无 | 发票管理缺失 |
| WorkOrder | 无 | 工单管理缺失 |
| CustomerCare | 无 | 客户关怀缺失 |
| Campaign | 无 | 营销活动缺失 |
| SMSBroadcast | SysSmsTemplate | 需扩展为群发能力 |
| EmailBroadcast | SysMailTemplate | 需扩展为群发能力 |
| Reimbursement | 无 | 报销流程缺失 |
| Loan | 无 | 借款流程缺失 |
| Trip | 无 | 出差流程缺失 |
| Visit | 无 | 客户拜访流程缺失 |

---

## 4. 页面类型对比

### 4.1 原型页面分布

| 类型 | 数量(约) | 说明 |
|------|---------|------|
| 列表页 | 50+ | 带搜索/筛选/分页的数据表格 |
| 表单页 | 30+ | 创建/编辑 |
| 详情页 | 25+ | 带Tab切换的信息展示 |
| 审批页 | 20+ | 多种审批状态变体 |
| 报表页 | 20+ | 图表/统计分析 |
| 弹窗 | 30+ | 确认/选择/操作 |

### 4.2 现有页面统计

| 类型 | 数量(约) | 说明 |
|------|---------|------|
| 列表页 | 15+ | 客户、线索、商机、合同、回款等 |
| 表单页 | 12+ | 创建/编辑表单 |
| 详情页 | 8+ | 客户、线索、商机、合同、回款、联系人等 |
| 审批页 | 0 | 通过 BPM 统一处理 |
| 报表页 | 5+ | 客户、漏斗、绩效、画像、排行 |
| 弹窗 | 10+ | 选择/确认弹窗 |

### 4.3 页面差距分析

| 差距 | 说明 |
|------|------|
| 列表页 | 缺少订单、发票、工单、营销活动、任务、日程等列表页 |
| 表单页 | 缺少订单、发票、工单、营销活动、报销、借款等表单页 |
| 详情页 | 缺少订单、发票、工单等详情页 |
| 审批页 | 原型有独立审批页，现有依赖 BPM |

---

## 5. 审批状态机对比

### 5.1 原型审批状态机

```
待审批 → 审批中 → 已通过 / 被驳回 / 被否决
                          ↓
                       已撤销
```

适用范围：订单、回款、报销、退款、请假、出差、借款、客户拜访

### 5.2 现有审批状态机

```java
// CrmAuditStatusEnum
public enum CrmAuditStatusEnum {
    PENDING(0, "待审核"),
    APPROVED(1, "审核通过"),
    REJECTED(2, "审核不通过");
}
```

适用范围：合同、回款

### 5.3 状态机差距

| 差距 | 说明 |
|------|------|
| 状态缺失 | 缺少"审批中"、"被否决"、"已撤销"状态 |
| 适用范围 | 未覆盖报销、退款、请假、出差、借款、客户拜访 |

---

## 6. 优化建议

### 6.1 功能增强建议

#### 优先级 P0（核心必选）

| 序号 | 优化项 | 描述 | 涉及模块 |
|------|--------|------|---------|
| 1 | 订单管理模块 | 新建独立订单模块，与合同分离 | crm/order |
| 2 | 发票管理模块 | 新建发票管理，支持开票/票据记录 | crm/invoice |
| 3 | 工单管理模块 | 新建工单管理，支持状态流转 | crm/workorder |
| 4 | 营销活动模块 | 新建营销活动，支持目标客户管理 | crm/marketing |
| 5 | 客户关怀模块 | 新建客户关怀，支持生日/节假日自动祝福 | crm/customerCare |

#### 优先级 P1（重要功能）

| 序号 | 优化项 | 描述 | 涉及模块 |
|------|--------|------|---------|
| 6 | OA 流程扩展 | 增加出差、借款、客户拜访流程 | bpm/oa |
| 7 | 退款管理 | 接入 pay 模块退款功能到 CRM | crm/receivable |
| 8 | 报价管理 | 独立报价单管理模块 | crm/quotation |
| 9 | 销售预测 | 增加预测分析功能 | crm/statistics |
| 10 | 客户查重 | 在客户管理中增加查重接口 | crm/customer |

#### 优先级 P2（一般功能）

| 序号 | 优化项 | 描述 | 涉及模块 |
|------|--------|------|---------|
| 11 | 任务管理 | 新建任务管理模块 | crm/task |
| 12 | 日程管理 | 新建日程管理模块 | crm/schedule |
| 13 | 短信群发 | 集成系统短信模块，支持批量发送 | crm/marketing |
| 14 | 邮件群发 | 集成系统邮件模块，支持批量发送 | crm/marketing |
| 15 | 审批状态机扩展 | 增加审批中、被否决、已撤销状态 | crm/common |

### 6.2 架构优化建议

#### 6.2.1 模块划分优化

```
建议新增模块结构：
mitedtsm-module-crm/
├── crm-order/          # 订单管理（新增）
├── crm-invoice/        # 发票管理（新增）
├── crm-workorder/      # 工单管理（新增）
├── crm-marketing/      # 营销工具（新增）
├── crm-customer-care/  # 客户关怀（新增）
└── crm-task/           # 任务管理（新增）

建议从其他模块迁移：
├── bpm-oa/             # 扩展出差、借款、拜访流程
└── pay-refund/         # 接入退款能力
```

#### 6.2.2 实体关系优化

```
优化后的实体关系：
CrmCustomerDO ──1:N── CrmContactDO
     │
     ├──1:N── CrmClueDO
     │
     ├──1:N── CrmBusinessDO ──1:N── CrmQuotationDO
     │
     ├──1:N── CrmOrderDO ──1:N── CrmOrderItemDO
     │       ├──1:N── CrmInvoiceDO
     │       └──1:N── CrmReceivableDO ──1:N── CrmReceivablePlanDO
     │
     ├──1:N── CrmContractDO ──1:N── CrmContractItemDO
     │
     ├──1:N── CrmWorkOrderDO
     │
     └──1:N── CrmCustomerCareDO

CrmMarketingCampaignDO ──1:N── CrmCampaignCustomerDO
CrmSmsBroadcastDO / CrmEmailBroadcastDO
```

#### 6.2.3 审批流程优化

```java
// 优化后的审批状态枚举
public enum CrmAuditStatusEnum {
    PENDING(0, "待审批"),
    PROCESSING(1, "审批中"),
    APPROVED(2, "已通过"),
    REJECTED(3, "被驳回"),
    OVERRULED(4, "被否决"),
    CANCELLED(5, "已撤销");
}

// 统一审批接口
public interface CrmApprovalService {
    void submitApproval(Long businessId, String bizType);
    void approve(Long taskId, String comment);
    void reject(Long taskId, String comment);
    void overrule(Long taskId, String comment);
    void cancel(Long businessId, String bizType);
}
```

### 6.3 前端优化建议

#### 6.3.1 页面结构优化

```
新增前端页面结构：
src/views/crm/
├── order/             # 订单管理（新增）
│   ├── OrderForm.vue
│   ├── OrderDetail.vue
│   └── index.vue
├── invoice/           # 发票管理（新增）
│   ├── InvoiceForm.vue
│   ├── InvoiceDetail.vue
│   └── index.vue
├── workorder/         # 工单管理（新增）
│   ├── WorkOrderForm.vue
│   ├── WorkOrderDetail.vue
│   └── index.vue
├── marketing/         # 营销工具（新增）
│   ├── campaign/
│   │   ├── CampaignForm.vue
│   │   └── index.vue
│   ├── broadcast/
│   │   ├── SmsBroadcastForm.vue
│   │   ├── EmailBroadcastForm.vue
│   │   └── index.vue
│   └── care/
│       ├── CareForm.vue
│       └── index.vue
└── customerCare/      # 客户关怀（新增）
    ├── CustomerCareForm.vue
    └── index.vue
```

#### 6.3.2 组件复用优化

| 组件 | 复用场景 | 优化建议 |
|------|---------|---------|
| ApprovalCard | 订单审批、回款审批、报销审批 | 提取通用审批卡片组件 |
| CustomerSelector | 商机、订单、合同、工单 | 提取通用客户选择器 |
| ProductSelector | 商机产品、订单产品、合同产品 | 提取通用产品选择器 |
| AuditStatusBadge | 各审批状态展示 | 提取通用状态徽章 |

---

## 7. 实施路线图

### 阶段一：核心功能补齐（4-6 周）

| 任务 | 模块 | 工时 |
|------|------|------|
| 订单管理模块 | crm/order | 2 周 |
| 发票管理模块 | crm/invoice | 1 周 |
| 审批状态机扩展 | crm/common | 0.5 周 |
| 客户查重接口 | crm/customer | 0.5 周 |

### 阶段二：业务流程完善（4-6 周）

| 任务 | 模块 | 工时 |
|------|------|------|
| OA 流程扩展（出差/借款/拜访） | bpm/oa | 2 周 |
| 退款管理集成 | crm/receivable + pay | 1 周 |
| 报价管理模块 | crm/quotation | 1 周 |
| 销售预测分析 | crm/statistics | 1 周 |

### 阶段三：营销与服务能力（4-6 周）

| 任务 | 模块 | 工时 |
|------|------|------|
| 工单管理模块 | crm/workorder | 2 周 |
| 营销活动模块 | crm/marketing | 2 周 |
| 客户关怀模块 | crm/customerCare | 1 周 |

### 阶段四：办公工具完善（2-3 周）

| 任务 | 模块 | 工时 |
|------|------|------|
| 任务管理模块 | crm/task | 1 周 |
| 日程管理模块 | crm/schedule | 1 周 |

---

## 8. 总结

### 8.1 现有 CRM 模块优势

1. **基础功能完善**：客户、商机、合同、回款、统计分析等核心 CRM 功能完整
2. **数据权限控制**：基于 CrmPermission 的精细化数据权限管理
3. **统计报表丰富**：5 大类统计报表（客户、漏斗、绩效、画像、排行）
4. **待办事项集成**：backlog 模块整合各类待办提醒
5. **与 BPM 集成**：合同、回款审批已接入工作流

### 8.2 主要差距

| 类别 | 差距 | 影响 |
|------|------|------|
| 业务功能 | 缺少订单、发票、工单、营销活动、客户关怀 | 业务覆盖不完整 |
| OA 流程 | 缺少出差、借款、客户拜访流程 | 办公自动化不完整 |
| 审批状态 | 状态机不完整 | 审批体验受限 |
| 页面数量 | 约 60+ 页面 vs 原型 200+ | 用户体验差距大 |

### 8.3 核心优化方向

1. **订单与合同分离**：建立独立的订单管理体系
2. **完善财务闭环**：增加发票、退款管理
3. **扩展服务能力**：新增工单管理模块
4. **增强营销能力**：新增营销活动、客户关怀
5. **完善 OA 流程**：扩展出差、借款、客户拜访
6. **统一审批状态机**：标准化审批流程