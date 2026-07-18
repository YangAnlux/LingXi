<template>
  <el-drawer
    :model-value="modelValue"
    @update:model-value="$emit('update:modelValue', $event)"
    :title="t('campaign.send.drawerTitle')"
    size="650px"
    direction="rtl"
    @close="handleClose"
  >
    <el-steps :active="currentStep" finish-status="success" align-center class="mb-20px">
      <el-step :title="t('campaign.send.step1Title')" />
      <el-step :title="t('campaign.send.step2Title')" />
      <el-step :title="t('campaign.send.step3Title')" />
      <el-step :title="t('campaign.send.step4Title')" />
    </el-steps>

    <div class="step-content" style="min-height: 350px;">
      <!-- Step 1: Channel -->
      <div v-show="currentStep === 0">
        <el-form label-width="100px">
          <el-form-item :label="t('campaign.send.channel')">
            <el-radio-group v-model="formData.channel" @change="onChannelChange">
              <el-radio-button :value="1">{{ t('campaign.send.sms') }}</el-radio-button>
              <el-radio-button :value="2">{{ t('campaign.send.mail') }}</el-radio-button>
            </el-radio-group>
          </el-form-item>
        </el-form>
      </div>

      <!-- Step 2: Template -->
      <div v-show="currentStep === 1">
        <el-form label-width="100px">
          <el-form-item :label="t('campaign.send.template')">
            <el-select
              v-model="formData.templateCode"
              :placeholder="t('campaign.send.templatePlaceholder')"
              class="w-1/1"
              filterable
              @change="onTemplateChange"
            >
              <el-option
                v-for="tmpl in templateList"
                :key="tmpl.code"
                :label="tmpl.name + ' (' + tmpl.code + ')'"
                :value="tmpl.code"
              >
                <div>
                  <div>{{ tmpl.name }}</div>
                  <div style="font-size: 12px; color: #909399;">{{ tmpl.content || tmpl.title }}</div>
                </div>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="t('campaign.send.templatePreview')" v-if="selectedTemplate">
            <el-card shadow="never" class="w-1/1">
              <div v-html="highlightedPreview" style="white-space: pre-wrap; color: #606266; line-height: 1.8;"></div>
            </el-card>
          </el-form-item>
        </el-form>
      </div>

      <!-- Step 3: Customer Selection -->
      <div v-show="currentStep === 2">
        <el-form label-width="100px">
          <el-form-item :label="t('campaign.send.customerMode')">
            <el-radio-group v-model="formData.customerSelectMode" @change="onCustomerModeChange">
              <el-radio :value="'ALL_ACTIVE'">{{ t('campaign.send.customerModeAll') }}</el-radio>
              <el-radio :value="'CSV'">{{ t('campaign.send.customerModeCsv') }}</el-radio>
              <el-radio :value="'BY_TAG'">{{ t('campaign.send.customerModeTag') }}</el-radio>
            </el-radio-group>
          </el-form-item>

          <!-- CSV Upload -->
          <el-form-item v-if="formData.customerSelectMode === 'CSV'" :label="t('campaign.send.csvUpload')">
            <el-upload
              ref="csvUploadRef"
              :auto-upload="false"
              :limit="1"
              accept=".csv"
              :on-change="handleCsvChange"
              :on-exceed="() => message.warning(t('campaign.send.csvLimitHint'))"
              drag
              action="#"
            >
              <Icon icon="ep:upload" class="text-3xl mb-8px" />
              <div class="el-upload__text">{{ t('campaign.send.csvUploadHint') }}</div>
            </el-upload>
            <div v-if="csvPreview.length > 0" style="margin-top: 8px; font-size: 13px; color: #67c23a;">
              {{ t('campaign.send.csvMatchedCount', { count: csvPreview.length }) }}
            </div>
          </el-form-item>

          <!-- Tag Selection -->
          <el-form-item v-if="formData.customerSelectMode === 'BY_TAG'" :label="t('campaign.send.byTag')">
            <el-input
              v-model="formData.customerTag"
              :placeholder="t('campaign.send.byTagPlaceholder')"
              class="w-1/1"
            />
          </el-form-item>

          <el-form-item>
            <div style="font-size: 13px; color: #909399;">
              {{ t('campaign.send.selectedCount') }}: <strong>{{ selectedCount }}</strong>
            </div>
          </el-form-item>
        </el-form>
      </div>

      <!-- Step 4: Send Mode -->
      <div v-show="currentStep === 3">
        <el-form label-width="100px">
          <el-form-item :label="t('campaign.send.sendMode')">
            <el-radio-group v-model="formData.sendMode">
              <el-radio :value="'IMMEDIATE'">{{ t('campaign.send.immediateSend') }}</el-radio>
              <el-radio :value="'SCHEDULED'">{{ t('campaign.send.scheduledSend') }}</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item v-if="formData.sendMode === 'SCHEDULED'" :label="t('campaign.send.scheduledTime')">
            <el-date-picker
              v-model="formData.scheduledTime"
              type="datetime"
              :placeholder="t('campaign.send.scheduledTimePlaceholder')"
              value-format="YYYY-MM-DD HH:mm:ss"
              :disabled-date="disabledDate"
              class="w-1/1"
            />
          </el-form-item>
          <el-form-item>
            <el-alert
              :title="t('campaign.send.sendSummary', { channel: formData.channel === 1 ? t('campaign.send.sms') : t('campaign.send.mail'), count: selectedCount })"
              type="info"
              :closable="false"
              show-icon
            />
          </el-form-item>
        </el-form>
      </div>
    </div>

    <template #footer>
      <div style="display: flex; justify-content: space-between;">
        <el-button @click="prevStep" :disabled="currentStep === 0">
          {{ t('common.previous') }}
        </el-button>
        <div>
          <el-button @click="handleClose">{{ t('common.cancel') }}</el-button>
          <el-button v-if="currentStep < 3" type="primary" @click="nextStep">
            {{ t('common.next') }}
          </el-button>
          <el-button v-if="currentStep === 3" type="primary" :loading="sending" @click="handleSend">
            {{ sending ? t('campaign.send.sending') : t('campaign.send.sendBtn') }}
          </el-button>
        </div>
      </div>
    </template>
  </el-drawer>
