import request from '@/config/axios'

export interface TaskVO {
  id: number
  name: string
  description: string
  status: number
  priority: number
  startTime?: Date
  endTime?: Date
  completedTime?: Date
  ownerUserId: number
  ownerUserName?: string
  assignerUserId: number
  assignerUserName?: string
  relatedCampaignId: number
  relatedCustomerId: number
  relatedBusinessId: number
  remark: string
  creator: string
  creatorName?: string
  createTime: Date
  updateTime: Date
}

export const getTaskPage = async (params: any) => {
  return await request.get({ url: `/crm/task/page`, params })
}

export const getTask = async (id: number) => {
  return await request.get({ url: `/crm/task/get?id=` + id })
}

export const createTask = async (data: TaskVO) => {
  return await request.post({ url: `/crm/task/create`, data })
}

export const updateTask = async (data: TaskVO) => {
  return await request.put({ url: `/crm/task/update`, data })
}

export const deleteTask = async (id: number) => {
  return await request.delete({ url: `/crm/task/delete?id=` + id })
}

export const exportTask = async (params: any) => {
  return await request.download({ url: `/crm/task/export-excel`, params })
}

export const updateTaskStatus = async (id: number, status: number) => {
  return await request.put({ url: `/crm/task/status`, params: { id, status } })
}
