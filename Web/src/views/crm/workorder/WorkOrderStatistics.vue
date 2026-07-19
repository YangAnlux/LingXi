// 23软件工程4班蔡磊202305566515
<template>
  <div v-loading="loading">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card shadow="hover" v-loading="loading">
          <template #header>{{ t('workorder.statisticsByType') }}</template>
          <el-table :data="data.byType" size="small" max-height="400" empty-text="-">
            <el-table-column prop="name" :label="t('workorder.type')" />
            <el-table-column prop="count" :label="t('workorder.statisticsCount')" align="center" width="80" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" v-loading="loading">
          <template #header>{{ t('workorder.statisticsByStatus') }}</template>
          <el-table :data="data.byStatus" size="small" max-height="400" empty-text="-">
            <el-table-column prop="name" :label="t('workorder.status')" />
            <el-table-column prop="count" :label="t('workorder.statisticsCount')" align="center" width="80" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" v-loading="loading">
          <template #header>{{ t('workorder.statisticsByAssignee') }}</template>
          <el-table :data="data.byAssignee" size="small" max-height="400" empty-text="-">
            <el-table-column prop="name" :label="t('workorder.assigneeName')" />
            <el-table-column prop="count" :label="t('workorder.statisticsCount')" align="center" width="80" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts" setup>
import * as WorkOrderApi from '@/api/crm/workorder'

defineOptions({ name: 'CrmWorkOrderStatistics' })

const { t } = useI18n('crm')
const loading = ref(true)

const data = reactive<WorkOrderApi.WorkOrderStatisticsVO>({
  byType: [],
  byStatus: [],
  byAssignee: []
})

const loadData = async () => {
  loading.value = true
  try {
    const res = await WorkOrderApi.getStatistics()
    if (res) {
      data.byType = res.byType || []
      data.byStatus = res.byStatus || []
      data.byAssignee = res.byAssignee || []
    }
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadData()
})
</script>
