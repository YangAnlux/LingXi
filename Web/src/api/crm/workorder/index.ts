import request from '@/config/axios'

export type WorkOrderVO = {
  id: number
  title: string
  type: string
  priority: string
  status: string
  customerId: number
  customerName: string
  assigneeId: number
  assigneeName: string
  slaDeadline: string
  isSlaBreached: number
  content: string
  solution: string
  resolvedTime: string
  createTime: string
  updateTime: string
}

export type WorkOrderPageReqVO = {
  pageNo: number
  pageSize: number
  title?: string
  type?: string
  priority?: string
  status?: string
  customerId?: number
  assigneeId?: number
}

export type WorkOrderSaveReqVO = {
  id?: number
  title: string
  type?: string
  priority?: string
  customerId?: number
  assigneeId?: number
  slaDeadline?: string
  content: string
  solution?: string
}

export const createWorkOrder = async (data: WorkOrderSaveReqVO) => {
  return await request.post({ url: '/crm/work-order/create', data })
}

export const updateWorkOrder = async (data: WorkOrderSaveReqVO) => {
  return await request.put({ url: '/crm/work-order/update', data })
}

export const deleteWorkOrder = async (id: number) => {
  return await request.delete({ url: '/crm/work-order/delete?id=' + id })
}

export const getWorkOrder = async (id: number) => {
  return await request.get({ url: '/crm/work-order/get', params: { id } })
}

export const getWorkOrderPage = async (params: WorkOrderPageReqVO) => {
  return await request.get({ url: '/crm/work-order/page', params })
}

export const updateWorkOrderStatus = async (id: number, status: string) => {
  return await request.put({ url: '/crm/work-order/update-status', params: { id, status } })
}

export const assignWorkOrder = async (id: number, assigneeId: number) => {
  return await request.put({ url: '/crm/work-order/assign', params: { id, assigneeId } })
}