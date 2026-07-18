<!-- 商机报表：阶段分布/金额汇总 -->
<template>
  <!-- Echarts 图 -->
  <el-row :gutter="16">
    <el-col :span="12">
      <el-card shadow="never">
        <template #header>{{ t('funnel.stageDistribution') }}</template>
        <el-skeleton :loading="loading" animated>
          <Echart :height="400" :options="pieOption" />
        </el-skeleton>
      </el-card>
    </el-col>
    <el-col :span="12">
      <el-card shadow="never">
        <template #header>{{ t('funnel.amountSummary') }}</template>
        <el-skeleton :loading="loading" animated>
          <Echart :height="400" :options="barOption" />
        </el-skeleton>
      </el-card>
    </el-col>
  </el-row>

  <!-- 统计列表 -->
  <el-card class="mt-16px" shadow="never">
    <el-table v-loading="loading" :data="list" :table-layout="'auto'" show-summary>
      <el-table-column align="center" :label="t('customer.index')" type="index" width="80" />
      <el-table-column align="center" :label="t('funnel.stage')" prop="statusName" min-width="150" />
      <el-table-column align="center" :label="t('funnel.percent')" prop="percent" min-width="100">
        <template #default="scope">
          {{ scope.row.percent ? (scope.row.percent * 100).toFixed(0) + '%' : '-' }}
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :label="t('funnel.businessCount')"
        prop="businessCount"
        min-width="120"
      />
      <el-table-column
        :formatter="erpPriceTableColumnFormatter"
        align="center"
        :label="t('funnel.businessTotalPrice')"
        prop="totalPrice"
        min-width="150"
      />
      <el-table-column align="center" :label="t('funnel.avgPrice')" min-width="150">
        <template #default="scope">
          {{ scope.row.businessCount > 0 ? formatPrice(scope.row.totalPrice / scope.row.businessCount) : '-' }}
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>
<script lang="ts" setup>
import { CrmStatisticsBusinessSummaryByStatusRespVO, StatisticFunnelApi } from '@/api/crm/statistics/funnel'
import { EChartsOption } from 'echarts'
import { erpPriceTableColumnFormatter } from '@/utils'
import { Echart } from '@/components/Echart'
import { useI18n } from 'vue-i18n'

defineOptions({ name: 'BusinessReport' })

const { t } = useI18n('crm.statistics')

const props = defineProps<{ queryParams: any }>()

const loading = ref(false)
const list = ref<CrmStatisticsBusinessSummaryByStatusRespVO[]>([])

/** 饼图配置：阶段分布 */
const pieOption = reactive<EChartsOption>({
  title: {
    text: t('funnel.stageDistribution'),
    left: 'center'
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b}: {c} ({d}%)'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      name: t('funnel.businessCount'),
      type: 'pie',
      radius: '50%',
      data: [],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
}) as EChartsOption

/** 柱状图配置：金额汇总 */
const barOption = reactive<EChartsOption>({
  title: {
    text: t('funnel.amountSummary'),
    left: 'center'
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: { type: 'shadow' }
  },
  grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
  xAxis: {
    type: 'category',
    data: []
  },
  yAxis: {
    type: 'value',
    name: t('funnel.businessTotalPrice')
  },
  series: [
    {
      name: t('funnel.businessTotalPrice'),
      type: 'bar',
      data: [],
      itemStyle: { color: '#409EFF' }
    }
  ]
}) as EChartsOption

/** 格式化价格 */
const formatPrice = (price: number | string) => {
  if (!price) return '0.00'
  return parseFloat(price.toString()).toFixed(2)
}

/** 获取统计数据 */
const loadData = async () => {
  loading.value = true
  try {
    const result = await StatisticFunnelApi.getBusinessSummaryByStatus(props.queryParams)
    const data = result.data as CrmStatisticsBusinessSummaryByStatusRespVO[]
    list.value = data || []
    // 更新饼图
    if (pieOption.series && pieOption.series[0]) {
      pieOption.series[0]['data'] = data.map((item) => ({
        value: item.businessCount || 0,
        name: item.statusName || '-'
      }))
    }
    // 更新柱状图
    if (barOption.xAxis && barOption.series && barOption.series[0]) {
      barOption.xAxis['data'] = data.map((item) => item.statusName || '-')
      barOption.series[0]['data'] = data.map((item) => parseFloat((item.totalPrice || 0).toString()))
    }
  } finally {
    loading.value = false
  }
}

defineExpose({ loadData })

/** 初始化 */
onMounted(() => {
  loadData()
})
</script>
