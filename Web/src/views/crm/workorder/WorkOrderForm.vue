// 2023级软4蔡磊202305566515,2026年7月14日
<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="auto"
    >
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('workorder.title')" prop="title">
            <el-input v-model="formData.title" :placeholder="t('workorder.titlePlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('workorder.type')" prop="type">
            <el-input v-model="formData.type" :placeholder="t('workorder.typePlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('workorder.priority')" prop="priority">
            <el-select v-model="formData.priority" :placeholder="t('workorder.priorityPlaceholder')" class="w-1/1">
              <el-option value="LOW" :label="t('workorder.priorityLow')" />
              <el-option value="NORMAL" :label="t('workorder.priorityNormal')" />
              <el-option value="HIGH" :label="t('workorder.priorityHigh')" />
              <el-option value="URGENT" :label="t('workorder.priorityUrgent')" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('workorder.status')" prop="status">
            <el-select v-model="formData.status" :placeholder="t('workorder.statusPlaceholder')" class="w-1/1">
              <el-option value="待处理" :label="t('workorder.statusPending')" />
              <el-option value="处理中" :label="t('workorder.statusProcessing')" />
              <el-option value="已完结" :label="t('workorder.statusResolved')" />
              <el-option value="已退回" :label="t('workorder.statusReturned')" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item :label="t('workorder.content')" prop="content">
            <el-input type="textarea" v-model="formData.content" :placeholder="t('workorder.contentPlaceholder')" :rows="5" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{ t('dialog.confirm') }}</el-button>
      <el-button @click="dialogVisible = false">{{ t('dialog.cancel') }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import * as WorkOrderApi from '@/api/crm/workorder'

const { t } = useI18n('crm')
const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const formData = ref({
  id: undefined,
  title: undefined,
  type: undefined,
  priority: 'NORMAL',
  status: '待处理',
  content: undefined
})
const formRules = reactive({
  title: [{ required: true, message: t('workorder.titleRequired'), trigger: 'blur' }],
  priority: [{ required: true, message: t('workorder.priorityRequired'), trigger: 'change' }]
})
const formRef = ref()

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type, { scope: 'common' })
  formType.value = type
  resetForm()
  if (id) {
    formLoading.value = true
    try {
      formData.value = await WorkOrderApi.getWorkOrder(id)
    } finally {
      formLoading.value = false
    }
  }
}
defineExpose({ open })

/** 提交表单 */
const emit = defineEmits(['success'])
const submitForm = async () => {
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return
  formLoading.value = true
  try {
    const data = formData.value as unknown as WorkOrderApi.WorkOrderVO
    if (formType.value === 'create') {
      await WorkOrderApi.createWorkOrder(data)
      message.success(t('common.createSuccess'))
    } else {
      await WorkOrderApi.updateWorkOrder(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    emit('success')
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    title: undefined,
    type: undefined,
    priority: 'NORMAL',
    status: '待处理',
    content: undefined
  }
  formRef.value?.resetFields()
}
</script>
