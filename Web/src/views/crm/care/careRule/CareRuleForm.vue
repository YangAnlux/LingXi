<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form ref="formRef" v-loading="formLoading" :model="formData" :rules="formRules" label-width="120px">
      <el-row>
        <el-col :span="24">
          <el-form-item :label="t('careRule.name')" prop="name">
            <el-input v-model="formData.name" :placeholder="t('careRule.namePlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="t('careRule.triggerType')" prop="triggerType">
            <el-select v-model="formData.triggerType" :placeholder="t('careRule.triggerTypePlaceholder')" class="w-1/1">
              <el-option value="BIRTHDAY" :label="t('careRule.triggerBirthday')" />
              <el-option value="HOLIDAY" :label="t('careRule.triggerHoliday')" />
              <el-option value="MANUAL" :label="t('careRule.triggerManual')" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('careRule.triggerDaysBefore')" prop="triggerDaysBefore">
            <el-input-number v-model="formData.triggerDaysBefore" :min="0" :max="30" class="w-1/1" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="t('careRule.channel')" prop="channel">
            <el-select v-model="formData.channel" :placeholder="t('careRule.channelPlaceholder')" class="w-1/1">
              <el-option :value="1" :label="t('campaign.send.sms')" />
              <el-option :value="2" :label="t('campaign.send.mail')" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('careRule.templateCode')" prop="templateCode">
            <el-input v-model="formData.templateCode" :placeholder="t('careRule.templateCodePlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="t('careRule.sendWindowStart')" prop="sendWindowStart">
            <el-time-picker v-model="formData.sendWindowStart" format="HH:mm:ss" value-format="HH:mm:ss" :placeholder="t('careRule.sendWindowStartPlaceholder')" class="w-1/1" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('careRule.sendWindowEnd')" prop="sendWindowEnd">
            <el-time-picker v-model="formData.sendWindowEnd" format="HH:mm:ss" value-format="HH:mm:ss" :placeholder="t('careRule.sendWindowEndPlaceholder')" class="w-1/1" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item :label="t('careRule.remark')" prop="remark">
            <el-input v-model="formData.remark" type="textarea" :placeholder="t('careRule.remarkPlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">{{ t('common.cancel') }}</el-button>
      <el-button type="primary" @click="submitForm" :loading="formLoading">
        {{ t('common.confirm') }}
      </el-button>
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import * as CareRuleApi from '@/api/crm/care/rule'

defineOptions({ name: 'CrmCareRuleForm' })

const { t } = useI18n('crm')
const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formRef = ref()
const isUpdate = ref(false)
const updateId = ref<number>()

const formData = reactive<CareRuleApi.CareRuleVO>({
  name: '',
  triggerType: 'BIRTHDAY',
  triggerDaysBefore: 0,
  templateId: 1,
  templateCode: '',
  channel: 1,
  sendWindowStart: '08:00:00',
  sendWindowEnd: '20:00:00',
  isEnabled: 1,
  remark: ''
})

const formRules = {
  name: [{ required: true, message: '请输入规则名称', trigger: 'blur' }],
  triggerType: [{ required: true, message: '请选择触发类型', trigger: 'change' }],
  channel: [{ required: true, message: '请选择发送渠道', trigger: 'change' }],
  templateCode: [{ required: true, message: '请输入模板编码', trigger: 'blur' }]
}

const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  isUpdate.value = type === 'update'
  dialogTitle.value = isUpdate.value ? t('careRule.editTitle') : t('careRule.createTitle')
  if (id) {
    updateId.value = id
    formLoading.value = true
    try {
      const data = await CareRuleApi.getCareRule(id)
      Object.assign(formData, data)
    } finally { formLoading.value = false }
  } else {
    resetForm()
  }
}

const resetForm = () => {
  Object.assign(formData, {
    name: '', triggerType: 'BIRTHDAY', triggerDaysBefore: 0,
    templateId: 1, templateCode: '', channel: 1,
    sendWindowStart: '08:00:00', sendWindowEnd: '20:00:00',
    isEnabled: 1, remark: ''
  })
  formRef.value?.resetFields()
}

const submitForm = async () => {
  try {
    await formRef.value?.validate()
    formLoading.value = true
    if (isUpdate.value) {
      formData.id = updateId.value
      await CareRuleApi.updateCareRule(formData)
      message.success(t('common.updateSuccess'))
    } else {
      await CareRuleApi.createCareRule(formData)
      message.success(t('common.createSuccess'))
    }
    dialogVisible.value = false
    emit('success')
  } catch {} finally { formLoading.value = false }
}

const emit = defineEmits<{ (e: 'success'): void }>()

defineExpose({ open })
</script>
