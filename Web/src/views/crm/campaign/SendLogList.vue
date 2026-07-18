<template>
  <ContentWrap>
    <el-form class="-mb-15px" :model="queryParams" ref="queryFormRef" label-width="auto">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="t('campaign.sendLog.campaign')" prop="campaignId">
            <el-select v-model="queryParams.campaignId" :placeholder="t('common.all')" class="!w-240px" clearable>
              <el-option v-for="c in campaignList" :key="c.id" :label="c.name" :value="c.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('campaign.sendLog.channel')" prop="channel">
            <el-select v-model="queryParams.channel" :placeholder="t('common.all')" class="!w-240px" clearable>
              <el-option :value="1" :label="t('campaign.send.sms')" />
              <el-option :value="2" :label="t('campaign.send.mail')" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('campaign.sendLog.sendTime')" prop="sendTime">
            <el-date-picker
              v-model="dateRange"
              type="datetimerange"
              :start-placeholder="t('campaign.startTimePlaceholder')"
              :end-placeholder="t('campaign.endTimePlaceholder')"
              class="!w-240px"
              value-format="YYYY-MM-DD HH:mm:ss"
              @change="onDateRangeChange"
            />
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
      <el-table-column :label="t('campaign.sendLog.campaign')" align="center" prop="campaignName" min-width="140" />
      <el-table-column :label="t('campaign.sendLog.channel')" align="center" min-width="80">
        <template #default="scope">
          <el-tag :type="scope.row.channel === 1 ? '' : 'warning'" size="small">
            {{ scope.row.channel === 1 ? t('campaign.send.sms') : t('campaign.send.mail') }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="t('campaign.sendLog.templateCode')" align="center" prop="templateCode" min-width="160" show-overflow-tooltip />
      <el-table-column :label="t('campaign.sendLog.totalCount')" align="center" prop="totalCount" min-width="80" />
      <el-table-column :label="t('campaign.sendLog.successCount')" align="center" min-width="80">
        <template #default="scope">
          <span style="color: #67c23a;">{{ scope.row.successCount || 0 }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="t('campaign.sendLog.failCount')" align="center" min-width="80">
        <template #default="scope">
          <span :style="{ color: scope.row.failCount > 0 ? '#f56c6c' : '' }">{{ scope.row.failCount || 0 }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="t('campaign.sendLog.openCount')" align="center" min-width="80">
        <template #default="scope">
          {{ scope.row.channel === 2 ? (scope.row.openCount || 0) : '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="t('campaign.sendLog.clickCount')" align="center" min-width="80">
        <template #default="scope">
          {{ scope.row.channel === 2 ? (scope.row.clickCount || 0) : '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="t('campaign.sendLog.sendTime')" align="center" min-width="160">
        <template #default="scope">
          {{ formatDateTime(scope.row.sendTime) }}
        </template>
      </el-table-column>
      <el-table-column :label="t('campaign.sendLog.remark')" align="center" prop="remark" min-width="120" show-overflow-tooltip />
    </el-table>
    <Pagination :total="total" v-model:page="queryParams.pageNo" v-model:limit="queryParams.pageSize" @pagination="getList" />
  </ContentWrap>
</template>

<script setup lang="ts">
import * as CampaignApi from '@/api/crm/campaign'
import * as SendApi from '@/api/crm/campaign/send'
import dayjs from 'dayjs'

defineOptions({ name: 'CrmSendLogList' })

const { t } = useI18n('crm')
const loading = ref(true)
const total = ref(0)
const list = ref([])
const campaignList = ref<any[]>([])
const dateRange = ref<string[]>([])

const queryParams = reactive({
  pageNo: 1, pageSize: 10,
  campaignId: null as number | null,
  channel: null as number | null,
  sendTimeStart: '',
  sendTimeEnd: ''
})
const queryFormRef = ref()

const formatDateTime = (v: any) => {
  if (!v) return ''
  const d = dayjs(v)
  return d.isValid() ? d.format('YYYY-MM-DD HH:mm:ss') : ''
}

const onDateRangeChange = (val: string[]) => {
  if (val && val.length === 2) {
    queryParams.sendTimeStart = val[0]
    queryParams.sendTimeEnd = val[1]
  } else {
    queryParams.sendTimeStart = ''
    queryParams.sendTimeEnd = ''
  }
}

const loadCampaigns = async () => {
  try {
    const data = await CampaignApi.getCampaignPage({ pageNo: 1, pageSize: 200 })
    campaignList.value = data.list || []
  } catch { campaignList.value = [] }
}

const getList = async () => {
  loading.value = true
  try {
    const data = await SendApi.getSendLogPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally { loading.value = false }
}

const handleQuery = () => { queryParams.pageNo = 1; getList() }
const resetQuery = () => {
  dateRange.value = []
  queryParams.sendTimeStart = ''
  queryParams.sendTimeEnd = ''
  queryFormRef.value.resetFields()
  handleQuery()
}

onMounted(async () => {
  await loadCampaigns()
  getList()
})
</script>
