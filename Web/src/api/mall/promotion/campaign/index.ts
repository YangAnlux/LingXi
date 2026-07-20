import request from '@/config/axios'

export interface CampaignVO {
  id?: number
  name: string
  status?: number
  startTime: string
  endTime: string
  remark?: string
  sort?: number
  createTime?: string
}

// 查询营销活动分页
export const getCampaignPage = async (params) => {
  return await request.get({ url: `/promotion/campaign/page`, params })
}

// 查询营销活动详情
export const getCampaign = async (id: number) => {
  return await request.get({ url: `/promotion/campaign/get?id=` + id })
}

// 新增营销活动
export const createCampaign = async (data: CampaignVO) => {
  return await request.post({ url: `/promotion/campaign/create`, data })
}

// 修改营销活动
export const updateCampaign = async (data: CampaignVO) => {
  return await request.put({ url: `/promotion/campaign/update`, data })
}

// 关闭营销活动
export const closeCampaign = async (id: number) => {
  return await request.put({ url: `/promotion/campaign/close?id=` + id })
}

// 删除营销活动
export const deleteCampaign = async (id: number) => {
  return await request.delete({ url: `/promotion/campaign/delete?id=` + id })
}
