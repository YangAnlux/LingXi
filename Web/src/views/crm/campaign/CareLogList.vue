<template>
  <ContentWrap>
    <el-form class="-mb-15px" :model="queryParams" ref="queryFormRef" label-width="auto">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="t('campaign.careLog.careType')" prop="careType">
            <el-select v-model="queryParams.careType" :placeholder="t('common.all')" class="!w-240px" clearable>
              <el-option :value="1" :label="t('campaign.careLog.careTypeBirthday')" />
              <el-option :value="2" :label="t('campaign.careLog.careTypeHoliday')" />
              <el-option :value="3" :label="t('campaign.careLog.careTypeManual')" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('campaign.careLog.channel')" prop="channel">
            <el-select v-model="queryParams.channel" :placeholder="t('common.all')" class="!w-240px" clearable>
              <el-option :value="1" :label="t('campaign.send.sms')" />
              <el-option :value="2" :label="t('campaign.send.mail')" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('campaign.careLog.status')" prop="status">
            <el-select v-model="queryParams.status" :placeholder="t('common.all')" class="!w-240px" clearable>
              <el-option :value="0" :label="t('campaign.careLog.statusSuccess')" />
              <el-option :value="1" :label="t('campaign.careLog.statusFail')" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item>
            <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> {{ t('common.search') }}</el-button>
            <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> {{ t('common.reset') }}</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true">
      <el-table-column :label="t('campaign.careLog.customerName')" align="center" prop="customerName" min-width="120" />
      <el-table-column :label="t('campaign.careLog.careType')" align="center" min-width="100">
        <template #default="scope">
          <el-tag :type="scope.row.careType === 1 ? 'warning' : scope.row.careType === 2 ? 'success' : 'info'">
            {{ getCareTypeLabel(scope.row.careType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="t('campaign.careLog.channel')" align="center" min-width="80">
        <template #default="scope">
          {{ scope.row.channel === 1 ? t('campaign.send.sms') : t('campaign.send.mail') }}
        </template>
      </el-table-column>
      <el-table-column :label="t('campaign.careLog.templateCode')" align="center" prop="templateCode" min-width="150" />
      <el-table-column :label="t('campaign.careLog.content')" align="center" prop="content" min-width="200" show-overflow-tooltip />
      <el-table-column :label="t('campaign.careLog.status')" align="center" min-width="80">
        <template #default="scope">
          <el-tag :type="scope.row.status === 0 ? 'success' : 'danger'">
            {{ scope.row.status === 0 ? t('campaign.careLog.statusSuccess') : t('campaign.careLog.statusFail') }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="t('campaign.careLog.sendTime')" align="center" min-width="160">
        <template #default="scope">
          {{ formatDateTime(scope.row.sendTime) }}
        </template>
      </el-table-column>
    </el-table>
    <Pagination :total="total" v-model:page="queryParams.pageNo" v-model:limit="queryParams.pageSize" @pagination="getList" />
  </ContentWrap>
</template>

<script setup lang="ts">
import * as SendApi from '@/api/crm/campaign/send'
import dayjs from 'dayjs'

defineOptions({ name: 'CrmCareLog' })

const { t } = useI18n('crm')
const loading = ref(true)
const total = ref(0)
const list = ref([])
const queryParams = reactive({
  pageNo: 1, pageSize: 10,
  careType: null, channel: null, status: null
})
const queryFormRef = ref()

const getCareTypeLabel = (type: number) => {
  const labels: Record<number, string> = { 1: t('campaign.careLog.careTypeBirthday'), 2: t('campaign.careLog.careTypeHoliday'), 3: t('campaign.careLog.careTypeManual') }
  return labels[type] || ''
}

const formatDateTime = (v: any) => {
  if (!v) return ''
  const d = dayjs(v)
  return d.isValid() ? d.format('YYYY-MM-DD HH:mm:ss') : ''
}

const getList = async () => {
  loading.value = true
  try {
    const data = await SendApi.getCareLogPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally { loading.value = false }
}

const handleQuery = () => { queryParams.pageNo = 1; getList() }
const resetQuery = () => { queryFormRef.value.resetFields(); handleQuery() }

onMounted(() => getList())
</script>
