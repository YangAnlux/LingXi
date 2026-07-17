// 2023级软4蔡磊202305566515,2026年7月14日
import request from '@/config/axios'

export interface WorkOrderVO {
  id: number
  title: string
  type: string
  priority: string
  status: string
  customerId: number
  customerName?: string
  assigneeId: number
  assigneeName?: string
  slaDeadline: Date
  isSlaBreached: boolean
  content: string
  solution: string
  resolvedTime: Date
  createTime: Date
  creator: string
  creatorName?: string
  satisfactionScore?: number
  satisfactionComment?: string
}

// 查询工单分页
export const getWorkOrderPage = async (params: any) => {
  return await request.get({ url: `/crm/work-order/page`, params })
}

// 查询工单详情
export const getWorkOrder = async (id: number) => {
  return await request.get({ url: `/crm/work-order/get?id=` + id })
}

// 新增工单
export const createWorkOrder = async (data: WorkOrderVO) => {
  return await request.post({ url: `/crm/work-order/create`, data })
}

// 修改工单
export const updateWorkOrder = async (data: WorkOrderVO) => {
  return await request.put({ url: `/crm/work-order/update`, data })
}

// 删除工单
export const deleteWorkOrder = async (id: number) => {
  return await request.delete({ url: `/crm/work-order/delete?id=` + id })
}

// 开始处理工单（待处理/已退回 → 处理中）
export const processWorkOrder = async (id: number) => {
  return await request.put({ url: `/crm/work-order/process?id=` + id })
}

// 完结工单（处理中 → 已完结）
export const resolveWorkOrder = async (id: number) => {
  return await request.put({ url: `/crm/work-order/resolve?id=` + id })
}

// 退回工单（处理中 → 已退回）
export const returnWorkOrder = async (id: number) => {
  return await request.put({ url: `/crm/work-order/return?id=` + id })
}

// 23软件工程4班蔡磊202305566515
// 分配工单（指派处理人）
export const assignWorkOrder = async (data: { id: number; assigneeId: number }) => {
  return await request.put({ url: `/crm/work-order/assign`, data })
}

// 处理记录查询参数
export interface WorkOrderRecordVO {
  id: number
  workOrderId: number
  fromStatus: string
  toStatus: string
  creator: string
  createTime: Date
}

// 获得工单处理记录列表
export const getRecordList = async (workOrderId: number) => {
  return await request.get({ url: `/crm/work-order/record-list?workOrderId=` + workOrderId })
}

// 23软件工程4班蔡磊202305566515
// 满意度回访评分
export const submitSatisfaction = async (data: { id: number; satisfactionScore: number; satisfactionComment?: string }) => {
  return await request.put({ url: `/crm/work-order/satisfaction`, data })
}

// 23软件工程4班蔡磊202305566515
export interface WorkOrderStatItem {
  name: string
  count: number
}

// 23软件工程4班蔡磊202305566515
export interface WorkOrderStatisticsVO {
  byType: WorkOrderStatItem[]
  byStatus: WorkOrderStatItem[]
  byAssignee: WorkOrderStatItem[]
}

// 23软件工程4班蔡磊202305566515
// 工单统计报表（按类型/状态/处理人）
export const getStatistics = async () => {
  return await request.get({ url: `/crm/work-order/statistics` })
}
