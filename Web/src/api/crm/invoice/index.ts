// 23软4胡伟-202305566535-修改于2026.07.14
// [ADD START] 发票API层 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
import request from '@/config/axios'

export interface InvoiceVO {
  id: number
  no: string
  customerId?: number
  customerName?: string
  contractId?: number
  contract?: {
    id?: number
    name?: string
    no?: string
    totalPrice?: number
  }
  ownerUserId?: number
  ownerUserName?: string
  invoiceNo: string
  type: number
  amount: number
  title?: string
  taxNo?: string
  status: number
  issueDate?: Date
  processInstanceId?: string
  auditStatus?: number
  remark?: string
  creator?: string
  creatorName?: string
  createTime?: Date
  updateTime?: Date
}

// 查询发票分页列表
export const getInvoicePage = async (params) => {
  return await request.get({ url: `/crm/invoice/page`, params })
}

// 查询发票详情
export const getInvoice = async (id: number) => {
  return await request.get({ url: `/crm/invoice/get?id=` + id })
}

// 新增发票
export const createInvoice = async (data: InvoiceVO) => {
  return await request.post({ url: `/crm/invoice/create`, data })
}

// 修改发票
export const updateInvoice = async (data: InvoiceVO) => {
  return await request.put({ url: `/crm/invoice/update`, data })
}

// 删除发票
export const deleteInvoice = async (id: number) => {
  return await request.delete({ url: `/crm/invoice/delete?id=` + id })
}

// 导出发票 Excel
export const exportInvoice = async (params) => {
  return await request.download({ url: `/crm/invoice/export-excel`, params })
}
// [ADD END] 发票API层 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
