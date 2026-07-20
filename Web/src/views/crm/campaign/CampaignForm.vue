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
          <el-form-item :label="t('campaign.name')" prop="name">
            <el-input v-model="formData.name" :placeholder="t('campaign.namePlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('campaign.status')" prop="status">
            <el-select v-model="formData.status" :placeholder="t('common.all')" class="w-1/1">
              <el-option :value="1" :label="t('campaign.statusDraft')" />
              <el-option :value="2" :label="t('campaign.statusRunning')" />
              <el-option :value="3" :label="t('campaign.statusEnded')" />
              <el-option :value="4" :label="t('campaign.statusCancelled')" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('campaign.startTime')" prop="startTime">
            <el-date-picker
              v-model="formData.startTime"
              type="datetime"
              :placeholder="t('campaign.startTimePlaceholder')"
              class="w-1/1"
              value-format="YYYY-MM-DD HH:mm:ss"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('campaign.endTime')" prop="endTime">
            <el-date-picker
              v-model="formData.endTime"
              type="datetime"
              :placeholder="t('campaign.endTimePlaceholder')"
              class="w-1/1"
              value-format="YYYY-MM-DD HH:mm:ss"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('campaign.budget')" prop="budget">
            <el-input v-model="formData.budget" :placeholder="t('campaign.budgetPlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('campaign.actualCost')" prop="actualCost">
            <el-input v-model="formData.actualCost" :placeholder="t('campaign.actualCostPlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('campaign.ownerUserId')" prop="ownerUserId">
            <el-select
              v-model="formData.ownerUserId"
              :disabled="formType !== 'create'"
              class="w-1/1"
              clearable
              filterable
            >
              <el-option
                v-for="item in userOptions"
                :key="item.id"
                :label="item.nickname"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item :label="t('campaign.description')" prop="description">
            <el-input
              v-model="formData.description"
              :placeholder="t('campaign.descriptionPlaceholder')"
              type="textarea"
              :rows="3"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item :label="t('campaign.remark')" prop="remark">
            <el-input
              v-model="formData.remark"
              :placeholder="t('campaign.remarkPlaceholder')"
              type="textarea"
              :rows="3"
            />
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
import { ref, reactive, computed } from 'vue'
import * as CampaignApi from '@/api/crm/campaign'
import { getSimpleUserList } from '@/api/system/user'
import dayjs from 'dayjs'

defineOptions({ name: 'CampaignForm' })

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
  status: 1,
  startTime: '',
  endTime: '',
  budget: '',
  actualCost: '',
  description: '',
  ownerUserId: null,
  remark: ''
})

const formRules = {
  name: [{ required: true, message: t('campaign.nameRequired'), trigger: 'blur' }]
}

const dialogTitle = computed(() => {
  return formType.value === 'create' ? t('common.add') + t('campaign.title') : t('common.edit') + t('campaign.title')
})

const open = async (type: string, id?: number) => {
  formType.value = type
  formLoading.value = true
  try {
    await loadUserOptions()
    resetForm()
    if (type === 'update' && id) {
      const data = await CampaignApi.getCampaign(id)
      Object.assign(formData, data)
      // 后端返回 epoch 毫秒数：0 表示未设置时间，>0 为有效时间
      // 日期选择器 value-format="YYYY-MM-DD HH:mm:ss" 需要字符串格式
      if (formData.startTime && formData.startTime !== 0) {
        formData.startTime = dayjs(formData.startTime).format('YYYY-MM-DD HH:mm:ss')
      } else {
        formData.startTime = ''
      }
      if (formData.endTime && formData.endTime !== 0) {
        formData.endTime = dayjs(formData.endTime).format('YYYY-MM-DD HH:mm:ss')
      } else {
        formData.endTime = ''
      }
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
  formData.name = ''
  formData.status = 1
  formData.startTime = ''
  formData.endTime = ''
  formData.budget = ''
  formData.actualCost = ''
  formData.description = ''
  formData.ownerUserId = null
  formData.remark = ''
}

const submitForm = async () => {
  try {
    await formRef.value.validate()
    formLoading.value = true
    // 将格式化日期字符串转为 epoch 毫秒数，后端 Jackson 期望 Long 类型
    const data = { ...formData }
    if (data.startTime && typeof data.startTime === 'string') {
      data.startTime = dayjs(data.startTime).valueOf()
    } else if (data.startTime === '' || data.startTime == null) {
      data.startTime = null
    }
    if (data.endTime && typeof data.endTime === 'string') {
      data.endTime = dayjs(data.endTime).valueOf()
    } else if (data.endTime === '' || data.endTime == null) {
      data.endTime = null
    }
    if (formType.value === 'create') {
      await CampaignApi.createCampaign(data)
      message.success(t('common.createSuccess'))
    } else {
      await CampaignApi.updateCampaign(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    emit('success')
  } finally {
    formLoading.value = false
  }
}

defineExpose({ open })
</script>
