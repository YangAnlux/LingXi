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
        <el-col :span="24">
          <el-form-item :label="t('name')" prop="name">
            <el-input v-model="formData.name" :placeholder="t('namePlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('startTime')" prop="startTime">
            <el-date-picker
              v-model="formData.startTime"
              :placeholder="t('startTimePlaceholder')"
              class="w-100%"
              type="datetime"
              value-format="YYYY-MM-DD HH:mm:ss"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('endTime')" prop="endTime">
            <el-date-picker
              v-model="formData.endTime"
              :placeholder="t('endTimePlaceholder')"
              class="w-100%"
              type="datetime"
              value-format="YYYY-MM-DD HH:mm:ss"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item :label="t('sort')" prop="sort">
            <el-input-number v-model="formData.sort" :min="0" clearable controls-position="right" class="w-100%" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item :label="t('remark')" prop="remark">
            <el-input v-model="formData.remark" :placeholder="t('remarkPlaceholder')" type="textarea" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{ t('common.ok') }}</el-button>
      <el-button @click="dialogVisible = false">{{ t('common.cancel') }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import * as CampaignApi from '@/api/mall/promotion/campaign'

const { t } = useI18n('mall.promotion.campaign') // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  name: undefined,
  startTime: undefined,
  endTime: undefined,
  sort: 0,
  remark: undefined
})
const formRules = reactive({
  name: [{ required: true, message: t('nameRequired'), trigger: 'blur' }],
  startTime: [{ required: true, message: t('startTimeRequired'), trigger: 'change' }],
  endTime: [{ required: true, message: t('endTimeRequired'), trigger: 'change' }],
  sort: [{ required: true, message: t('common.required'), trigger: 'blur' }]
})
const formRef = ref() // 表单 Ref

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      const data = await CampaignApi.getCampaign(id)
      formData.value = {
        id: data.id,
        name: data.name,
        startTime: data.startTime,
        endTime: data.endTime,
        sort: data.sort ?? 0,
        remark: data.remark
      }
    } finally {
      formLoading.value = false
    }
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as CampaignApi.CampaignVO
    if (formType.value === 'create') {
      await CampaignApi.createCampaign(data)
      message.success(t('common.createSuccess'))
    } else {
      await CampaignApi.updateCampaign(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    name: undefined,
    startTime: undefined,
    endTime: undefined,
    sort: 0,
    remark: undefined
  }
  formRef.value?.resetFields()
}
</script>
