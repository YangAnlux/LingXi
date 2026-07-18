import request from '@/config/axios'

export interface SendReqVO {
  campaignId: number
  templateCode: string
  customerIds: number[]
}

export interface SendRespVO {
  id: number
  campaignId: number
  channel: number
  templateCode: string
  totalCount: number
  successCount: number
  failCount: number
  sendTime: Date
}

export interface TemplateVO {
  id: number
  code: string
  name: string
  content: string
  title?: string
}

export interface CareLogVO {
  id: number
  customerId: number
  customerName?: string
  campaignId: number
  careType: number
  channel: number
  templateCode: string
  content: string
  status: number
  sendTime: Date
  remark: string
  createTime: Date
}

export interface SendStatisticsVO {
  totalSends: number
  smsTotal: number
  smsSuccess: number
  smsFail: number
  smsRate: number
  mailTotal: number
  mailSuccess: number
  mailFail: number
  mailRate: number
  mailOpenRate: number
  logs: any[]
}

export interface SendLogVO {
  id: number
  campaignId: number
  campaignName?: string
  channel: number
  templateCode: string
  totalCount: number
  successCount: number
  failCount: number
  openCount: number
  clickCount: number
  sendTime: Date
  remark: string
}

// 群发
export const sendSms = async (data: SendReqVO) => {
  return await request.post({ url: '/crm/campaign/send-sms', data })
}

export const sendMail = async (data: SendReqVO) => {
  return await request.post({ url: '/crm/campaign/send-mail', data })
}

// 模板列表
export const getSmsTemplateList = async () => {
  return await request.get({ url: '/crm/campaign/sms-template-list' })
}

export const getMailTemplateList = async () => {
  return await request.get({ url: '/crm/campaign/mail-template-list' })
}

// 关怀日志
export const getCareLogPage = async (params: any) => {
  return await request.get({ url: '/crm/campaign/care-log/page', params })
}

// 发送日志
export const getSendLogPage = async (params: any) => {
  return await request.get({ url: '/crm/campaign/send-log/page', params })
}

// 发送统计
export const getSendStatistics = async (campaignId?: number) => {
  return await request.get({ url: '/crm/campaign/send-statistics', params: { campaignId } })
}

export const getCampaignBreakdown = async () => {
  return await request.get({ url: '/crm/campaign/send-statistics/breakdown' })
}

// 统计仪表盘（独立 Controller: /crm/statistics）
export const getStatisticsDashboard = async (params?: any) => {
  return await request.get({ url: '/crm/statistics/dashboard', params })
}

export const getStatisticsTaskPage = async (params: any) => {
  return await request.get({ url: '/crm/statistics/tasks', params })
}
