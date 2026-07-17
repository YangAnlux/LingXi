// 23软4胡伟-202305566535-修改于2026.07.17
// [ADD START] 退款API层 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
import request from '@/config/axios'

export interface RefundVO {
  id: number
  no: string
  customerId?: number
  customerName?: string
  contractId?: number
  contractName?: string
  orderId?: number
  ownerUserId?: number
  ownerUserName?: string
  refundAmount: number
  refundDate?: string
  refundReason?: string
  refundType: number
  status: number
  remark?: string
  creatorName?: string
  createTime?: Date
  updateTime?: Date
}

// 查询退款分页列表
export const getRefundPage = async (params) => {
  return await request.get({ url: `/crm/refund/page`, params })
}

// 查询退款详情
export const getRefund = async (id: number) => {
  return await request.get({ url: `/crm/refund/get?id=` + id })
}

// 新增退款
export const createRefund = async (data: RefundVO) => {
  return await request.post({ url: `/crm/refund/create`, data })
}

// 修改退款
export const updateRefund = async (data: RefundVO) => {
  return await request.put({ url: `/crm/refund/update`, data })
}

// 删除退款
export const deleteRefund = async (id: number) => {
  return await request.delete({ url: `/crm/refund/delete?id=` + id })
}

// 提交退款审批
export const submitRefund = async (id: number) => {
  return await request.put({ url: `/crm/refund/submit?id=` + id })
}

// 导出退款 Excel
export const exportRefund = async (params) => {
  return await request.download({ url: `/crm/refund/export-excel`, params })
}
// [ADD END] 退款API层 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
