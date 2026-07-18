<template>
  <ContentWrap>
    <div v-loading="loading">
      <!-- 操作按钮区 -->
      <div class="mb-15px">
        <el-button v-hasPermi="['crm:campaign:update']" type="primary" @click="openForm">
          {{ t('common.edit') }}
        </el-button>
        <el-button v-hasPermi="['crm:send-task:create']" type="success" @click="openSendDrawer">
          {{ t('campaign.send.sendToCampaign') }}
        </el-button>
        <el-button @click="close">{{ t('common.back') }}</el-button>
      </div>

      <!-- 基本信息 -->
      <el-descriptions :column="2" border>
        <el-descriptions-item :label="t('campaign.name')">
          {{ campaign.name || '-' }}
        </el-descriptions-item>
        <el-descriptions-item :label="t('campaign.status')">
          <el-tag :type="getStatusTagType(campaign.status)">
            {{ getStatusLabel(campaign.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item :label="t('campaign.startTime')">
          {{ formatDateTime(campaign.startTime) }}
        </el-descriptions-item>
        <el-descriptions-item :label="t('campaign.endTime')">
          {{ formatDateTime(campaign.endTime) }}
        </el-descriptions-item>
        <el-descriptions-item :label="t('campaign.budget')">
          {{ campaign.budget != null ? campaign.budget + ' 元' : '-' }}
        </el-descriptions-item>
        <el-descriptions-item :label="t('campaign.actualCost')">
          {{ campaign.actualCost != null ? campaign.actualCost + ' 元' : '-' }}
        </el-descriptions-item>
        <el-descriptions-item :label="t('campaign.ownerUserName')">
          {{ campaign.ownerUserName || '-' }}
        </el-descriptions-item>
        <el-descriptions-item :label="t('campaign.description')" :span="2">
          {{ campaign.description || '-' }}
        </el-descriptions-item>
        <el-descriptions-item :label="t('campaign.remark')" :span="2">
          {{ campaign.remark || '-' }}
        </el-descriptions-item>
        <el-descriptions-item :label="t('campaign.creatorName')">
          {{ campaign.creatorName || '-' }}
        </el-descriptions-item>
        <el-descriptions-item :label="t('campaign.createTime')">
          {{ formatDateTime(campaign.createTime) }}
        </el-descriptions-item>
        <el-descriptions-item :label="t('campaign.updateTime')">
          {{ formatDateTime(campaign.updateTime) }}
        </el-descriptions-item>
      </el-descriptions>
    </div>
  </ContentWrap>

  <!-- 表单弹窗 -->
  <CampaignForm ref="formRef" @success="getDetail" />

  <!-- 群发抽屉 -->
  <SendDrawer v-model="sendDrawerVisible" :campaign-id="campaignId" @success="getDetail" />
</template>

<script lang="ts" setup>
import * as CampaignApi from '@/api/crm/campaign'
import CampaignForm from '@/views/crm/campaign/CampaignForm.vue'
import SendDrawer from '@/views/crm/campaign/components/SendDrawer.vue'
import dayjs from 'dayjs'

defineOptions({ name: 'CrmCampaignDetail' })

const { t } = useI18n('crm')
const message = useMessage()
const { delView } = useTagsViewStore()
const { currentRoute } = useRouter()

const campaignId = ref(0)
const loading = ref(true)
const campaign = ref<CampaignApi.CampaignVO>({} as CampaignApi.CampaignVO)
const sendDrawerVisible = ref(false)

const openSendDrawer = () => {
  sendDrawerVisible.value = true
}

const getStatusLabel = (status: number) => {
  const labels: Record<number, string> = {
    1: t('campaign.statusDraft'),
    2: t('campaign.statusRunning'),
    3: t('campaign.statusEnded'),
    4: t('campaign.statusCancelled')
  }
  return labels[status] || ''
}

const getStatusTagType = (status: number) => {
  const types: Record<number, string> = { 1: 'info', 2: 'success', 3: 'warning', 4: 'danger' }
  return types[status] || 'info'
}

const formatDateTime = (value: any) => {
  if (!value) return '-'
  const d = dayjs(value)
  return d.isValid() ? d.format('YYYY-MM-DD HH:mm:ss') : '-'
}

/** 获取详情 */
const getDetail = async () => {
  loading.value = true
  try {
    campaign.value = await CampaignApi.getCampaign(campaignId.value)
  } finally {
    loading.value = false
  }
}

/** 编辑按钮 */
const formRef = ref()
const openForm = () => {
  formRef.value?.open('update', campaignId.value)
}

/** 关闭并返回列表 */
const close = () => {
  delView(unref(currentRoute))
}

/** 初始化 */
const { params } = useRoute()
onMounted(() => {
  if (!params.id) {
    message.warning(t('campaign.paramError'))
    close()
    return
  }
  campaignId.value = Number(params.id)
  getDetail()
})
</script>
