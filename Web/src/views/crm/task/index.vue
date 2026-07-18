<template>
  <ContentWrap>
    <el-form class="-mb-15px" :model="queryParams" ref="queryFormRef" label-width="auto">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="t('task.name')" prop="name">
            <el-input v-model="queryParams.name" :placeholder="t('task.namePlaceholder')" clearable class="!w-240px" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('task.status')" prop="status">
            <el-select v-model="queryParams.status" :placeholder="t('common.all')" class="!w-240px" clearable>
              <el-option :value="0" :label="t('task.statusTodo')" />
              <el-option :value="1" :label="t('task.statusInProgress')" />
              <el-option :value="2" :label="t('task.statusCompleted')" />
              <el-option :value="3" :label="t('task.statusCancelled')" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('task.priority')" prop="priority">
            <el-select v-model="queryParams.priority" :placeholder="t('common.all')" class="!w-240px" clearable>
              <el-option :value="0" :label="t('task.priorityLow')" />
              <el-option :value="1" :label="t('task.priorityMedium')" />
              <el-option :value="2" :label="t('task.priorityHigh')" />
              <el-option :value="3" :label="t('task.priorityUrgent')" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item>
            <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> {{ t('common.search') }}</el-button>
            <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> {{ t('common.reset') }}</el-button>
            <el-button type="primary" @click="openForm('create')" v-hasPermi="['crm:task:create']">
              <Icon icon="ep:plus" class="mr-5px" /> {{ t('action.add') }}
            </el-button>
            <el-button type="success" plain @click="handleExport" :loading="exportLoading" v-hasPermi="['crm:task:export']">
              <Icon icon="ep:download" class="mr-5px" /> {{ t('common.export') }}
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column :label="t('task.name')" align="center" prop="name" fixed="left" min-width="160">
        <template #default="scope">
          <el-link :underline="false" type="primary" @click="openForm('update', scope.row.id)">
            {{ scope.row.name }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column :label="t('task.status')" align="center" min-width="120">
        <template #default="scope">
          <el-tag :type="getStatusTagType(scope.row.status)">
            {{ getStatusLabel(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="t('task.priority')" align="center" min-width="100">
        <template #default="scope">
          <el-tag :type="getPriorityTagType(scope.row.priority)" :effect="scope.row.priority >= 2 ? 'dark' : 'light'">
            {{ getPriorityLabel(scope.row.priority) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="t('task.startTime')" align="center" min-width="180">
        <template #default="scope">
          {{ formatDateTime(scope.row.startTime) }}
        </template>
      </el-table-column>
      <el-table-column :label="t('task.endTime')" align="center" min-width="180">
        <template #default="scope">
          <span :class="{ 'text-red-500': isOverdue(scope.row) }">
            {{ formatDateTime(scope.row.endTime) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column :label="t('task.ownerUserId')" align="center" prop="ownerUserName" min-width="100" />
      <el-table-column :label="t('task.assignerUserId')" align="center" prop="assignerUserName" min-width="100" />
      <el-table-column :label="t('task.creatorName')" align="center" prop="creatorName" min-width="100" />
      <el-table-column :label="t('task.createTime')" align="center" min-width="180">
        <template #default="scope">
          {{ formatDateTime(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column :label="t('common.action')" align="center" min-width="240" fixed="right">
        <template #default="scope">
          <el-button link type="primary" @click="openForm('update', scope.row.id)" v-hasPermi="['crm:task:update']">
            {{ t('common.edit') }}
          </el-button>
          <el-button link type="success" @click="handleStart(scope.row)"
            v-if="scope.row.status === 0" v-hasPermi="['crm:task:update']">
            {{ t('task.statusInProgress') }}
          </el-button>
          <el-button link type="primary" @click="handleComplete(scope.row)"
            v-if="scope.row.status === 1" v-hasPermi="['crm:task:update']">
            {{ t('task.statusCompleted') }}
          </el-button>
          <el-button link type="danger" @click="handleCancel(scope.row)"
            v-if="scope.row.status === 0 || scope.row.status === 1" v-hasPermi="['crm:task:update']">
            {{ t('task.statusCancelled') }}
          </el-button>
          <el-button link type="danger" @click="handleDelete(scope.row.id)" v-hasPermi="['crm:task:delete']">
            {{ t('common.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <Pagination :total="total" v-model:page="queryParams.pageNo" v-model:limit="queryParams.pageSize" @pagination="getList" />
  </ContentWrap>

  <TaskForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import download from '@/utils/download'
import * as TaskApi from '@/api/crm/task'
import TaskForm from './TaskForm.vue'
import dayjs from 'dayjs'

defineOptions({ name: 'CrmTask' })

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
  priority: null
})
const queryFormRef = ref()
const exportLoading = ref(false)

const getStatusLabel = (status: number) => {
  const labels: Record<number, string> = {
    0: t('task.statusTodo'), 1: t('task.statusInProgress'),
    2: t('task.statusCompleted'), 3: t('task.statusCancelled')
  }
  return labels[status] || ''
}

const getStatusTagType = (status: number) => {
  const types: Record<number, string> = { 0: 'info', 1: 'warning', 2: 'success', 3: 'info' }
  return types[status] || 'info'
}

const getPriorityLabel = (priority: number) => {
  const labels: Record<number, string> = {
    0: t('task.priorityLow'), 1: t('task.priorityMedium'),
    2: t('task.priorityHigh'), 3: t('task.priorityUrgent')
  }
  return labels[priority] || ''
}

const getPriorityTagType = (priority: number) => {
  const types: Record<number, string> = { 0: 'info', 1: 'warning', 2: 'danger', 3: 'danger' }
  return types[priority] || 'info'
}

const formatDateTime = (value: any) => {
  if (value == null || value === '' || value === 0) return ''
  const d = dayjs(value)
  return d.isValid() ? d.format('YYYY-MM-DD HH:mm:ss') : ''
}

const isOverdue = (row: any) => {
  if (!row.endTime || row.status === 2 || row.status === 3) return false
  return dayjs(row.endTime).isBefore(dayjs())
}

const getList = async () => {
  loading.value = true
  try {
    const data = await TaskApi.getTaskPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

const handleQuery = () => { queryParams.pageNo = 1; getList() }
const resetQuery = () => { queryFormRef.value.resetFields(); handleQuery() }

const formRef = ref()
const openForm = (type: string, id?: number) => { formRef.value.open(type, id) }

const handleDelete = async (id: number) => {
  try {
    await message.delConfirm()
    await TaskApi.deleteTask(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}

const handleExport = async () => {
  try {
    await message.exportConfirm()
    exportLoading.value = true
    const data = await TaskApi.exportTask(queryParams)
    download.excel(data, t('task.exportFileName') + '.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

const handleStart = async (row: any) => {
  try {
    await message.confirm(t('task.startConfirm', { name: row.name }))
    await TaskApi.updateTaskStatus(row.id, 1)
    message.success(t('task.updateStatusSuccess'))
    await getList()
  } catch {}
}

const handleComplete = async (row: any) => {
  try {
    await message.confirm(t('task.completeConfirm', { name: row.name }))
    await TaskApi.updateTaskStatus(row.id, 2)
    message.success(t('task.updateStatusSuccess'))
    await getList()
  } catch {}
}

const handleCancel = async (row: any) => {
  try {
    await message.confirm(t('task.cancelConfirm', { name: row.name }))
    await TaskApi.updateTaskStatus(row.id, 3)
    message.success(t('task.updateStatusSuccess'))
    await getList()
  } catch {}
}

onMounted(() => { getList() })
</script>
