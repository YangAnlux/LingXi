<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form ref="formRef" v-loading="formLoading" :model="formData" :rules="formRules" label-width="auto">
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('workReport.name')" prop="title">
            <el-input v-model="formData.title" :placeholder="t('workReport.namePlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('workReport.reportType')" prop="reportType">
            <el-select v-model="formData.reportType" :placeholder="t('common.all')" class="w-1/1">
              <el-option :value="1" :label="t('workReport.reportTypeDaily')" />
              <el-option :value="2" :label="t('workReport.reportTypeWeekly')" />
              <el-option :value="3" :label="t('workReport.reportTypeMonthly')" />
              <el-option :value="4" :label="t('workReport.reportTypeOther')" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('workReport.reportDate')" prop="reportDate">
            <el-date-picker
              v-model="formData.reportDate"
              type="date"
              :placeholder="t('workReport.reportDatePlaceholder')"
              class="w-1/1"
              value-format="YYYY-MM-DD"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('workReport.auditUserId')" prop="auditUserId">
            <el-select v-model="formData.auditUserId" class="w-1/1" clearable filterable>
              <el-option v-for="item in userOptions" :key="item.id" :label="item.nickname" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item :label="t('workReport.content')" prop="content">
            <el-input v-model="formData.content" :placeholder="t('workReport.contentPlaceholder')" type="textarea" :rows="5" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item :label="t('workReport.remark')" prop="remark">
            <el-input v-model="formData.remark" :placeholder="t('workReport.remarkPlaceholder')" type="textarea" :rows="3" />
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
import * as WorkReportApi from '@/api/crm/workreport'
import { getSimpleUserList } from '@/api/system/user'

defineOptions({ name: 'WorkReportForm' })

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
  title: '',
  content: '',
  reportDate: '',
  reportType: 1,
  auditUserId: null,
  relatedCampaignId: null,
  relatedCustomerId: null,
  remark: ''
})

const formRules = {
  title: [{ required: true, message: t('workReport.nameRequired'), trigger: 'blur' }]
}

const dialogTitle = computed(() => {
  return formType.value === 'create' ? t('common.add') + t('workReport.title') : t('common.edit') + t('workReport.title')
})

const open = async (type: string, id?: number) => {
  formType.value = type
  formLoading.value = true
  try {
    await loadUserOptions()
    resetForm()
    if (type === 'update' && id) {
      const data = await WorkReportApi.getWorkReport(id)
      Object.assign(formData, data)
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
  } catch {
    userOptions.value = []
  }
}

const resetForm = () => {
  formData.id = null
  formData.title = ''
  formData.content = ''
  formData.reportDate = ''
  formData.reportType = 1
  formData.auditUserId = null
  formData.relatedCampaignId = null
  formData.relatedCustomerId = null
  formData.remark = ''
}

const submitForm = async () => {
  try {
    await formRef.value.validate()
    formLoading.value = true
    const data = { ...formData }
    if (formType.value === 'create') {
      await WorkReportApi.createWorkReport(data)
      message.success(t('common.addSuccess'))
    } else {
      await WorkReportApi.updateWorkReport(data)
      message.success(t('common.editSuccess'))
    }
    dialogVisible.value = false
    emit('success')
  } finally {
    formLoading.value = false
  }
}

defineExpose({ open })
</script>
