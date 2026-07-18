<template>
  <ContentWrap>
    <el-form class="-mb-15px" :model="queryParams" ref="queryFormRef" label-width="auto">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="t('workReport.name')" prop="title">
            <el-input v-model="queryParams.title" :placeholder="t('workReport.namePlaceholder')" clearable class="!w-240px" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('workReport.status')" prop="status">
            <el-select v-model="queryParams.status" :placeholder="t('common.all')" class="!w-240px" clearable>
              <el-option :value="0" :label="t('workReport.statusDraft')" />
              <el-option :value="10" :label="t('workReport.statusProcess')" />
              <el-option :value="20" :label="t('workReport.statusApprove')" />
              <el-option :value="30" :label="t('workReport.statusReject')" />
              <el-option :value="40" :label="t('workReport.statusCancel')" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('workReport.reportType')" prop="reportType">
            <el-select v-model="queryParams.reportType" :placeholder="t('common.all')" class="!w-240px" clearable>
              <el-option :value="1" :label="t('workReport.reportTypeDaily')" />
              <el-option :value="2" :label="t('workReport.reportTypeWeekly')" />
              <el-option :value="3" :label="t('workReport.reportTypeMonthly')" />
              <el-option :value="4" :label="t('workReport.reportTypeOther')" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item>
            <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> {{ t('common.search') }}</el-button>
            <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> {{ t('common.reset') }}</el-button>
            <el-button type="primary" @click="openForm('create')" v-hasPermi="['crm:work-report:create']">
              <Icon icon="ep:plus" class="mr-5px" /> {{ t('action.add') }}
            </el-button>
            <el-button type="success" plain @click="handleExport" :loading="exportLoading" v-hasPermi="['crm:work-report:export']">
              <Icon icon="ep:download" class="mr-5px" /> {{ t('common.export') }}
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column :label="t('workReport.name')" align="center" prop="title" fixed="left" min-width="160">
        <template #default="scope">
          <el-link :underline="false" type="primary" @click="openForm('update', scope.row.id)">
            {{ scope.row.title }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column :label="t('workReport.reportType')" align="center" min-width="100">
        <template #default="scope">
          <el-tag>{{ getReportTypeLabel(scope.row.reportType) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="t('workReport.status')" align="center" min-width="120">
        <template #default="scope">
          <el-tag :type="getStatusTagType(scope.row.status)">
            {{ getStatusLabel(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="t('workReport.reportDate')" align="center" min-width="120">
        <template #default="scope">
          {{ scope.row.reportDate || '' }}
        </template>
      </el-table-column>
      <el-table-column :label="t('workReport.ownerUserId')" align="center" prop="ownerUserName" min-width="100" />
      <el-table-column :label="t('workReport.auditUserId')" align="center" prop="auditUserName" min-width="100" />
      <el-table-column :label="t('workReport.auditTime')" align="center" min-width="180">
        <template #default="scope">
          {{ formatDateTime(scope.row.auditTime) }}
        </template>
      </el-table-column>
      <el-table-column :label="t('workReport.auditRemark')" align="center" prop="auditRemark" min-width="150" />
      <el-table-column :label="t('workReport.creatorName')" align="center" prop="creatorName" min-width="100" />
      <el-table-column :label="t('workReport.createTime')" align="center" min-width="180">
        <template #default="scope">
          {{ formatDateTime(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column :label="t('common.action')" align="center" min-width="280" fixed="right">
        <template #default="scope">
          <el-button link type="primary" @click="openForm('update', scope.row.id)"
            v-if="scope.row.status === 0" v-hasPermi="['crm:work-report:update']">
            {{ t('common.edit') }}
          </el-button>
          <el-button link type="success" @click="handleSubmit(scope.row)"
            v-if="scope.row.status === 0" v-hasPermi="['crm:work-report:update']">
            {{ t('workReport.statusProcess') }}
          </el-button>
          <el-button link type="success" @click="handleApprove(scope.row, 20)"
            v-if="scope.row.status === 10" v-hasPermi="['crm:work-report:update']">
            {{ t('workReport.auditApprove') }}
          </el-button>
          <el-button link type="danger" @click="handleApprove(scope.row, 30)"
            v-if="scope.row.status === 10" v-hasPermi="['crm:work-report:update']">
            {{ t('workReport.auditReject') }}
          </el-button>
          <el-button link type="warning" @click="handleCancel(scope.row)"
            v-if="scope.row.status === 0 || scope.row.status === 10" v-hasPermi="['crm:work-report:update']">
            {{ t('workReport.statusCancel') }}
          </el-button>
          <el-button link type="danger" @click="handleDelete(scope.row.id)"
            v-if="scope.row.status === 0 || scope.row.status === 40" v-hasPermi="['crm:work-report:delete']">
            {{ t('common.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <Pagination :total="total" v-model:page="queryParams.pageNo" v-model:limit="queryParams.pageSize" @pagination="getList" />
  </ContentWrap>

  <WorkReportForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import download from '@/utils/download'
import * as WorkReportApi from '@/api/crm/workreport'
import WorkReportForm from './WorkReportForm.vue'
import dayjs from 'dayjs'
import { ElMessageBox } from 'element-plus'

defineOptions({ name: 'CrmWorkReport' })

const message = useMessage()
const { t } = useI18n('crm')
const loading = ref(true)
const total = ref(0)
const list = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  title: null,
  status: null,
  reportType: null
})
const queryFormRef = ref()
const exportLoading = ref(false)

const getStatusLabel = (status: number) => {
  const labels: Record<number, string> = {
    0: t('workReport.statusDraft'),
    10: t('workReport.statusProcess'),
    20: t('workReport.statusApprove'),
    30: t('workReport.statusReject'),
    40: t('workReport.statusCancel')
  }
  return labels[status] || ''
}

const getStatusTagType = (status: number) => {
  const types: Record<number, string> = { 0: 'info', 10: 'warning', 20: 'success', 30: 'danger', 40: 'info' }
  return types[status] || 'info'
}

const getReportTypeLabel = (type: number) => {
  const labels: Record<number, string> = { 1: t('workReport.reportTypeDaily'), 2: t('workReport.reportTypeWeekly'), 3: t('workReport.reportTypeMonthly'), 4: t('workReport.reportTypeOther') }
  return labels[type] || ''
}

const formatDateTime = (value: any) => {
  if (value == null || value === '' || value === 0) return ''
  const d = dayjs(value)
  return d.isValid() ? d.format('YYYY-MM-DD HH:mm:ss') : ''
}

const getList = async () => {
  loading.value = true
  try {
    const data = await WorkReportApi.getWorkReportPage(queryParams)
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
    await WorkReportApi.deleteWorkReport(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}

const handleExport = async () => {
  try {
    await message.exportConfirm()
    exportLoading.value = true
    const data = await WorkReportApi.exportWorkReport(queryParams)
    download.excel(data, t('workReport.exportFileName') + '.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

const handleSubmit = async (row: any) => {
  try {
    await message.confirm(t('workReport.submitConfirm', { name: row.title }))
    await WorkReportApi.submitWorkReport(row.id)
    message.success(t('workReport.submitSuccess'))
    await getList()
  } catch {}
}

const handleApprove = async (row: any, status: number) => {
  try {
    const statusLabel = status === 20 ? t('workReport.statusApprove') : t('workReport.statusReject')
    const { value: remark } = await ElMessageBox.prompt(
      t('workReport.auditRemarkPlaceholder'),
      t('workReport.approveConfirm', { name: row.title, status: statusLabel }),
      { confirmButtonText: t('common.confirm'), cancelButtonText: t('common.cancel') }
    )
    await WorkReportApi.approveWorkReport(row.id, status, remark || '')
    message.success(t('workReport.approveSuccess'))
    await getList()
  } catch {}
}

const handleCancel = async (row: any) => {
  try {
    await message.confirm(t('workReport.cancelConfirm', { name: row.title }))
    await WorkReportApi.cancelWorkReport(row.id)
    message.success(t('workReport.cancelSuccess'))
    await getList()
  } catch {}
}

onMounted(() => {
  getList()
})
</script>
