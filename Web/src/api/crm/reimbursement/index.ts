// 23软4胡伟-202305566535-修改于2026.07.16
// [ADD START] 报销API层 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
import request from '@/config/axios'

export interface ReimbursementVO {
  id: number
  no: string
  customerId?: number
  customerName?: string
  contractId?: number
  contractName?: string
  ownerUserId?: number
  ownerUserName?: string
  reimbursementDate?: string
  totalAmount: number
  type: number
  status: number
  remark?: string
  creatorName?: string
  createTime?: Date
  updateTime?: Date
}

// 查询报销分页列表
export const getReimbursementPage = async (params) => {
  return await request.get({ url: `/crm/reimbursement/page`, params })
}

// 查询报销详情
export const getReimbursement = async (id: number) => {
  return await request.get({ url: `/crm/reimbursement/get?id=` + id })
}

// 新增报销
export const createReimbursement = async (data: ReimbursementVO) => {
  return await request.post({ url: `/crm/reimbursement/create`, data })
}

// 修改报销
export const updateReimbursement = async (data: ReimbursementVO) => {
  return await request.put({ url: `/crm/reimbursement/update`, data })
}

// 删除报销
export const deleteReimbursement = async (id: number) => {
  return await request.delete({ url: `/crm/reimbursement/delete?id=` + id })
}

// 提交报销审批
export const submitReimbursement = async (id: number) => {
  return await request.put({ url: `/crm/reimbursement/submit?id=` + id })
}

// 导出报销 Excel
export const exportReimbursement = async (params) => {
  return await request.download({ url: `/crm/reimbursement/export-excel`, params })
}
// [ADD END] 报销API层 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
