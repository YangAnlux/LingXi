<!-- 23软2张奎良-202305566305 -->
<template>
  <ContentWrap>
    <div class="flex items-center justify-between mb-20px">
      <h2 class="text-18px font-600">{{ t('report.task.taskBoard') }}</h2>
      <div class="flex items-center gap-20px">
        <div class="flex items-center gap-8px">
          <el-tag type="warning" size="small">{{ t('report.task.upcomingExpired') }}: {{ boardData.upcomingExpiredCount }}</el-tag>
        </div>
        <div class="flex items-center gap-8px">
          <el-tag type="danger" size="small">{{ t('report.task.expired') }}: {{ boardData.expiredCount }}</el-tag>
        </div>
      </div>
    </div>

    <div class="flex gap-20px">
      <div class="flex-1">
        <div class="bg-gray-50 rounded-lg p-15px">
          <div class="flex items-center justify-between mb-15px">
            <h3 class="text-14px font-600 flex items-center gap-8px">
              <el-tag type="info" size="small">{{ t('report.task.statusTodo') }}</el-tag>
              <span class="text-gray-500">({{ boardData.todoTasks.length }})</span>
            </h3>
            <el-button
              type="primary"
              size="small"
              plain
              @click="openForm('create')"
              v-hasPermi="['report:task:create']"
            >
              <Icon icon="ep:plus" /> {{ t('action.add') }}
            </el-button>
          </div>
          <div class="space-y-12px">
            <el-card
              v-for="task in boardData.todoTasks"
              :key="task.id"
              :shadow="'hover'"
              class="cursor-pointer"
              @click="handleCardClick(task)"
            >
              <div class="flex items-start justify-between">
                <div class="flex-1">
                  <div class="flex items-center gap-8px mb-8px">
                    <span class="font-500">{{ task.title }}</span>
                    <el-tag :type="getPriorityTagType(task.priority)" size="small">{{ getPriorityLabel(task.priority) }}</el-tag>
                  </div>
                  <div class="text-gray-500 text-sm mb-8px">{{ task.description }}</div>
                  <div class="flex items-center gap-15px text-xs text-gray-400">
                    <span class="flex items-center gap-4px">
                      <Icon icon="ep:user" /> {{ task.assigneeName || t('report.task.unassigned') }}
                    </span>
                    <span class="flex items-center gap-4px" :class="{ 'text-danger': isExpired(task) }">
                      <Icon icon="ep:clock" /> {{ formatDate(task.endDate) }}
                    </span>
                  </div>
                </div>
                <div class="flex flex-col gap-8px ml-15px">
                  <el-button
                    type="success"
                    size="small"
                    link
                    @click.stop="handleStart(task.id)"
                    v-hasPermi="['report:task:assign']"
                  >
                    {{ t('report.task.start') }}
                  </el-button>
                  <el-button
                    type="primary"
                    size="small"
                    link
                    @click.stop="openForm('update', task.id)"
                    v-hasPermi="['report:task:update']"
                  >
                    {{ t('action.edit') }}
                  </el-button>
                </div>
              </div>
            </el-card>
            <div v-if="boardData.todoTasks.length === 0" class="text-center py-30px text-gray-400">
              {{ t('report.task.noTodoTasks') }}
            </div>
          </div>
        </div>
      </div>

      <div class="flex-1">
        <div class="bg-blue-50 rounded-lg p-15px">
          <div class="flex items-center justify-between mb-15px">
            <h3 class="text-14px font-600 flex items-center gap-8px">
              <el-tag type="primary" size="small">{{ t('report.task.statusInProgress') }}</el-tag>
              <span class="text-gray-500">({{ boardData.inProgressTasks.length }})</span>
            </h3>
          </div>
          <div class="space-y-12px">
            <el-card
              v-for="task in boardData.inProgressTasks"
              :key="task.id"
              :shadow="'hover'"
              class="cursor-pointer"
              @click="handleCardClick(task)"
            >
              <div class="flex items-start justify-between">
                <div class="flex-1">
                  <div class="flex items-center gap-8px mb-8px">
                    <span class="font-500">{{ task.title }}</span>
                    <el-tag :type="getPriorityTagType(task.priority)" size="small">{{ getPriorityLabel(task.priority) }}</el-tag>
                  </div>
                  <div class="text-gray-500 text-sm mb-8px">{{ task.description }}</div>
                  <div class="mb-8px">
                    <el-progress :percentage="task.progress || 0" :stroke-width="6" :show-text="false" />
                    <span class="text-xs text-gray-400 ml-8px">{{ task.progress || 0 }}%</span>
                  </div>
                  <div class="flex items-center gap-15px text-xs text-gray-400">
                    <span class="flex items-center gap-4px">
                      <Icon icon="ep:user" /> {{ task.assigneeName }}
                    </span>
                    <span class="flex items-center gap-4px" :class="{ 'text-danger': isExpired(task) }">
                      <Icon icon="ep:clock" /> {{ formatDate(task.endDate) }}
                    </span>
                    <span v-if="isUpcomingExpired(task)" class="text-warning">
                      {{ t('report.task.upcomingExpired') }}
                    </span>
                  </div>
                </div>
                <div class="flex flex-col gap-8px ml-15px">
                  <el-button
                    type="success"
                    size="small"
                    link
                    @click.stop="handleComplete(task.id)"
                    v-hasPermi="['report:task:complete']"
                  >
                    {{ t('action.complete') }}
                  </el-button>
                  <el-button
                    type="danger"
                    size="small"
                    link
                    @click.stop="handleCancel(task.id)"
                    v-hasPermi="['report:task:cancel']"
                  >
                    {{ t('action.cancel') }}
                  </el-button>
                </div>
              </div>
            </el-card>
            <div v-if="boardData.inProgressTasks.length === 0" class="text-center py-30px text-gray-400">
              {{ t('report.task.noInProgressTasks') }}
            </div>
          </div>
        </div>
      </div>

      <div class="flex-1">
        <div class="bg-green-50 rounded-lg p-15px">
          <div class="flex items-center justify-between mb-15px">
            <h3 class="text-14px font-600 flex items-center gap-8px">
              <el-tag type="success" size="small">{{ t('report.task.statusCompleted') }}</el-tag>
              <span class="text-gray-500">({{ boardData.completedTasks.length }})</span>
            </h3>
          </div>
          <div class="space-y-12px">
            <el-card
              v-for="task in boardData.completedTasks"
              :key="task.id"
              :shadow="'hover'"
              class="cursor-pointer opacity-75"
              @click="handleCardClick(task)"
            >
              <div class="flex items-start justify-between">
                <div class="flex-1">
                  <div class="flex items-center gap-8px mb-8px">
                    <span class="font-500 line-through">{{ task.title }}</span>
                    <el-tag type="success" size="small">{{ t('action.complete') }}</el-tag>
                  </div>
                  <div class="text-gray-500 text-sm mb-8px">{{ task.description }}</div>
                  <div class="flex items-center gap-15px text-xs text-gray-400">
                    <span class="flex items-center gap-4px">
                      <Icon icon="ep:user" /> {{ task.assigneeName }}
                    </span>
                    <span class="flex items-center gap-4px">
                      <Icon icon="ep:check-circle" /> {{ formatDate(task.completeDate) }}
                    </span>
                  </div>
                </div>
                <div class="flex flex-col gap-8px ml-15px">
                  <el-button
                    type="danger"
                    size="small"
                    link
                    @click.stop="handleDelete(task.id)"
                    v-hasPermi="['report:task:delete']"
                  >
                    {{ t('action.del') }}
                  </el-button>
                </div>
              </div>
            </el-card>
            <div v-if="boardData.completedTasks.length === 0" class="text-center py-30px text-gray-400">
              {{ t('report.task.noCompletedTasks') }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </ContentWrap>

  <TaskForm ref="formRef" @success="handleSuccess" />
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { ElMessage } from 'element-plus'
import { Icon } from '@/components/Icon'
import { ContentWrap } from '@/components/ContentWrap'
import TaskForm from './TaskForm.vue'
import { getTaskBoard, startTask, completeTask, cancelTask, deleteTask } from '@/api/report/task'
import type { TaskRespVO } from '@/api/report/task'

const { t } = useI18n()

const loading = ref(false)
const formRef = ref<InstanceType<typeof TaskForm>>()

const boardData = reactive({
  todoTasks: [] as TaskRespVO[],
  inProgressTasks: [] as TaskRespVO[],
  completedTasks: [] as TaskRespVO[],
  upcomingExpiredCount: 0,
  expiredCount: 0
})

const typeOptions = [
  { value: 1, label: t('report.task.typeDaily') },
  { value: 2, label: t('report.task.typeUrgent') },
  { value: 3, label: t('report.task.typeProject') },
  { value: 4, label: t('report.task.typeTemporary') }
]

const priorityOptions = [
  { value: 1, label: t('report.task.priorityLow') },
  { value: 2, label: t('report.task.priorityMedium') },
  { value: 3, label: t('report.task.priorityHigh') },
  { value: 4, label: t('report.task.priorityUrgent') }
]

const statusOptions = [
  { value: 0, label: t('report.task.statusTodo') },
  { value: 1, label: t('report.task.statusInProgress') },
  { value: 2, label: t('report.task.statusCompleted') },
  { value: 3, label: t('report.task.statusCancelled') }
]

function formatDate(date: string | number[]): string {
  if (!date) return ''
  if (Array.isArray(date)) {
    const [year, month, day] = date
    return `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`
  }
  return date
}

function getPriorityLabel(priority: number): string {
  const option = priorityOptions.find(o => o.value === priority)
  return option ? option.label : ''
}

function getPriorityTagType(priority: number): string {
  switch (priority) {
    case 4: return 'danger'
    case 3: return 'warning'
    case 2: return 'primary'
    default: return 'info'
  }
}

function isExpired(task: TaskRespVO): boolean {
  if (!task.endDate) return false
  const endDateStr = formatDate(task.endDate)
  return endDateStr && new Date(endDateStr) < new Date(new Date().toDateString())
}

function isUpcomingExpired(task: TaskRespVO): boolean {
  if (!task.endDate) return false
  const endDateStr = formatDate(task.endDate)
  if (!endDateStr) return false
  const endDate = new Date(endDateStr)
  const today = new Date(new Date().toDateString())
  const threeDaysLater = new Date(today)
  threeDaysLater.setDate(threeDaysLater.getDate() + 3)
  return endDate >= today && endDate < threeDaysLater
}

async function getBoardData() {
  loading.value = true
  try {
    const data = await getTaskBoard()
    Object.assign(boardData, data)
  } catch (error) {
    console.error('获取看板数据失败:', error)
  } finally {
    loading.value = false
  }
}

function handleCardClick(task: TaskRespVO) {
  openForm('update', task.id)
}

function openForm(type: 'create' | 'update', id?: number) {
  formRef.value?.open(type, id)
}

async function handleStart(id: number) {
  try {
    await startTask(id)
    ElMessage.success(t('report.task.startSuccess'))
    getBoardData()
  } catch (error) {
    console.error('开始任务失败:', error)
  }
}

async function handleComplete(id: number) {
  try {
    await completeTask(id)
    ElMessage.success(t('report.task.completeSuccess'))
    getBoardData()
  } catch (error) {
    console.error('完成任务失败:', error)
  }
}

async function handleCancel(id: number) {
  try {
    await cancelTask(id)
    ElMessage.success(t('report.task.cancelSuccess'))
    getBoardData()
  } catch (error) {
    console.error('取消任务失败:', error)
  }
}

async function handleDelete(id: number) {
  try {
    await deleteTask(id)
    ElMessage.success(t('action.delSuccess'))
    getBoardData()
  } catch (error) {
    console.error('删除任务失败:', error)
  }
}

function handleSuccess() {
  getBoardData()
}

onMounted(() => {
  getBoardData()
})
</script>