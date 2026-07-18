<template>
  <ContentWrap :title="t('campaign.send.title')">
    <el-form ref="formRef" :model="formData" :rules="formRules" label-width="120px" style="max-width: 800px;">
      <el-form-item :label="t('campaign.send.campaign')" prop="campaignId">
        <el-select v-model="formData.campaignId" :placeholder="t('campaign.send.campaignPlaceholder')" class="w-1/1" filterable clearable>
          <el-option v-for="c in campaignList" :key="c.id" :label="c.name" :value="c.id" />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('campaign.send.channel')" prop="channel">
        <el-radio-group v-model="formData.channel" @change="onChannelChange">
          <el-radio-button :value="1">{{ t('campaign.send.sms') }}</el-radio-button>
          <el-radio-button :value="2">{{ t('campaign.send.mail') }}</el-radio-button>
        </el-radio-group>
      </el-form-item>
      <el-form-item :label="t('campaign.send.template')" prop="templateCode">
        <el-select v-model="formData.templateCode" :placeholder="t('campaign.send.templatePlaceholder')" class="w-1/1" clearable @change="onTemplateChange">
          <el-option v-for="tmpl in templateList" :key="tmpl.code" :label="tmpl.name + ' (' + tmpl.code + ')'" :value="tmpl.code">
            <div>
              <div>{{ tmpl.name }}</div>
              <div style="font-size: 12px; color: #909399;">{{ tmpl.content || tmpl.title }}</div>
            </div>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item :label="t('campaign.send.preview')" v-if="selectedTemplate">
        <el-card shadow="never" class="w-1/1">
          <div style="white-space: pre-wrap; color: #606266;">{{ selectedTemplate.content || selectedTemplate.title }}</div>
        </el-card>
      </el-form-item>
      <el-form-item :label="t('campaign.send.customers')" prop="customerIds">
        <div style="width: 100%;">
          <el-transfer
            v-model="formData.customerIds"
            :data="customerTransferData"
            :titles="[t('campaign.send.availableCustomers'), t('campaign.send.selectedCustomers')]"
            filterable
            :filter-method="filterCustomer"
            filter-placeholder="搜索客户"
            style="width: 100%;"
          />
          <div style="margin-top: 8px; font-size: 13px; color: #909399;">
            {{ t('campaign.send.selectedCount') }}: {{ formData.customerIds.length }}
          </div>
        </div>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="sending" @click="handleSend">
          {{ sending ? t('campaign.send.sending') : t('campaign.send.sendBtn') }}
        </el-button>
        <el-button @click="handleReset">{{ t('common.reset') }}</el-button>
      </el-form-item>
    </el-form>

    <el-divider />

    <div v-if="sendResult" style="margin-top: 16px;">
      <el-alert :title="t('campaign.send.result')" :type="sendResult.failCount > 0 ? 'warning' : 'success'" :closable="false" show-icon>
        <template #default>
          <div>{{ t('campaign.send.total') }}: {{ sendResult.totalCount }}</div>
          <div style="color: #67c23a;">{{ t('campaign.send.success') }}: {{ sendResult.successCount }}</div>
          <div v-if="sendResult.failCount > 0" style="color: #f56c6c;">{{ t('campaign.send.fail') }}: {{ sendResult.failCount }}</div>
        </template>
      </el-alert>
    </div>
  </ContentWrap>
</template>

<script setup lang="ts">
import * as CampaignApi from '@/api/crm/campaign'
import * as SendApi from '@/api/crm/campaign/send'
import * as CustomerApi from '@/api/crm/customer'

defineOptions({ name: 'CrmCampaignSendForm' })

const { t } = useI18n('crm')
const message = useMessage()
const route = useRoute()

const sending = ref(false)
const sendResult = ref<any>(null)
const campaignList = ref<any[]>([])
const templateList = ref<any[]>([])
const customerTransferData = ref<any[]>([])
const selectedTemplate = ref<any>(null)

const formData = reactive({
  campaignId: null as number | null,
  channel: 1,
  templateCode: '',
  customerIds: [] as number[]
})

const formRules = {
  campaignId: [{ required: true, message: '请选择营销活动', trigger: 'change' }],
  templateCode: [{ required: true, message: '请选择模板', trigger: 'change' }],
  customerIds: [{ type: 'array', required: true, message: '请选择客户', trigger: 'change', min: 1 }]
}

const loadCampaigns = async () => {
  try {
    const data = await CampaignApi.getCampaignPage({ pageNo: 1, pageSize: 200 })
    campaignList.value = data.list || []
  } catch { campaignList.value = [] }
}

const onChannelChange = async () => {
  formData.templateCode = ''
  selectedTemplate.value = null
  await loadTemplates()
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

const onTemplateChange = (code: string) => {
  selectedTemplate.value = templateList.value.find(tmpl => tmpl.code === code) || null
}

const loadCustomers = async () => {
  try {
    const data = await CustomerApi.getCustomerSimpleList()
    customerTransferData.value = (data || []).map((c: any) => ({
      key: c.id,
      label: c.name,
      disabled: false
    }))
  } catch { customerTransferData.value = [] }
}

const filterCustomer = (query: string, item: any) => {
  return item.label.toLowerCase().includes(query.toLowerCase())
}

const formRef = ref()

const handleSend = async () => {
  try {
    await formRef.value?.validate()

    await message.confirm(
      t('campaign.send.sendConfirm', { count: formData.customerIds.length })
    )

    sending.value = true
    const req = {
      campaignId: formData.campaignId!,
      templateCode: formData.templateCode,
      customerIds: formData.customerIds
    }
    if (formData.channel === 1) {
      sendResult.value = await SendApi.sendSms(req)
    } else {
      sendResult.value = await SendApi.sendMail(req)
    }
    message.success(t('campaign.send.sendSuccess'))
  } catch {
  } finally {
    sending.value = false
  }
}

const handleReset = () => {
  formData.campaignId = null
  formData.channel = 1
  formData.templateCode = ''
  formData.customerIds = []
  sendResult.value = null
  selectedTemplate.value = null
  onChannelChange()
}

onMounted(async () => {
  const campaignId = route.query.campaignId
  if (campaignId) {
    formData.campaignId = Number(campaignId)
  }
  await Promise.all([loadCampaigns(), loadTemplates(), loadCustomers()])
})
</script>
