<!-- 23软2张奎良-202305566305 -->
<template>
  <el-dialog
    :title="title"
    v-model="dialogVisible"
    width="600px"
    destroy-on-close
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="120px"
    >
      <el-form-item :label="t('report.task.title')" prop="title">
        <el-input v-model="form.title" :placeholder="t('report.task.titlePlaceholder')" />
      </el-form-item>
      <el-form-item :label="t('report.task.description')" prop="description">
        <el-input v-model="form.description" type="textarea" :rows="3" :placeholder="t('report.task.descriptionPlaceholder')" />
      </el-form-item>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="t('report.task.type')" prop="type">
            <el-select v-model="form.type" :placeholder="t('report.task.typePlaceholder')">
              <el-option
                v-for="item in typeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('report.task.priority')" prop="priority">
            <el-select v-model="form.priority" :placeholder="t('report.task.priorityPlaceholder')">
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
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="t('report.task.startDate')" prop="startDate">
            <el-date-picker
              v-model="form.startDate"
              type="date"
              value-format="YYYY-MM-DD"
              :placeholder="t('report.task.startDatePlaceholder')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('report.task.endDate')" prop="endDate">
            <el-date-picker
              v-model="form.endDate"
              type="date"
              value-format="YYYY-MM-DD"
              :placeholder="t('report.task.endDatePlaceholder')"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item :label="t('report.task.assigneeName')" prop="assigneeName">
        <el-input v-model="form.assigneeName" :placeholder="t('report.task.assigneeNamePlaceholder')" />
      </el-form-item>
      <el-form-item :label="t('report.task.deptName')" prop="deptName">
        <el-input v-model="form.deptName" :placeholder="t('report.task.deptNamePlaceholder')" />
      </el-form-item>
      <el-form-item :label="t('report.task.progress')" prop="progress">
        <el-slider v-model="form.progress" :min="0" :max="100" :show-input="true" />
      </el-form-item>
      <el-form-item :label="t('report.task.remark')" prop="remark">
        <el-input v-model="form.remark" type="textarea" :rows="2" :placeholder="t('report.task.remarkPlaceholder')" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">{{ t('common.cancel') }}</el-button>
      <el-button type="primary" @click="handleSubmit">{{ t('common.confirm') }}</el-button>
    </template>
  </el-dialog>
</template>
<script lang="ts" setup>
// 23软2张奎良-202305566305
import { ref, reactive, computed } from 'vue'
import { TaskType, TaskPriority, TaskStatus } from '@/api/report/task'
import type { TaskSaveReqVO } from '@/api/report/task'
import * as TaskApi from '@/api/report/task'

/** 消息弹窗 */
const message = useMessage()
/** 国际化 */
const { t } = useI18n()

/** 表单引用 */
const formRef = ref()

/** 弹窗可见状态 */
const dialogVisible = ref(false)

/** 操作类型 */
const operationType = ref('create')

/** 表单数据 */
const form = reactive<TaskSaveReqVO>({
  title: '',
  description: '',
  type: TaskType.DAILY,
  priority: TaskPriority.MEDIUM,
  status: TaskStatus.PENDING,
  startDate: '',
  endDate: '',
  assigneeName: '',
  deptName: '',
  progress: 0,
  remark: ''
})

/** 表单标题 */
const title = computed(() => {
  return operationType.value === 'create' ? t('action.add') : t('action.edit')
})

/** 表单验证规则 */
const rules = {
  title: [
    { required: true, message: t('report.task.titleRequired'), trigger: 'blur' }
  ],
  type: [
    { required: true, message: t('report.task.typeRequired'), trigger: 'change' }
  ],
  priority: [
    { required: true, message: t('report.task.priorityRequired'), trigger: 'change' }
  ],
  startDate: [
    { required: true, message: t('report.task.startDateRequired'), trigger: 'change' }
  ],
  endDate: [
    { required: true, message: t('report.task.endDateRequired'), trigger: 'change' }
  ]
}

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

/**
 * 打开弹窗
 * @param type 操作类型（create/update）
 * @param id 任务ID（更新时传入）
 */
const open = async (type: string, id?: number) => {
  operationType.value = type
  dialogVisible.value = true
  
  if (type === 'create') {
    Object.assign(form, {
      title: '',
      description: '',
      type: TaskType.DAILY,
      priority: TaskPriority.MEDIUM,
      status: TaskStatus.PENDING,
      startDate: '',
      endDate: '',
      assigneeName: '',
      deptName: '',
      progress: 0,
      remark: ''
    })
  } else if (type === 'update' && id) {
    const data = await TaskApi.getTask(id)
    Object.assign(form, data)
    // 处理日期字段：后端返回数组格式 [year, month, day]，转换为 YYYY-MM-DD 字符串
    if (Array.isArray(form.startDate)) {
      const [year, month, day] = form.startDate
      form.startDate = `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`
    }
    if (Array.isArray(form.endDate)) {
      const [year, month, day] = form.endDate
      form.endDate = `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`
    }
  }
  
  await nextTick()
  formRef.value?.clearValidate()
}

/**
 * 提交表单
 */
const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    
    if (operationType.value === 'create') {
      await TaskApi.createTask(form)
      message.success(t('common.addSuccess'))
    } else {
      await TaskApi.updateTask(form)
      message.success(t('common.updateSuccess'))
    }
    
    dialogVisible.value = false
    emit('success')
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

/**
 * 关闭弹窗
 */
const handleClose = () => {
  formRef.value?.clearValidate()
}

/** 成功事件 */
const emit = defineEmits(['success'])

/** 暴露方法给父组件 */
defineExpose({
  open
})
</script>
