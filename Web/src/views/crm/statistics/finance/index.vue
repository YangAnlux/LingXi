<template>
  <!-- 23软4胡伟-202305566535-修改于2026.07.17 -->
  <!-- [ADD START] 财务数据汇总页面 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17 -->
  <ContentWrap :title="t('finance.title')">
    <!-- 指标卡片 -->
    <el-row :gutter="20" class="mb-20px">
      <!-- 回款 -->
      <el-col :span="4">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-label">{{ t('finance.receivable') }}</div>
          <div class="stat-count">{{ summary.receivableCount || 0 }}</div>
          <div class="stat-amount">{{ erpPriceTableColumnFormatter({}, {}, summary.receivableTotal || 0) }}</div>
        </el-card>
      </el-col>
      <!-- 发票 -->
      <el-col :span="4">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-label">{{ t('finance.invoice') }}</div>
          <div class="stat-count">{{ summary.invoiceCount || 0 }}</div>
          <div class="stat-amount">{{ erpPriceTableColumnFormatter({}, {}, summary.invoiceTotal || 0) }}</div>
        </el-card>
      </el-col>
      <!-- 费用 -->
      <el-col :span="4">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-label">{{ t('finance.expense') }}</div>
          <div class="stat-count">{{ summary.expenseCount || 0 }}</div>
          <div class="stat-amount">{{ erpPriceTableColumnFormatter({}, {}, summary.expenseTotal || 0) }}</div>
        </el-card>
      </el-col>
      <!-- 报销 -->
      <el-col :span="4">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-label">{{ t('finance.reimbursement') }}</div>
          <div class="stat-count">{{ summary.reimbursementCount || 0 }}</div>
          <div class="stat-amount">{{ erpPriceTableColumnFormatter({}, {}, summary.reimbursementTotal || 0) }}</div>
        </el-card>
      </el-col>
      <!-- 退款 -->
      <el-col :span="4">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-label">{{ t('finance.refund') }}</div>
          <div class="stat-count">{{ summary.refundCount || 0 }}</div>
          <div class="stat-amount">{{ erpPriceTableColumnFormatter({}, {}, summary.refundTotal || 0) }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <div ref="countChartRef" style="height: 350px" />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div ref="amountChartRef" style="height: 350px" />
        </el-card>
      </el-col>
    </el-row>
  </ContentWrap>
  <!-- [ADD END] 财务数据汇总页面 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17 -->
</template>

<script lang="ts" setup>
import * as echarts from 'echarts'
import { onMounted, ref, nextTick } from 'vue'
import * as FinanceApi from '@/api/crm/financeStatistics'
import type { FinanceStatisticsVO } from '@/api/crm/financeStatistics'
import { erpPriceTableColumnFormatter } from '@/utils'

defineOptions({ name: 'CrmFinanceStatistics' })

const { t } = useI18n('crm')
const summary = ref<FinanceStatisticsVO>({} as FinanceStatisticsVO)
const countChartRef = ref<HTMLElement>()
const amountChartRef = ref<HTMLElement>()

const loadData = async () => {
  summary.value = await FinanceApi.getFinanceSummary()
  await nextTick()
  renderCharts()
}

const renderCharts = () => {
  const categories = [
    t('finance.receivable'),
    t('finance.invoice'),
    t('finance.expense'),
    t('finance.reimbursement'),
    t('finance.refund')
  ]
  const countData = [
    summary.value.receivableCount || 0,
    summary.value.invoiceCount || 0,
    summary.value.expenseCount || 0,
    summary.value.reimbursementCount || 0,
    summary.value.refundCount || 0
  ]
  const amountData = [
    Number(summary.value.receivableTotal || 0),
    Number(summary.value.invoiceTotal || 0),
    Number(summary.value.expenseTotal || 0),
    Number(summary.value.reimbursementTotal || 0),
    Number(summary.value.refundTotal || 0)
  ]

  // 笔数柱状图
  if (countChartRef.value) {
    const countChart = echarts.init(countChartRef.value)
    countChart.setOption({
      title: { text: t('finance.countChart') },
      tooltip: {},
      xAxis: { data: categories, axisLabel: { rotate: 20 } },
      yAxis: {},
      series: [{ name: t('finance.count'), type: 'bar', data: countData }]
    })
  }

  // 金额柱状图
  if (amountChartRef.value) {
    const amountChart = echarts.init(amountChartRef.value)
    amountChart.setOption({
      title: { text: t('finance.amountChart') },
      tooltip: {},
      xAxis: { data: categories, axisLabel: { rotate: 20 } },
      yAxis: {},
      series: [{ name: t('finance.amount'), type: 'bar', data: amountData }]
    })
  }
}

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.stat-card {
  text-align: center;
  .stat-label {
    font-size: 14px;
    color: #909399;
    margin-bottom: 8px;
  }
  .stat-count {
    font-size: 28px;
    font-weight: bold;
    color: #303133;
  }
  .stat-amount {
    font-size: 14px;
    color: #67C23A;
    margin-top: 4px;
  }
}
</style>
