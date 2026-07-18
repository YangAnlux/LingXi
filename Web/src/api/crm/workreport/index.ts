import request from '@/config/axios'

export interface WorkReportVO {
  id: number
  title: string
  content: string
  reportDate: string
  reportType: number
  status: number
  ownerUserId: number
  ownerUserName?: string
  auditUserId: number
  auditUserName?: string
  auditTime?: Date
  auditRemark: string
  relatedCampaignId: number
  relatedCustomerId: number
  remark: string
  creator: string
  creatorName?: string
  createTime: Date
  updateTime: Date
}

export const getWorkReportPage = async (params: any) => {
  return await request.get({ url: `/crm/work-report/page`, params })
}

export const getWorkReport = async (id: number) => {
  return await request.get({ url: `/crm/work-report/get?id=` + id })
}

export const createWorkReport = async (data: WorkReportVO) => {
  return await request.post({ url: `/crm/work-report/create`, data })
}

export const updateWorkReport = async (data: WorkReportVO) => {
  return await request.put({ url: `/crm/work-report/update`, data })
}

export const deleteWorkReport = async (id: number) => {
  return await request.delete({ url: `/crm/work-report/delete?id=` + id })
}

export const exportWorkReport = async (params: any) => {
  return await request.download({ url: `/crm/work-report/export-excel`, params })
}

export const submitWorkReport = async (id: number) => {
  return await request.put({ url: `/crm/work-report/submit`, params: { id } })
}

export const approveWorkReport = async (id: number, status: number, remark?: string) => {
  return await request.put({ url: `/crm/work-report/approve`, params: { id, status, remark } })
}

export const cancelWorkReport = async (id: number) => {
  return await request.put({ url: `/crm/work-report/cancel`, params: { id } })
}