</template>

<script setup lang="ts">
import * as CampaignApi from '@/api/crm/campaign'
import * as SendApi from '@/api/crm/campaign/send'
import * as CustomerApi from '@/api/crm/customer'
import * as SendTaskApi from '@/api/crm/sendTask'

defineOptions({ name: 'CrmSendDrawer' })

const props = defineProps<{
  modelValue: boolean
  campaignId?: number
}>()

const emit = defineEmits<{
  (e: 'update:modelValue', v: boolean): void
  (e: 'success'): void
}>()

const { t } = useI18n('crm')
const message = useMessage()

const currentStep = ref(0)
const sending = ref(false)
const templateList = ref<any[]>([])
const selectedTemplate = ref<any>(null)
const csvUploadRef = ref()
const csvPreview = ref<string[]>([])

const formData = reactive({
  channel: 1,
  templateCode: '',
  templateId: null as number | null,
  customerSelectMode: 'ALL_ACTIVE',
  customerTag: '',
  sendMode: 'IMMEDIATE',
  scheduledTime: null as string | null
})

const selectedCount = computed(() => {
  if (formData.customerSelectMode === 'CSV') return csvPreview.value.length
  if (formData.customerSelectMode === 'ALL_ACTIVE') return '—'
  return '—'
})

const highlightedPreview = computed(() => {
  if (!selectedTemplate.value) return ''
  const content = selectedTemplate.value.content || selectedTemplate.value.title || ''
  return content.replace(
    /\$\{(\w+)\}|\{(\w+)\}/g,
    '<mark style="background:#fff3cd;padding:2px 4px;border-radius:3px;">$&</mark>'
  )
})

const disabledDate = (time: Date) => {
  return time.getTime() < Date.now() - 8.64e7 // 不能选过去的时间
}

const loadTemplates = async () => {
  try {
    if (formData.channel === 1) {
      templateList.value = await SendApi.getSmsTemplateList()
    } else {
      templateList.value = await SendApi.getMailTemplateList()
    }
  } catch { templateList.value = [] }
}

const onChannelChange = () => {
  formData.templateCode = ''
  selectedTemplate.value = null
  loadTemplates()
}

const onTemplateChange = (code: string) => {
  selectedTemplate.value = templateList.value.find(t => t.code === code) || null
  if (selectedTemplate.value) {
    formData.templateId = selectedTemplate.value.id
  }
}

const onCustomerModeChange = () => {
  if (formData.customerSelectMode !== 'CSV') {
    csvPreview.value = []
  }
}

const handleCsvChange = (file: any) => {
  const reader = new FileReader()
  reader.onload = (e) => {
    const text = e.target?.result as string
    const lines = text.split('\n').filter(line => line.trim())
    // 跳过表头，提取第一列（手机号/邮箱）
    csvPreview.value = lines.slice(1).map(line => line.split(',')[0]?.trim()).filter(Boolean)
  }
  if (file.raw) {
    reader.readAsText(file.raw)
  }
}

const nextStep = () => {
  if (currentStep.value === 1 && !formData.templateCode) {
    message.warning(t('campaign.send.selectTemplateHint'))
    return
  }
  if (currentStep.value < 3) {
    currentStep.value++
  }
}

const prevStep = () => {
  if (currentStep.value > 0) {
    currentStep.value--
  }
}

const handleClose = () => {
  emit('update:modelValue', false)
  currentStep.value = 0
  formData.templateCode = ''
  formData.templateId = null
  formData.customerSelectMode = 'ALL_ACTIVE'
  formData.customerTag = ''
  formData.sendMode = 'IMMEDIATE'
  formData.scheduledTime = null
  selectedTemplate.value = null
  csvPreview.value = []
}

const handleSend = async () => {
  sending.value = true
  try {
    const req: any = {
      activityId: props.campaignId || null,
      taskType: 'MARKETING',
      templateId: formData.templateId,
      templateCode: formData.templateCode,
      channel: formData.channel,
      customerSelectMode: formData.customerSelectMode,
      sendMode: formData.sendMode,
      scheduledTime: formData.scheduledTime || null
    }
    if (formData.customerSelectMode === 'BY_TAG') {
      req.customerTag = formData.customerTag
    }

    const res = await SendTaskApi.createSendTask(req)

    // 检查变量缺失警告
    if (res.hasWarning && res.missingVarCount > 0) {
      try {
        await message.confirm(
          t('campaign.send.variableMissingWarning', { count: res.missingVarCount })
        )
        // 用户确认后强制发送
        await SendTaskApi.createSendTask({ ...req, forceSend: true })
        message.success(t('campaign.send.sendSuccess'))
        emit('success')
        handleClose()
      } catch {
        // 用户取消
      }
    } else {
      message.success(t('campaign.send.sendSuccess'))
      emit('success')
      handleClose()
    }
  } catch (e: any) {
    if (e?.msg) message.error(e.msg)
  } finally {
    sending.value = false
  }
}

onMounted(() => {
  loadTemplates()
})
</script>

<style scoped>
.step-content {
  padding: 20px 0;
}
</style>
