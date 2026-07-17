// 23软4胡伟-202305566535-修改于2026.07.16
// [ADD START] 费用API层 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
import request from '@/config/axios'

export interface ExpenseVO {
  id: number
  no: string
  customerId?: number
  customerName?: string
  contractId?: number
  contractName?: string
  ownerUserId?: number
  ownerUserName?: string
  type: number
  amount: number
  expenseDate?: Date
  remark?: string
  creatorName?: string
  createTime?: Date
  updateTime?: Date
}

// 查询费用分页列表
export const getExpensePage = async (params) => {
  return await request.get({ url: `/crm/expense/page`, params })
}

// 查询费用详情
export const getExpense = async (id: number) => {
  return await request.get({ url: `/crm/expense/get?id=` + id })
}

// 新增费用
export const createExpense = async (data: ExpenseVO) => {
  return await request.post({ url: `/crm/expense/create`, data })
}

// 修改费用
export const updateExpense = async (data: ExpenseVO) => {
  return await request.put({ url: `/crm/expense/update`, data })
}

// 删除费用
export const deleteExpense = async (id: number) => {
  return await request.delete({ url: `/crm/expense/delete?id=` + id })
}

// 导出费用 Excel
export const exportExpense = async (params) => {
  return await request.download({ url: `/crm/expense/export-excel`, params })
}
// [ADD END] 费用API层 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
