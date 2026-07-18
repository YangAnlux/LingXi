<template>
  <ContentWrap>
    <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 16px;">
      <div>
        <el-radio-group v-model="filterType" @change="loadData" size="small">
          <el-radio-button value="all">{{ t('common.all') }}</el-radio-button>
          <el-radio-button value="my">{{ t('task.ownerUserId') }}</el-radio-button>
        </el-radio-group>
      </div>
      <el-button type="primary" @click="openForm('create')" v-hasPermi="['crm:task:create']">
        <Icon icon="ep:plus" class="mr-5px" /> {{ t('action.add') }}
      </el-button>
    </div>
  </ContentWrap>

  <div class="kanban-board">
    <div class="kanban-column" v-for="col in columns" :key="col.status">
      <div class="kanban-column-header" :style="{ borderTopColor: col.color }">
        <span>{{ col.label }}</span>
        <el-tag :type="col.tagType" size="small">{{ col.tasks.length }}</el-tag>
      </div>
      <div class="kanban-column-body" @dragover.prevent @drop="handleDrop($event, col.status)">
        <div
          v-for="task in col.tasks"
          :key="task.id"
          class="kanban-card"
          :class="{ 'is-overdue': isOverdue(task) }"
          draggable="true"
          @dragstart="handleDragStart($event, task)"
          @click="openForm('update', task.id)"
        >
          <div class="kanban-card-header">
            <el-tag :type="getPriorityTagType(task.priority)" size="small" :effect="task.priority >= 2 ? 'dark' : 'plain'">
              {{ getPriorityLabel(task.priority) }}
            </el-tag>
          </div>
          <div class="kanban-card-title">{{ task.name }}</div>
          <div class="kanban-card-meta">
            <span v-if="task.endTime" class="kanban-card-date" :class="{ 'text-red-500': isOverdue(task) }">
              <Icon icon="ep:clock" class="mr-4px" />
              {{ formatDate(task.endTime) }}
            </span>
            <span class="kanban-card-owner">
              <Icon icon="ep:user" class="mr-4px" />
              {{ task.ownerUserName || task.ownerUserId }}
            </span>
          </div>
        </div>
        <div v-if="col.tasks.length === 0" class="kanban-empty">{{ t('common.noData') }}</div>
      </div>
    </div>
  </div>

  <TaskForm ref="formRef" @success="loadData" />
</template>

<script setup lang="ts">
import * as TaskApi from '@/api/crm/task'
import TaskForm from './TaskForm.vue'
import { useUserStore } from '@/store/modules/user'
import dayjs from 'dayjs'

defineOptions({ name: 'CrmTaskKanban' })

const { t } = useI18n('crm')
const loading = ref(false)
const filterType = ref('all')
const currentUserId = computed(() => useUserStore().getUser?.id)

const columns = reactive([
  { status: 0, label: t('task.statusTodo'), color: '#909399', tagType: 'info', tasks: [] },
  { status: 1, label: t('task.statusInProgress'), color: '#409eff', tagType: '', tasks: [] },
  { status: 2, label: t('task.statusCompleted'), color: '#67c23a', tagType: 'success', tasks: [] }
])

const getPriorityLabel = (priority: number) => {
  const labels: Record<number, string> = {
    0: t('task.priorityLow'), 1: t('task.priorityMedium'), 2: t('task.priorityHigh'), 3: t('task.priorityUrgent')
  }
  return labels[priority] || ''
}

const getPriorityTagType = (priority: number) => {
  const types: Record<number, string> = { 0: 'info', 1: 'warning', 2: 'danger', 3: 'danger' }
  return types[priority] || 'info'
}

const formatDate = (value: any) => {
  if (!value) return ''
  const d = dayjs(value)
  return d.isValid() ? d.format('MM/DD HH:mm') : ''
}

const isOverdue = (task: any) => {
  if (!task.endTime || task.status === 2 || task.status === 3) return false
  return dayjs(task.endTime).isBefore(dayjs())
}

const loadData = async () => {
  loading.value = true
  try {
    for (const col of columns) {
      const params: any = { pageNo: 1, pageSize: 200, status: col.status }
      if (filterType.value === 'my' && currentUserId.value) {
        params.ownerUserId = currentUserId.value
      }
      const data = await TaskApi.getTaskPage(params)
      col.tasks = data.list || []
    }
  } finally {
    loading.value = false
  }
}

let draggedTask: any = null
const handleDragStart = (e: DragEvent, task: any) => {
  draggedTask = task
}

const handleDrop = async (e: DragEvent, targetStatus: number) => {
  if (!draggedTask || draggedTask.status === targetStatus) return
  try {
    await TaskApi.updateTaskStatus(draggedTask.id, targetStatus)
    await loadData()
  } catch {}
  draggedTask = null
}

const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

onMounted(() => loadData())
</script>

<style scoped>
.kanban-board {
  display: flex;
  gap: 16px;
  padding: 0 16px 16px;
  overflow-x: auto;
  min-height: calc(100vh - 240px);
}

.kanban-column {
  flex: 1;
  min-width: 300px;
  max-width: 420px;
  display: flex;
  flex-direction: column;
  background: #f5f7fa;
  border-radius: 8px;
}

.kanban-column-header {
  padding: 12px 16px;
  font-weight: 600;
  font-size: 14px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-top: 3px solid #909399;
}

.kanban-column-body {
  flex: 1;
  padding: 8px 12px;
  overflow-y: auto;
  max-height: calc(100vh - 320px);
}

.kanban-card {
  background: #fff;
  border-radius: 6px;
  padding: 12px;
  margin-bottom: 8px;
  cursor: pointer;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
  transition: box-shadow 0.2s;
}

.kanban-card:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.kanban-card.is-overdue {
  border-left: 3px solid #f56c6c;
}

.kanban-card-header {
  margin-bottom: 6px;
}

.kanban-card-title {
  font-size: 14px;
  color: #303133;
  margin-bottom: 8px;
  line-height: 1.4;
}

.kanban-card-meta {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #909399;
}

.kanban-card-date, .kanban-card-owner {
  display: flex;
  align-items: center;
}

.kanban-empty {
  text-align: center;
  color: #c0c4cc;
  padding: 40px 0;
  font-size: 13px;
}

.mr-4px {
  margin-right: 4px;
}
</style>
