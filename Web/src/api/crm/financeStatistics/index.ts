// 23软4胡伟-202305566535-修改于2026.07.17
// [ADD START] 财务统计API - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
import request from '@/config/axios'

export interface FinanceStatisticsVO {
  receivableCount: number
  receivableTotal: number
  invoiceCount: number
  invoiceTotal: number
  expenseCount: number
  expenseTotal: number
  reimbursementCount: number
  reimbursementTotal: number
  refundCount: number
  refundTotal: number
}

// 获取财务汇总数据
export const getFinanceSummary = async (): Promise<FinanceStatisticsVO> => {
  return await request.get({ url: '/crm/finance-statistics/summary' })
}
// [ADD END] 财务统计API - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
