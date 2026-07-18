import request from '@/config/axios'

export interface ScheduleVO {
  id: number
  title: string
  description: string
  startTime: Date
  endTime: Date
  allDay: boolean
  color: string
  scheduleType: number
  status: number
  ownerUserId: number
  ownerUserName?: string
  relatedTaskId: number
  remark: string
  creator: string
  creatorName?: string
  createTime: Date
}

export const getSchedulePage = async (params: any) => {
  return await request.get({ url: `/crm/schedule/page`, params })
}

export const getSchedule = async (id: number) => {
  return await request.get({ url: `/crm/schedule/get?id=` + id })
}

export const createSchedule = async (data: ScheduleVO) => {
  return await request.post({ url: `/crm/schedule/create`, data })
}

export const updateSchedule = async (data: ScheduleVO) => {
  return await request.put({ url: `/crm/schedule/update`, data })
}

export const deleteSchedule = async (id: number) => {
  return await request.delete({ url: `/crm/schedule/delete?id=` + id })
}

export const getScheduleListByRange = async (start: string, end: string) => {
  return await request.get({ url: `/crm/schedule/list-by-range`, params: { start, end } })
}
