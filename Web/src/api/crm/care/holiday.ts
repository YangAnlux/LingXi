import request from '@/config/axios'

export interface HolidayVO {
  id: number
  name: string
  holidayDate: string
  templateCode: string
  status: number
  remark: string
  createTime: Date
  updateTime: Date
}

export const getHolidayPage = async (params: any) => {
  return await request.get({ url: '/crm/holiday/page', params })
}

export const getHoliday = async (id: number) => {
  return await request.get({ url: '/crm/holiday/get?id=' + id })
}

export const createHoliday = async (data: HolidayVO) => {
  return await request.post({ url: '/crm/holiday/create', data })
}

export const updateHoliday = async (data: HolidayVO) => {
  return await request.put({ url: '/crm/holiday/update', data })
}

export const deleteHoliday = async (id: number) => {
  return await request.delete({ url: '/crm/holiday/delete?id=' + id })
}

export const listAllEnabledHolidays = async () => {
  return await request.get({ url: '/crm/holiday/list-all' })
}
