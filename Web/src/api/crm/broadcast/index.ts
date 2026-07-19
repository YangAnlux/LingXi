import request from '@/config/axios'

export interface BroadcastVO {
  id: number
  no: string
  type: number
  typeName?: string
  title: string
  content: string
  templateCode: string
  customerIds: string
  customerCount: number
  successCount: number
  failCount: number
  status: number
  statusName?: string
  creatorUserId: number
  creatorUserName: string
  remark: string
  sendTime: Date
  createTime: Date
}

export interface BroadcastSaveVO {
  type: number
  title: string
  content: string
  templateCode: string
  customerIds: number[]
  remark: string
}

export const getBroadcastPage = async (params) => {
  return await request.get({ url: '/crm/broadcast/page', params })
}

export const getBroadcast = async (id: number) => {
  return await request.get({ url: `/crm/broadcast/get?id=${id}` })
}

export const createBroadcast = async (data: BroadcastSaveVO) => {
  return await request.post({ url: '/crm/broadcast/create', data })
}

export const deleteBroadcast = async (id: number) => {
  return await request.delete({ url: `/crm/broadcast/delete?id=${id}` })
}

export const sendBroadcast = async (id: number) => {
  return await request.put({ url: `/crm/broadcast/send?id=${id}` })
}