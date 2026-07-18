<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form ref="formRef" v-loading="formLoading" :model="formData" :rules="formRules" label-width="auto">
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('task.name')" prop="name">
            <el-input v-model="formData.name" :placeholder="t('task.namePlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('task.priority')" prop="priority">
            <el-select v-model="formData.priority" class="w-1/1">
              <el-option :value="0" :label="t('task.priorityLow')" />
              <el-option :value="1" :label="t('task.priorityMedium')" />
              <el-option :value="2" :label="t('task.priorityHigh')" />
              <el-option :value="3" :label="t('task.priorityUrgent')" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('task.startTime')" prop="startTime">
            <el-date-picker
              v-model="formData.startTime" type="datetime"
              :placeholder="t('task.startTimePlaceholder')" class="w-1/1"
              value-format="YYYY-MM-DD HH:mm:ss"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('task.endTime')" prop="endTime">
            <el-date-picker
              v-model="formData.endTime" type="datetime"
              :placeholder="t('task.endTimePlaceholder')" class="w-1/1"
              value-format="YYYY-MM-DD HH:mm:ss"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('task.ownerUserId')" prop="ownerUserId">
            <el-select v-model="formData.ownerUserId" :disabled="formType !== 'create'" class="w-1/1" clearable filterable>
              <el-option v-for="item in userOptions" :key="item.id" :label="item.nickname" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('task.assignerUserId')" prop="assignerUserId">
            <el-select v-model="formData.assignerUserId" class="w-1/1" clearable filterable>
              <el-option v-for="item in userOptions" :key="item.id" :label="item.nickname" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item :label="t('task.description')" prop="description">
            <el-input v-model="formData.description" :placeholder="t('task.descriptionPlaceholder')" type="textarea" :rows="4" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item :label="t('task.remark')" prop="remark">
            <el-input v-model="formData.remark" :placeholder="t('task.remarkPlaceholder')" type="textarea" :rows="3" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">{{ t('common.cancel') }}</el-button>
      <el-button type="primary" @click="submitForm">{{ t('common.confirm') }}</el-button>
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import * as TaskApi from '@/api/crm/task'
import { getSimpleUserList } from '@/api/system/user'
import dayjs from 'dayjs'

defineOptions({ name: 'TaskForm' })

const emit = defineEmits(['success'])
const { t } = useI18n('crm')
const message = useMessage()

const dialogVisible = ref(false)
const formType = ref('create')
const formLoading = ref(false)
const formRef = ref()
const userOptions = ref<Array<{ id: number; nickname: string }>>([])

const formData = reactive({
  id: null,
  name: '',
  description: '',
  priority: 1,
  startTime: '',
  endTime: '',
  ownerUserId: null,
  assignerUserId: null,
  relatedCampaignId: null,
  relatedCustomerId: null,
  relatedBusinessId: null,
  remark: ''
})

const formRules = {
  name: [{ required: true, message: t('task.nameRequired'), trigger: 'blur' }]
}

const dialogTitle = computed(() => {
  return formType.value === 'create' ? t('common.add') + t('task.title') : t('common.edit') + t('task.title')
})

const open = async (type: string, id?: number) => {
  formType.value = type
  formLoading.value = true
  try {
    await loadUserOptions()
    resetForm()
    if (type === 'update' && id) {
      const data = await TaskApi.getTask(id)
      Object.assign(formData, data)
      if (formData.startTime && formData.startTime !== 0) {
        formData.startTime = dayjs(formData.startTime).format('YYYY-MM-DD HH:mm:ss')
      } else { formData.startTime = '' }
      if (formData.endTime && formData.endTime !== 0) {
        formData.endTime = dayjs(formData.endTime).format('YYYY-MM-DD HH:mm:ss')
      } else { formData.endTime = '' }
    }
    dialogVisible.value = true
  } finally {
    formLoading.value = false
  }
}

const loadUserOptions = async () => {
  try {
    const list = await getSimpleUserList()
    userOptions.value = (list || []).filter((u: any) => u.id && u.nickname)
  } catch { userOptions.value = [] }
}

const resetForm = () => {
  formData.id = null; formData.name = ''; formData.description = ''; formData.priority = 1
  formData.startTime = ''; formData.endTime = ''
  formData.ownerUserId = null; formData.assignerUserId = null
  formData.relatedCampaignId = null; formData.relatedCustomerId = null; formData.relatedBusinessId = null
  formData.remark = ''
}

const submitForm = async () => {
  try {
    await formRef.value.validate()
    formLoading.value = true
    const data = { ...formData }
    if (data.startTime && typeof data.startTime === 'string') {
      data.startTime = dayjs(data.startTime).valueOf()
    } else if (data.startTime === '' || data.startTime == null) { data.startTime = null }
    if (data.endTime && typeof data.endTime === 'string') {
      data.endTime = dayjs(data.endTime).valueOf()
    } else if (data.endTime === '' || data.endTime == null) { data.endTime = null }
    if (formType.value === 'create') {
      await TaskApi.createTask(data)
      message.success(t('common.addSuccess'))
    } else {
      await TaskApi.updateTask(data)
      message.success(t('common.editSuccess'))
    }
    dialogVisible.value = false
    emit('success')
  } finally { formLoading.value = false }
}

defineExpose({ open })
</script>
