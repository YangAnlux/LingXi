<template>
  <!-- 筛选栏 -->
  <ContentWrap>
    <el-form :inline="true" :model="queryParams">
      <el-form-item :label="t('campaign.statistics.taskType')">
        <el-select v-model="queryParams.taskType" :placeholder="t('common.all')" clearable @change="loadDashboard" class="!w-180px">
          <el-option :value="'MARKETING'" :label="t('campaign.statistics.taskTypeCampaign')" />
          <el-option :value="'AUTO_CARE'" :label="t('campaign.statistics.taskTypeCare')" />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('campaign.statistics.channel')">
        <el-select v-model="queryParams.channel" :placeholder="t('common.all')" clearable @change="loadDashboard" class="!w-160px">
          <el-option :value="'SMS'" :label="t('campaign.send.sms')" />
          <el-option :value="'EMAIL'" :label="t('campaign.send.mail')" />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('campaign.statistics.timeRange')">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          :start-placeholder="t('campaign.startTimePlaceholder')"
          :end-placeholder="t('campaign.endTimePlaceholder')"
          value-format="YYYY-MM-DD"
          @change="loadDashboard"
          class="!w-260px"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadDashboard"><Icon icon="ep:search" class="mr-5px" /> {{ t('common.search') }}</el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 4 统计卡片 -->
  <el-row :gutter="16" class="mt-16px" v-loading="cardLoading">
    <el-col :span="6">
      <el-card shadow="never">
        <el-statistic :title="t('campaign.statistics.todaySent')" :value="dashboard.todaySent || 0">
          <template #prefix><Icon icon="ep:message" class="mr-4px" /></template>
        </el-statistic>
      </el-card>
    </el-col>
    <el-col :span="6">
      <el-card shadow="never">
        <el-statistic :title="t('campaign.statistics.todaySuccess')" :value="dashboard.todaySuccess || 0">
          <template #prefix><Icon icon="ep:circle-check" class="mr-4px" style="color:#67c23a;" /></template>
        </el-statistic>
      </el-card>
    </el-col>
    <el-col :span="6">
      <el-card shadow="never">
        <el-statistic :title="t('campaign.statistics.todayFail')" :value="dashboard.todayFail || 0">
          <template #prefix><Icon icon="ep:circle-close" class="mr-4px" style="color:#f56c6c;" /></template>
        </el-statistic>
      </el-card>
    </el-col>
    <el-col :span="6">
      <el-card shadow="never">
        <el-statistic :title="t('campaign.statistics.deliverRate')" :value="(dashboard.deliverRate || 0) + '%'" />
      </el-card>
    </el-col>
  </el-row>

  <!-- ECharts 7天趋势图 -->
  <ContentWrap :title="t('campaign.statistics.trendTitle')" class="mt-16px" v-loading="chartLoading">
    <Echart :height="400" :options="trendChartOption" />
  </ContentWrap>

  <!-- 任务明细表格 -->
  <ContentWrap :title="t('campaign.statistics.taskDetail')" class="mt-16px" v-loading="tableLoading">
    <el-table :data="taskList" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column :label="t('campaign.statistics.taskId')" align="center" prop="taskId" min-width="80" />
      <el-table-column :label="t('campaign.statistics.taskType')" align="center" min-width="100">
        <template #default="{ row }">
          <el-tag :type="row.taskType === 'MARKETING' ? '' : 'success'" size="small">
            {{ row.taskType === 'MARKETING' ? t('campaign.statistics.taskTypeCampaign') : t('campaign.statistics.taskTypeCare') }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="t('campaign.statistics.channel')" align="center" min-width="80">
        <template #default="{ row }">
          <el-tag :type="row.channel === 1 ? '' : 'warning'" size="small">
            {{ row.channel === 1 ? t('campaign.send.sms') : t('campaign.send.mail') }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="t('campaign.statistics.totalCount')" align="center" prop="totalCount" min-width="80" />
      <el-table-column :label="t('campaign.statistics.successCount')" align="center" min-width="80">
        <template #default="{ row }">
          <span style="color: #67c23a;">{{ row.successCount || 0 }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="t('campaign.statistics.failCount')" align="center" min-width="80">
        <template #default="{ row }">
          <span :style="{ color: row.failCount > 0 ? '#f56c6c' : '' }">{{ row.failCount || 0 }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="t('campaign.statistics.deliverRate')" align="center" min-width="90">
        <template #default="{ row }">
          {{ row.deliverRate != null ? row.deliverRate + '%' : '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="t('campaign.statistics.status')" align="center" prop="status" min-width="90" />
    </el-table>
    <Pagination
      :total="taskTotal"
      v-model:page="taskPageNo"
      v-model:limit="taskPageSize"
      @pagination="loadTaskList"
    />
  </ContentWrap>
</template>

<script setup lang="ts">
import { EChartsOption } from 'echarts'
import * as SendApi from '@/api/crm/campaign/send'

defineOptions({ name: 'CrmSendStatistics' })

const { t } = useI18n('crm')

const cardLoading = ref(false)
const chartLoading = ref(false)
const tableLoading = ref(false)

const dateRange = ref<string[]>([])
const dashboard = ref<any>({})
const taskList = ref<any[]>([])
const taskTotal = ref(0)
const taskPageNo = ref(1)
const taskPageSize = ref(10)
const trendData = ref<any[]>([])

const queryParams = reactive({
  taskType: null as string | null,
  channel: null as string | null
})

const trendChartOption = computed<EChartsOption>(() => ({
  tooltip: { trigger: 'axis' as const },
  legend: {
    data: [
      t('campaign.statistics.totalCount'),
      t('campaign.statistics.successCount'),
      t('campaign.statistics.failCount')
    ]
  },
  grid: { left: 20, right: 20, bottom: 20, containLabel: true },
  xAxis: {
    type: 'category' as const,
    data: trendData.value.map((p: any) => p.date || ''),
    axisLabel: { rotate: 30 }
  },
  yAxis: { type: 'value' as const },
  series: [
    {
      name: t('campaign.statistics.totalCount'),
      type: 'line',
      data: trendData.value.map((p: any) => p.totalCount || 0),
      smooth: true,
      itemStyle: { color: '#409EFF' }
    },
    {
      name: t('campaign.statistics.successCount'),
      type: 'line',
      data: trendData.value.map((p: any) => p.successCount || 0),
      smooth: true,
      itemStyle: { color: '#67C23A' }
    },
    {
      name: t('campaign.statistics.failCount'),
      type: 'line',
      data: trendData.value.map((p: any) => p.failCount || 0),
      smooth: true,
      itemStyle: { color: '#F56C6C' }
    }
  ],
  toolbox: {
    feature: {
      saveAsImage: { show: true, name: t('campaign.statistics.trendTitle') }
    }
  }
}))

const loadDashboard = async () => {
  cardLoading.value = true
  chartLoading.value = true
  try {
    const params: any = {}
    if (queryParams.taskType) params.bizType = queryParams.taskType
    if (queryParams.channel) params.channel = queryParams.channel
    if (dateRange.value && dateRange.value.length === 2) {
      params.startDate = dateRange.value[0]
      params.endDate = dateRange.value[1]
    }
    const data = await SendApi.getStatisticsDashboard(params)
    dashboard.value = data
    trendData.value = data.trend || []
  } finally {
    cardLoading.value = false
    chartLoading.value = false
  }
}

const loadTaskList = async () => {
  tableLoading.value = true
  try {
    const params: any = {
      pageNo: taskPageNo.value,
      pageSize: taskPageSize.value
    }
    if (queryParams.taskType) params.taskType = queryParams.taskType
    if (queryParams.channel) params.channel = queryParams.channel
    const data = await SendApi.getStatisticsTaskPage(params)
    taskList.value = data.list || []
    taskTotal.value = data.total || 0
  } finally {
    tableLoading.value = false
  }
}

onMounted(() => {
  loadDashboard()
  loadTaskList()
})
</script>
