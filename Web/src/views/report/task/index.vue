<!-- 23软2张奎良-202305566305 -->
<template>
  <ContentWrap>
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      label-width="auto"
    >
      <el-row :gutter="20">
        <el-col :span="6">
          <el-form-item :label="t('report.task.title')" prop="title">
            <el-input
              v-model="queryParams.title"
              :placeholder="t('report.task.titlePlaceholder')"
              clearable
              @keyup.enter="handleQuery"
              class="!w-240px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item :label="t('report.task.type')" prop="type">
            <el-select
              v-model="queryParams.type"
              :placeholder="t('report.task.typePlaceholder')"
              clearable
              class="!w-240px"
            >
              <el-option
                v-for="item in typeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item :label="t('report.task.status')" prop="status">
            <el-select
              v-model="queryParams.status"
              :placeholder="t('report.task.statusPlaceholder')"
              clearable
              class="!w-240px"
            >
              <el-option
                v-for="item in statusOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item :label="t('report.task.priority')" prop="priority">
            <el-select
              v-model="queryParams.priority"
              :placeholder="t('report.task.priorityPlaceholder')"
              clearable
              class="!w-240px"
            >
              <el-option
                v-for="item in priorityOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item>
            <el-button @click="handleQuery"><Icon icon="ep:search" />{{ t('common.query') }}</el-button>
            <el-button @click="resetQuery"><Icon icon="ep:refresh" />{{ t('common.reset') }}</el-button>
            <el-button
              type="primary"
              plain
              @click="openForm('create')"
              v-hasPermi="['report:task:create']"
            >
              <Icon icon="ep:plus" /> {{ t('action.add') }}
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :table-layout="'auto'" @selection-change="handleRowCheckboxChange">
      <el-table-column type="selection" width="55" />
      <el-table-column :label="t('common.id')" align="center" prop="id" width="80" />
      <el-table-column
        :label="t('report.task.title')"
        align="center"
        prop="title"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        :label="t('report.task.type')"
        align="center"
        prop="type"
        width="120"
      >
        <template #default="scope">
          <el-tag :type="getTypeTagType(scope.row.type)">
            {{ getTypeLabel(scope.row.type) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('report.task.priority')"
        align="center"
        prop="priority"
        width="100"
      >
        <template #default="scope">
          <el-tag :type="getPriorityTagType(scope.row.priority)" effect="dark">
            {{ getPriorityLabel(scope.row.priority) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('report.task.status')"
        align="center"
        prop="status"
        width="100"
      >
        <template #default="scope">
          <el-tag :type="getStatusTagType(scope.row.status)">
            {{ getStatusLabel(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('report.task.assigneeName')"
        align="center"
        prop="assigneeName"
        width="120"
      />
      <el-table-column
        :label="t('report.task.deptName')"
        align="center"
        prop="deptName"
        width="120"
      />
      <el-table-column
        :label="t('report.task.startDate')"
        align="center"
        prop="startDate"
        width="120"
      />
      <el-table-column
        :label="t('report.task.endDate')"
        align="center"
        prop="endDate"
        width="120"
      />
      <el-table-column
        :label="t('report.task.progress')"
        align="center"
        prop="progress"
        width="160"
      >
        <template #default="scope">
          <el-progress :percentage="scope.row.progress || 0" :width="120" :show-text="true" />
        </template>
      </el-table-column>
      <el-table-column :label="t('common.operation')" align="center" min-width="300">
        <template #default="scope">
          <div class="flex items-center justify-center">
            <el-button
              type="primary"
              link
              @click="openForm('update', scope.row.id)"
              v-hasPermi="['report:task:update']"
              v-if="scope.row.status === TaskStatus.PENDING"
            >
              <Icon icon="ep:edit" />{{ t('action.edit') }}
            </el-button>
            <el-button
              type="success"
              link
              @click="handleAssign(scope.row.id)"
              v-hasPermi="['report:task:assign']"
              v-if="scope.row.status === TaskStatus.PENDING"
            >
              <Icon icon="ep:user" />{{ t('report.task.assign') }}
            </el-button>
            <el-button
              type="primary"
              link
              @click="handleStart(scope.row.id)"
              v-hasPermi="['report:task:start']"
              v-if="scope.row.status === TaskStatus.PENDING || scope.row.status === TaskStatus.CANCELLED"
            >
              <Icon icon="ep:play" />{{ t('report.task.start') }}
            </el-button>
            <el-button
              type="success"
              link
              @click="handleComplete(scope.row.id)"
              v-hasPermi="['report:task:complete']"
              v-if="scope.row.status === TaskStatus.IN_PROGRESS"
            >
              <Icon icon="ep:check" />{{ t('report.task.complete') }}
            </el-button>
            <el-button
              type="warning"
              link
              @click="handleCancel(scope.row.id)"
              v-hasPermi="['report:task:cancel']"
              v-if="scope.row.status === TaskStatus.PENDING || scope.row.status === TaskStatus.IN_PROGRESS"
            >
              <Icon icon="ep:close" />{{ t('report.task.cancel') }}
            </el-button>
            <el-button
              type="danger"
              link
              @click="handleDelete(scope.row.id)"
              v-hasPermi="['report:task:delete']"
            >
              <Icon icon="ep:delete" />{{ t('action.del') }}
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

  <TaskForm ref="formRef" @success="getList" />
</template>
<script lang="ts" setup>
// 23软2张奎良-202305566305
import { dateFormatter } from '@/utils/formatTime'
import { TaskType, TaskPriority, TaskStatus } from '@/api/report/task'
import * as TaskApi from '@/api/report/task'
import TaskForm from './TaskForm.vue'
import { ElMessageBox } from 'element-plus'

/** 组件名称 */
defineOptions({ name: 'Task' })

/** 消息弹窗 */
const message = useMessage()
/** 国际化 */
const { t } = useI18n()

/** 列表加载状态 */
const loading = ref(true)
/** 列表总数 */
const total = ref(0)
/** 列表数据 */
const list = ref([])
/** 查询参数 */
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  title: undefined,
  type: undefined,
  status: undefined,
  priority: undefined,
  dateRange: []
})
/** 查询表单引用 */
const queryFormRef = ref()
/** 表单弹窗引用 */
const formRef = ref()

/** 任务类型选项 */
const typeOptions = [
  { label: t('report.task.typeDaily'), value: TaskType.DAILY },
  { label: t('report.task.typeEmergency'), value: TaskType.EMERGENCY },
  { label: t('report.task.typeProject'), value: TaskType.PROJECT },
  { label: t('report.task.typeTemporary'), value: TaskType.TEMPORARY }
]

/** 任务优先级选项 */
const priorityOptions = [
  { label: t('report.task.priorityLow'), value: TaskPriority.LOW },
  { label: t('report.task.priorityMedium'), value: TaskPriority.MEDIUM },
  { label: t('report.task.priorityHigh'), value: TaskPriority.HIGH },
  { label: t('report.task.priorityUrgent'), value: TaskPriority.URGENT }
]

/** 任务状态选项 */
const statusOptions = [
  { label: t('report.task.statusPending'), value: TaskStatus.PENDING },
  { label: t('report.task.statusInProgress'), value: TaskStatus.IN_PROGRESS },
  { label: t('report.task.statusCompleted'), value: TaskStatus.COMPLETED },
  { label: t('report.task.statusCancelled'), value: TaskStatus.CANCELLED }
]

/**
 * 获取任务类型标签样式
 * @param type 类型值
 * @returns 标签样式
 */
const getTypeTagType = (type: number) => {
  switch (type) {
    case TaskType.DAILY:
      return 'primary'
    case TaskType.EMERGENCY:
      return 'danger'
    case TaskType.PROJECT:
      return 'success'
    case TaskType.TEMPORARY:
      return 'warning'
    default:
      return 'info'
  }
}

/**
 * 获取任务类型标签文字
 * @param type 类型值
 * @returns 类型名称
 */
const getTypeLabel = (type: number) => {
  switch (type) {
    case TaskType.DAILY:
      return t('report.task.typeDaily')
    case TaskType.EMERGENCY:
      return t('report.task.typeEmergency')
    case TaskType.PROJECT:
      return t('report.task.typeProject')
    case TaskType.TEMPORARY:
      return t('report.task.typeTemporary')
    default:
      return '-'
  }
}

/**
 * 获取任务优先级标签样式
 * @param priority 优先级值
 * @returns 标签样式
 */
const getPriorityTagType = (priority: number) => {
  switch (priority) {
    case TaskPriority.LOW:
      return 'info'
    case TaskPriority.MEDIUM:
      return 'primary'
    case TaskPriority.HIGH:
      return 'warning'
    case TaskPriority.URGENT:
      return 'danger'
    default:
      return 'info'
  }
}

/**
 * 获取任务优先级标签文字
 * @param priority 优先级值
 * @returns 优先级名称
 */
const getPriorityLabel = (priority: number) => {
  switch (priority) {
    case TaskPriority.LOW:
      return t('report.task.priorityLow')
    case TaskPriority.MEDIUM:
      return t('report.task.priorityMedium')
    case TaskPriority.HIGH:
      return t('report.task.priorityHigh')
    case TaskPriority.URGENT:
      return t('report.task.priorityUrgent')
    default:
      return '-'
  }
}

/**
 * 获取任务状态标签样式
 * @param status 状态值
 * @returns 标签样式
 */
const getStatusTagType = (status: number) => {
  switch (status) {
    case TaskStatus.PENDING:
      return 'info'
    case TaskStatus.IN_PROGRESS:
      return 'primary'
    case TaskStatus.COMPLETED:
      return 'success'
    case TaskStatus.CANCELLED:
      return 'warning'
    default:
      return 'info'
  }
}

/**
 * 获取任务状态标签文字
 * @param status 状态值
 * @returns 状态名称
 */
const getStatusLabel = (status: number) => {
  switch (status) {
    case TaskStatus.PENDING:
      return t('report.task.statusPending')
    case TaskStatus.IN_PROGRESS:
      return t('report.task.statusInProgress')
    case TaskStatus.COMPLETED:
      return t('report.task.statusCompleted')
    case TaskStatus.CANCELLED:
      return t('report.task.statusCancelled')
    default:
      return '-'
  }
}

/**
 * 查询列表数据
 */
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

/**
 * 搜索按钮操作
 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/**
 * 重置按钮操作
 */
const resetQuery = () => {
  queryFormRef.value?.resetFields()
  handleQuery()
}

/**
 * 打开表单弹窗
 * @param type 操作类型（create/update）
 * @param id 任务ID（更新时传入）
 */
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

/**
 * 删除操作
 * @param id 任务ID
 */
const handleDelete = async (id: number) => {
  try {
    await message.delConfirm()
    await TaskApi.deleteTask(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}

/**
 * 分配任务
 * @param id 任务ID
 */
const handleAssign = async (id: number) => {
  try {
    const { value: assigneeName } = await ElMessageBox.prompt(
      t('report.task.assignConfirm'),
      t('report.task.assign'),
      {
        inputPlaceholder: t('report.task.assigneeNamePlaceholder'),
        confirmButtonText: t('common.confirm'),
        cancelButtonText: t('common.cancel')
      }
    )
    if (assigneeName) {
      await TaskApi.assignTask(id, 1, assigneeName)
      message.success(t('report.task.assignSuccess'))
      await getList()
    }
  } catch {}
}

/**
 * 开始任务
 * @param id 任务ID
 */
const handleStart = async (id: number) => {
  try {
    await message.confirm(t('report.task.startConfirm'))
    await TaskApi.startTask(id)
    message.success(t('report.task.startSuccess'))
    await getList()
  } catch {}
}

/**
 * 完成任务
 * @param id 任务ID
 */
const handleComplete = async (id: number) => {
  try {
    await message.confirm(t('report.task.completeConfirm'))
    await TaskApi.completeTask(id)
    message.success(t('report.task.completeSuccess'))
    await getList()
  } catch {}
}

/**
 * 取消任务
 * @param id 任务ID
 */
const handleCancel = async (id: number) => {
  try {
    await message.confirm(t('report.task.cancelConfirm'))
    await TaskApi.cancelTask(id)
    message.success(t('report.task.cancelSuccess'))
    await getList()
  } catch {}
}

/**
 * 批量删除操作
 */
const checkedIds = ref<number[]>([])
const handleRowCheckboxChange = (rows: any[]) => {
  checkedIds.value = rows.map((row) => row.id)
}

/**
 * 组件挂载时初始化列表
 */
onMounted(() => {
  getList()
})
</script>
