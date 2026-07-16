// 2023级软4蔡磊202305566515,2026年7月14日
<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form class="-mb-15px" :model="queryParams" ref="queryFormRef" label-width="auto">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="t('workorder.title')" prop="title">
            <el-input
              v-model="queryParams.title"
              :placeholder="t('workorder.titlePlaceholder')"
              clearable
              @keyup.enter="handleQuery"
              class="!w-240px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('workorder.status')" prop="status">
            <el-select v-model="queryParams.status" clearable class="!w-240px">
              <el-option value="待处理" :label="t('workorder.statusPending')" />
              <el-option value="处理中" :label="t('workorder.statusProcessing')" />
              <el-option value="已完结" :label="t('workorder.statusResolved')" />
              <el-option value="已退回" :label="t('workorder.statusReturned')" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('workorder.priority')" prop="priority">
            <el-select v-model="queryParams.priority" clearable class="!w-240px">
              <el-option value="LOW" :label="t('workorder.priorityLow')" />
              <el-option value="NORMAL" :label="t('workorder.priorityNormal')" />
              <el-option value="HIGH" :label="t('workorder.priorityHigh')" />
              <el-option value="URGENT" :label="t('workorder.priorityUrgent')" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="t('workorder.type')" prop="type">
            <el-input
              v-model="queryParams.type"
              :placeholder="t('workorder.typePlaceholder')"
              clearable
              @keyup.enter="handleQuery"
              class="!w-240px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('workorder.assigneeId')" prop="assigneeId">
            <el-input
              v-model="queryParams.assigneeId"
              :placeholder="t('workorder.assigneePlaceholder')"
              clearable
              @keyup.enter="handleQuery"
              class="!w-240px"
            />
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

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true" :table-layout="'auto'">
      <el-table-column :label="t('workorder.id')" align="center" prop="id" width="80" />
      <el-table-column :label="t('workorder.title')" align="center" prop="title" min-width="180" fixed="left">
        <template #default="scope">
          <el-link :underline="false" type="primary">
            {{ scope.row.title }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column :label="t('workorder.type')" align="center" prop="type" min-width="120" />
      <el-table-column :label="t('workorder.priority')" align="center" prop="priority" min-width="100">
        <template #default="scope">
          <el-tag v-if="scope.row.priority === 'URGENT'" type="danger" size="small">{{ t('workorder.priorityUrgent') }}</el-tag>
          <el-tag v-else-if="scope.row.priority === 'HIGH'" type="warning" size="small">{{ t('workorder.priorityHigh') }}</el-tag>
          <el-tag v-else-if="scope.row.priority === 'NORMAL'" type="info" size="small">{{ t('workorder.priorityNormal') }}</el-tag>
          <el-tag v-else-if="scope.row.priority === 'LOW'" size="small">{{ t('workorder.priorityLow') }}</el-tag>
          <span v-else>{{ scope.row.priority }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="t('workorder.status')" align="center" prop="status" min-width="100">
        <template #default="scope">
          <el-tag v-if="scope.row.status === '待处理'" type="info" size="small">{{ t('workorder.statusPending') }}</el-tag>
          <el-tag v-else-if="scope.row.status === '处理中'" type="primary" size="small">{{ t('workorder.statusProcessing') }}</el-tag>
          <el-tag v-else-if="scope.row.status === '已完结'" type="success" size="small">{{ t('workorder.statusResolved') }}</el-tag>
          <el-tag v-else-if="scope.row.status === '已退回'" type="danger" size="small">{{ t('workorder.statusReturned') }}</el-tag>
          <span v-else>{{ scope.row.status }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="t('workorder.customerName')" align="center" prop="customerName" min-width="140" />
      <el-table-column :label="t('workorder.assigneeName')" align="center" prop="assigneeName" min-width="100" />
      <el-table-column
        :label="t('workorder.slaDeadline')"
        align="center"
        prop="slaDeadline"
        :formatter="dateFormatter"
        min-width="180"
      />
      <el-table-column :label="t('workorder.isSlaBreached')" align="center" prop="isSlaBreached" min-width="100">
        <template #default="scope">
          <el-tag v-if="scope.row.isSlaBreached" type="danger" size="small">{{ t('workorder.slaBreached') }}</el-tag>
          <el-tag v-else type="success" size="small">{{ t('workorder.slaNormal') }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('workorder.createTime')"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        min-width="180"
      />
      <el-table-column align="center" :label="t('workorder.creatorName')" prop="creatorName" min-width="100" />
      <el-table-column :label="t('common.action')" align="center" min-width="280" fixed="right">
        <template #default="scope">
          <el-button
            link
            type="primary"
            size="small"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['crm:work-order:update']"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            v-if="scope.row.status === '待处理'"
            link
            type="success"
            size="small"
            @click="handleTransition(scope.row.id, '处理中')"
            v-hasPermi="['crm:work-order:update']"
          >
            {{ t('workorder.transitionProcess') }}
          </el-button>
          <el-button
            v-if="scope.row.status === '处理中'"
            link
            type="success"
            size="small"
            @click="handleTransition(scope.row.id, '已完结')"
            v-hasPermi="['crm:work-order:update']"
          >
            {{ t('workorder.transitionResolve') }}
          </el-button>
          <el-button
            v-if="scope.row.status === '处理中'"
            link
            type="warning"
            size="small"
            @click="handleTransition(scope.row.id, '已退回')"
            v-hasPermi="['crm:work-order:update']"
          >
            {{ t('workorder.transitionReturn') }}
          </el-button>
          <el-button
            link
            type="danger"
            size="small"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['crm:work-order:delete']"
          >
            {{ t('common.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>
  <!-- 表单弹窗：添加/修改 -->
  <WorkOrderForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { dateFormatter } from '@/utils/formatTime'
import * as WorkOrderApi from '@/api/crm/workorder'
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
  type: null,
  priority: null,
  status: null,
  assigneeId: null
})
const queryFormRef = ref()

/** 查询列表 */
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

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    await message.delConfirm()
    await WorkOrderApi.deleteWorkOrder(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

// 2023级软4蔡磊202305566515,2026年7月14日
/** 状态流转操作 */
const handleTransition = async (id: number, status: string) => {
  try {
    const labelMap: Record<string, string> = {
      '处理中': t('workorder.transitionProcess'),
      '已完结': t('workorder.transitionResolve'),
      '已退回': t('workorder.transitionReturn')
    }
    await message.confirm(t('common.confirmText', { text: labelMap[status] }))
    await WorkOrderApi.transitionWorkOrderStatus(id, status)
    message.success(t('common.success'))
    await getList()
  } catch {}
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>
