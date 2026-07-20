<template>
  <ContentWrap>
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      label-width="auto"
    >
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="t('campaign.name')" prop="name">
            <el-input
              v-model="queryParams.name"
              :placeholder="t('campaign.namePlaceholder')"
              clearable
              @keyup.enter="handleQuery"
              class="!w-240px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('campaign.status')" prop="status">
            <el-select v-model="queryParams.status" :placeholder="t('common.all')" class="!w-240px" clearable>
              <el-option :value="1" :label="t('campaign.statusDraft')" />
              <el-option :value="2" :label="t('campaign.statusRunning')" />
              <el-option :value="3" :label="t('campaign.statusEnded')" />
              <el-option :value="4" :label="t('campaign.statusCancelled')" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="t('campaign.startTime')" prop="startTime">
            <el-date-picker
              v-model="queryParams.startTime"
              type="datetime"
              :placeholder="t('campaign.startTimePlaceholder')"
              class="!w-240px"
              value-format="YYYY-MM-DD HH:mm:ss"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('campaign.endTime')" prop="endTime">
            <el-date-picker
              v-model="queryParams.endTime"
              type="datetime"
              :placeholder="t('campaign.endTimePlaceholder')"
              class="!w-240px"
              value-format="YYYY-MM-DD HH:mm:ss"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item>
            <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> {{ t('common.search') }}</el-button>
            <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> {{ t('common.reset') }}</el-button>
            <el-button type="primary" @click="openForm('create')" v-hasPermi="['crm:campaign:create']">
              <Icon icon="ep:plus" class="mr-5px" /> {{ t('action.add') }}
            </el-button>
            <el-button
              type="success"
              plain
              @click="handleExport"
              :loading="exportLoading"
              v-hasPermi="['crm:campaign:export']"
            >
              <Icon icon="ep:download" class="mr-5px" /> {{ t('common.export') }}
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true" :table-layout="'auto'">
      <el-table-column :label="t('campaign.name')" align="center" prop="name" fixed="left" min-width="160">
        <template #default="scope">
          <el-link :underline="false" type="primary" @click="openForm('update', scope.row.id)">
            {{ scope.row.name }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column :label="t('campaign.status')" align="center" min-width="160">
        <template #default="scope">
          <div style="display: flex; flex-direction: column; align-items: center; gap: 4px;">
            <el-tag :type="getStatusTagType(scope.row.status)">
              {{ getStatusLabel(scope.row.status) }}
            </el-tag>
            <span v-if="scope.row.startTime" style="font-size: 12px; color: #909399;">
              {{ t('campaign.startTime') }}: {{ formatDateTime(scope.row.startTime) }}
            </span>
            <span v-if="scope.row.endTime" style="font-size: 12px; color: #909399;">
              {{ t('campaign.endTime') }}: {{ formatDateTime(scope.row.endTime) }}
            </span>
          </div>
        </template>
      </el-table-column>
      <el-table-column :label="t('campaign.startTime')" align="center" min-width="180">
        <template #default="scope">
          {{ formatDateTime(scope.row.startTime) }}
        </template>
      </el-table-column>
      <el-table-column :label="t('campaign.endTime')" align="center" min-width="180">
        <template #default="scope">
          {{ formatDateTime(scope.row.endTime) }}
        </template>
      </el-table-column>
      <el-table-column :label="t('campaign.budget')" align="center" min-width="120">
        <template #default="scope">
          {{ formatMoney(scope.row.budget) }}
        </template>
      </el-table-column>
      <el-table-column :label="t('campaign.actualCost')" align="center" min-width="120">
        <template #default="scope">
          {{ formatMoney(scope.row.actualCost) }}
        </template>
      </el-table-column>
      <el-table-column :label="t('campaign.ownerUserName')" align="center" prop="ownerUserName" min-width="100" />
      <el-table-column :label="t('campaign.creatorName')" align="center" prop="creatorName" min-width="100" />
      <el-table-column :label="t('campaign.createTime')" align="center" min-width="180">
        <template #default="scope">
          {{ formatDateTime(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column :label="t('common.action')" align="center" min-width="280" fixed="right">
        <template #default="scope">
          <div class="flex items-center justify-center flex-wrap gap-8px">
            <el-button
              link
              type="primary"
              @click="openForm('update', scope.row.id)"
              v-hasPermi="['crm:campaign:update']"
            >
              {{ t('common.edit') }}
            </el-button>
            <el-button
              v-if="scope.row.status === 1"
              link
              type="success"
              @click="handleStart(scope.row)"
              v-hasPermi="['crm:campaign:update']"
            >
              {{ t('campaign.statusRunning') }}
            </el-button>
            <el-button
              v-if="scope.row.status === 2"
              link
              type="warning"
              @click="handleEnd(scope.row)"
              v-hasPermi="['crm:campaign:update']"
            >
              {{ t('campaign.statusEnded') }}
            </el-button>
            <el-button
              v-if="scope.row.status === 1 || scope.row.status === 2"
              link
              type="danger"
              @click="handleCancel(scope.row)"
              v-hasPermi="['crm:campaign:update']"
            >
              {{ t('campaign.statusCancelled') }}
            </el-button>
            <el-button
              link
              type="danger"
              @click="handleDelete(scope.row.id)"
              v-hasPermi="['crm:campaign:delete']"
            >
              {{ t('common.delete') }}
            </el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>

  <CampaignForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import download from '@/utils/download'
import * as CampaignApi from '@/api/crm/campaign'
import CampaignForm from './CampaignForm.vue'
import dayjs from 'dayjs'

defineOptions({ name: 'CrmCampaign' })

const message = useMessage()
const { t } = useI18n('crm')
const loading = ref(true)
const total = ref(0)
const list = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  name: null,
  status: null,
  startTime: null,
  endTime: null
})
const queryFormRef = ref()
const exportLoading = ref(false)

const getStatusLabel = (status: number) => {
  const labels: Record<number, string> = {
    1: t('campaign.statusDraft'),
    2: t('campaign.statusRunning'),
    3: t('campaign.statusEnded'),
    4: t('campaign.statusCancelled')
  }
  return labels[status] || ''
}

const getStatusTagType = (status: number) => {
  const types: Record<number, string> = { 1: 'info', 2: 'success', 3: 'warning', 4: 'danger' }
  return types[status] || 'info'
}

const formatDateTime = (value: any) => {
  // 后端用 epoch 毫秒数 0 表示未设置时间，需要和 null/undefined/空字符串一样处理
  if (value == null || value === '' || value === 0) return ''
  const d = dayjs(value)
  return d.isValid() ? d.format('YYYY-MM-DD HH:mm:ss') : ''
}

const formatMoney = (value: number) => {
  return value != null ? Number(value).toFixed(2) : '0.00'
}

const getList = async () => {
  loading.value = true
  try {
    const data = await CampaignApi.getCampaignPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

const handleDelete = async (id: number) => {
  try {
    await message.delConfirm()
    await CampaignApi.deleteCampaign(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}

const handleExport = async () => {
  try {
    await message.exportConfirm()
    exportLoading.value = true
    const data = await CampaignApi.exportCampaign(queryParams)
    download.excel(data, t('campaign.exportFileName') + '.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

const handleStart = async (row: any) => {
  try {
    await message.confirm(t('campaign.startConfirm', { name: row.name }))
    await CampaignApi.updateCampaignStatus(row.id, 2)
    message.success(t('campaign.updateStatusSuccess'))
    await getList()
  } catch {}
}

const handleEnd = async (row: any) => {
  try {
    await message.confirm(t('campaign.endConfirm', { name: row.name }))
    await CampaignApi.updateCampaignStatus(row.id, 3)
    message.success(t('campaign.updateStatusSuccess'))
    await getList()
  } catch {}
}

const handleCancel = async (row: any) => {
  try {
    await message.confirm(t('campaign.cancelConfirm', { name: row.name }))
    await CampaignApi.updateCampaignStatus(row.id, 4)
    message.success(t('campaign.updateStatusSuccess'))
    await getList()
  } catch {}
}

onMounted(() => {
  getList()
})
</script>
