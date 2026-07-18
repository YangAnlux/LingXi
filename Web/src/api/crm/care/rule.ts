import request from '@/config/axios'

export interface CareRuleVO {
  id?: number
  name: string
  triggerType: string
  triggerDaysBefore?: number
  holidayId?: number | null
  templateId: number
  templateCode: string
  channel: number
  sendWindowStart?: string | null
  sendWindowEnd?: string | null
  isEnabled: number
  lastExecTime?: string
  remark?: string
  createTime?: string
}

export const getCareRulePage = async (params: any) => {
  return await request.get({ url: '/crm/care-rule/page', params })
}

export const getCareRule = async (id: number) => {
  return await request.get({ url: '/crm/care-rule/get?id=' + id })
}

export const createCareRule = async (data: CareRuleVO) => {
  return await request.post({ url: '/crm/care-rule/create', data })
}

export const updateCareRule = async (data: CareRuleVO) => {
  return await request.put({ url: '/crm/care-rule/update', data })
}

export const deleteCareRule = async (id: number) => {
  return await request.delete({ url: '/crm/care-rule/delete?id=' + id })
}

export const executeCareRule = async (id: number) => {
  return await request.post({ url: '/crm/care-rule/execute/' + id })
}
