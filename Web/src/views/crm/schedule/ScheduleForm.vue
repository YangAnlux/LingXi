<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form ref="formRef" v-loading="formLoading" :model="formData" :rules="formRules" label-width="auto">
      <el-row>
        <el-col :span="24">
          <el-form-item :label="t('schedule.name')" prop="title">
            <el-input v-model="formData.title" :placeholder="t('schedule.namePlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('schedule.startTime')" prop="startTime">
            <el-date-picker v-model="formData.startTime" type="datetime" :placeholder="t('schedule.startTimePlaceholder')" class="w-1/1" value-format="YYYY-MM-DD HH:mm:ss" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('schedule.endTime')" prop="endTime">
            <el-date-picker v-model="formData.endTime" type="datetime" :placeholder="t('schedule.endTimePlaceholder')" class="w-1/1" value-format="YYYY-MM-DD HH:mm:ss" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item :label="t('schedule.allDay')" prop="allDay">
            <el-switch v-model="formData.allDay" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('schedule.scheduleType')" prop="scheduleType">
            <el-select v-model="formData.scheduleType" class="w-1/1">
              <el-option :value="1" :label="t('schedule.scheduleTypePersonal')" />
              <el-option :value="2" :label="t('schedule.scheduleTypeTask')" />
              <el-option :value="3" :label="t('schedule.scheduleTypeMeeting')" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('schedule.color')" prop="color">
            <el-color-picker v-model="formData.color" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item :label="t('schedule.description')" prop="description">
            <el-input v-model="formData.description" :placeholder="t('schedule.descriptionPlaceholder')" type="textarea" :rows="3" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item :label="t('schedule.remark')" prop="remark">
            <el-input v-model="formData.remark" :placeholder="t('schedule.remarkPlaceholder')" type="textarea" :rows="2" />
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
import * as ScheduleApi from '@/api/crm/schedule'
import dayjs from 'dayjs'

defineOptions({ name: 'ScheduleForm' })

const emit = defineEmits(['success'])
const { t } = useI18n('crm')
const message = useMessage()

const dialogVisible = ref(false)
const formType = ref('create')
const formLoading = ref(false)
const formRef = ref()

const formData = reactive({
  id: null, title: '', description: '',
  startTime: '', endTime: '', allDay: false, color: '#409eff',
  scheduleType: 1, relatedTaskId: null, remark: ''
})

const formRules = {
  title: [{ required: true, message: t('schedule.nameRequired'), trigger: 'blur' }],
  startTime: [{ required: true, message: t('schedule.startTimeRequired'), trigger: 'blur' }],
  endTime: [{ required: true, message: t('schedule.endTimeRequired'), trigger: 'blur' }]
}

const dialogTitle = computed(() => {
  return formType.value === 'create' ? t('common.add') + t('schedule.title') : t('common.edit') + t('schedule.title')
})

const open = (type: string, id?: number, preset?: any) => {
  formType.value = type
  formLoading.value = true
  resetForm()
  // 应用预设值（从日历点击空白区域传入）
  if (preset) {
    if (preset.startTime) formData.startTime = preset.startTime
    if (preset.endTime) formData.endTime = preset.endTime
    if (preset.allDay !== undefined) formData.allDay = preset.allDay
  }
  if (type === 'update' && id) {
    ScheduleApi.getSchedule(id).then(data => {
      Object.assign(formData, data)
      if (formData.startTime && formData.startTime !== 0) {
        formData.startTime = dayjs(formData.startTime).format('YYYY-MM-DD HH:mm:ss')
      } else { formData.startTime = '' }
      if (formData.endTime && formData.endTime !== 0) {
        formData.endTime = dayjs(formData.endTime).format('YYYY-MM-DD HH:mm:ss')
      } else { formData.endTime = '' }
    })
  }
  dialogVisible.value = true
  formLoading.value = false
}

const resetForm = () => {
  formData.id = null; formData.title = ''; formData.description = ''
  formData.startTime = ''; formData.endTime = ''; formData.allDay = false
  formData.color = '#409eff'; formData.scheduleType = 1
  formData.relatedTaskId = null; formData.remark = ''
}

const submitForm = async () => {
  try {
    await formRef.value.validate()
    formLoading.value = true
    const data: any = { ...formData }
    if (data.startTime && typeof data.startTime === 'string') data.startTime = dayjs(data.startTime).valueOf()
    else if (data.startTime === '' || data.startTime == null) data.startTime = null
    if (data.endTime && typeof data.endTime === 'string') data.endTime = dayjs(data.endTime).valueOf()
    else if (data.endTime === '' || data.endTime == null) data.endTime = null
    if (formType.value === 'create') {
      await ScheduleApi.createSchedule(data)
      message.success(t('common.addSuccess'))
    } else {
      await ScheduleApi.updateSchedule(data)
      message.success(t('common.editSuccess'))
    }
    dialogVisible.value = false
    emit('success')
  } finally { formLoading.value = false }
}

defineExpose({ open })
</script>
