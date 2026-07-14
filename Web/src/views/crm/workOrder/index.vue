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
          <el-form-item :label="t('workOrder.title')" prop="title">
            <el-input
              v-model="queryParams.title"
              :placeholder="t('workOrder.titlePlaceholder')"
              clearable
              @keyup.enter="handleQuery"
              class="!w-240px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('workOrder.status')" prop="status">
            <el-select v-model="queryParams.status" class="!w-240px" clearable>
              <el-option :value="'PENDING'" :label="t('workOrder.statusPending')" />
              <el-option :value="'PROCESSING'" :label="t('workOrder.statusProcessing')" />
              <el-option :value="'RESOLVED'" :label="t('workOrder.statusResolved')" />
              <el-option :value="'REJECTED'" :label="t('workOrder.statusRejected')" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('workOrder.priority')" prop="priority">
            <el-select v-model="queryParams.priority" class="!w-240px" clearable>
              <el-option :value="'LOW'" :label="t('workOrder.priorityLow')" />
              <el-option :value="'NORMAL'" :label="t('workOrder.priorityNormal')" />
              <el-option :value="'HIGH'" :label="t('workOrder.priorityHigh')" />
              <el-option :value="'URGENT'" :label="t('workOrder.priorityUrgent')" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item>
            <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> {{ t('common.search') }}</el-button>
            <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> {{ t('common.reset') }}</el-button>
            <el-button type="primary" @click="openForm('create')" v-hasPermi="['crm:work-order:create']">
              <Icon icon="ep:plus" class="mr-5px" /> {{ t('action.add') }}
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true" :table-layout="'auto'">
      <el-table-column :label="t('workOrder.title')" align="center" prop="title" fixed="left" min-width="200">
        <template #default="scope">
          <el-link :underline="false" type="primary" @click="openDetail(scope.row.id)">
            {{ scope.row.title }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column :label="t('workOrder.type')" align="center" prop="type" min-width="100">
        <template #default="scope">
          <el-tag>{{ scope.row.type || t('common.none') }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="t('workOrder.priority')" align="center" prop="priority" min-width="100">
        <template #default="scope">
          <el-tag :type="getPriorityType(scope.row.priority)">{{ t('workOrder.priority' + scope.row.priority?.replace(/^./, c => c.toLowerCase())) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="t('workOrder.status')" align="center" prop="status" min-width="100">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row.status)">{{ t('workOrder.status' + scope.row.status?.replace(/^./, c => c.toLowerCase())) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="t('workOrder.customerName')" align="center" prop="customerName" min-width="120">
        <template #default="scope">
          {{ scope.row.customerName || t('common.none') }}
        </template>
      </el-table-column>
      <el-table-column :label="t('workOrder.assigneeName')" align="center" prop="assigneeName" min-width="100">
        <template #default="scope">
          {{ scope.row.assigneeName || t('common.none') }}
        </template>
      </el-table-column>
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('workOrder.slaDeadline')"
        prop="slaDeadline"
        min-width="180"
      />
      <el-table-column
        :label="t('workOrder.createTime')"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        min-width="180"
      />
      <el-table-column :label="t('common.action')" align="center" min-width="200" fixed="right">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['crm:work-order:update']"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            v-if="scope.row.status === 'PENDING'"
            link
            type="success"
            @click="handleAssign(scope.row)"
            v-hasPermi="['crm:work-order:update']"
          >
            {{ t('workOrder.assign') }}
          </el-button>
          <el-button
            v-if="scope.row.status === 'PROCESSING'"
            link
            type="warning"
            @click="handleResolve(scope.row.id)"
            v-hasPermi="['crm:work-order:update']"
          >
            {{ t('workOrder.resolve') }}
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['crm:work-order:delete']"
          >
            {{ t('common.delete') }}
          </el-button>
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

  <WorkOrderForm ref="formRef" @success="getList" />

  <el-dialog v-model="assignVisible" :title="t('workOrder.assign')" width="400px">
    <el-form :model="assignForm" label-width="auto">
      <el-form-item :label="t('workOrder.assignee')">
        <el-select v-model="assignForm.assigneeId" class="w-full">
          <el-option v-for="user in userOptions" :key="user.id" :label="user.nickname" :value="user.id" />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="assignVisible = false">{{ t('common.cancel') }}</el-button>
      <el-button type="primary" @click="submitAssign">{{ t('common.ok') }}</el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import * as WorkOrderApi from '@/api/crm/workOrder'
import * as UserApi from '@/api/system/user'
import WorkOrderForm from './WorkOrderForm.vue'

defineOptions({ name: 'CrmWorkOrder' })

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
  priority: null
})
const queryFormRef = ref()
const formRef = ref()

const assignVisible = ref(false)
const assignForm = reactive({
  assigneeId: undefined,
  workOrderId: undefined
})
const userOptions = ref<UserApi.UserVO[]>([])

const getPriorityType = (priority: string) => {
  const types: Record<string, string> = {
    LOW: 'info',
    NORMAL: '',
    HIGH: 'warning',
    URGENT: 'danger'
  }
  return types[priority] || ''
}

const getStatusType = (status: string) => {
  const types: Record<string, string> = {
    PENDING: 'info',
    PROCESSING: 'warning',
    RESOLVED: 'success',
    REJECTED: 'danger'
  }
  return types[status] || ''
}

const getList = async () => {
  loading.value = true
  try {
    const data = await WorkOrderApi.getWorkOrderPage(queryParams)
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
  queryFormRef.value?.resetFields()
  handleQuery()
}

const openDetail = (id: number) => {
  formRef.value.open('update', id)
}

const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

const handleDelete = async (id: number) => {
  try {
    await message.delConfirm()
    await WorkOrderApi.deleteWorkOrder(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}

const handleAssign = async (row: WorkOrderApi.WorkOrderVO) => {
  assignForm.workOrderId = row.id
  assignForm.assigneeId = undefined
  userOptions.value = await UserApi.getSimpleUserList()
  assignVisible.value = true
}

const submitAssign = async () => {
  if (!assignForm.assigneeId) {
    message.warning(t('workOrder.assigneeRequired'))
    return
  }
  try {
    await WorkOrderApi.assignWorkOrder(assignForm.workOrderId, assignForm.assigneeId)
    message.success(t('workOrder.assignSuccess'))
    assignVisible.value = false
    await getList()
  } catch {}
}

const handleResolve = async (id: number) => {
  try {
    await message.confirm(t('workOrder.confirmResolve'))
    await WorkOrderApi.updateWorkOrderStatus(id, 'RESOLVED')
    message.success(t('workOrder.resolveSuccess'))
    await getList()
  } catch {}
}

onMounted(() => {
  getList()
})
</script>