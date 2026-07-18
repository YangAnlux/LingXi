import request from '@/config/axios'

export interface SendTaskCreateVO {
  activityId?: number | null
  taskType: string
  templateId: number
  templateCode: string
  channel: number
  customerSelectMode: string
  customerIds?: number[]
  customerTag?: string
  sendMode: string
  scheduledTime?: string | null
  forceSend?: boolean
}

export interface SendTaskRespVO {
  id: number
  activityId?: number
  taskType: string
  templateCode: string
  channel: number
  customerSelectMode: string
  customerCount: number
  sendMode: string
  scheduledTime?: string
  status: string
  missingVarCount: number
  hasWarning: boolean
  missingVariables?: string[]
  createTime: string
}

export const createSendTask = async (data: SendTaskCreateVO) => {
  return await request.post({ url: '/crm/send-task/create', data })
}

export const getSendTaskPage = async (params: any) => {
  return await request.get({ url: '/crm/send-task/page', params })
}

export const getSendTask = async (id: number) => {
  return await request.get({ url: '/crm/send-task/get?id=' + id })
}
