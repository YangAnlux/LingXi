import request from '@/config/axios'

export interface CampaignVO {
  id: number
  name: string
  status: number
  startTime: Date
  endTime: Date
  budget: number
  actualCost: number
  channel: number
  description: string
  ownerUserId: number
  ownerUserName?: string
  remark: string
  creator: string
  creatorName?: string
  createTime: Date
  updateTime: Date
}

export const getCampaignPage = async (params: any) => {
  return await request.get({ url: `/crm/campaign/page`, params })
}

export const getCampaign = async (id: number) => {
  return await request.get({ url: `/crm/campaign/get?id=` + id })
}

export const createCampaign = async (data: CampaignVO) => {
  return await request.post({ url: `/crm/campaign/create`, data })
}

export const updateCampaign = async (data: CampaignVO) => {
  return await request.put({ url: `/crm/campaign/update`, data })
}

export const deleteCampaign = async (id: number) => {
  return await request.delete({ url: `/crm/campaign/delete?id=` + id })
}

export const exportCampaign = async (params) => {
  return await request.download({ url: `/crm/campaign/export-excel`, params })
}

export const updateCampaignStatus = async (id: number, status: number) => {
  return await request.put({ url: `/crm/campaign/status`, params: { id, status } })
}
