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
          <el-form-item :label="t('workOrder.title')" prop="title">
            <el-input v-model="formData.title" :placeholder="t('workOrder.titlePlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('workOrder.type')" prop="type">
            <el-select v-model="formData.type" :placeholder="t('workOrder.typePlaceholder')" class="w-1/1">
              <el-option :value="'TECH_SUPPORT'" :label="t('workOrder.typeTechSupport')" />
              <el-option :value="'CONSULTATION'" :label="t('workOrder.typeConsultation')" />
              <el-option :value="'COMPLAINT'" :label="t('workOrder.typeComplaint')" />
              <el-option :value="'OTHER'" :label="t('workOrder.typeOther')" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('workOrder.priority')" prop="priority">
            <el-select v-model="formData.priority" :placeholder="t('workOrder.priorityPlaceholder')" class="w-1/1">
              <el-option :value="'LOW'" :label="t('workOrder.priorityLow')" />
              <el-option :value="'NORMAL'" :label="t('workOrder.priorityNormal')" />
              <el-option :value="'HIGH'" :label="t('workOrder.priorityHigh')" />
              <el-option :value="'URGENT'" :label="t('workOrder.priorityUrgent')" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('workOrder.customer')" prop="customerId">
            <el-select v-model="formData.customerId" :placeholder="t('workOrder.customerPlaceholder')" class="w-1/1">
              <el-option v-for="customer in customerOptions" :key="customer.id" :label="customer.name" :value="customer.id" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('workOrder.assignee')" prop="assigneeId">
            <el-select v-model="formData.assigneeId" :placeholder="t('workOrder.assigneePlaceholder')" class="w-1/1">
              <el-option v-for="user in userOptions" :key="user.id" :label="user.nickname" :value="user.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('workOrder.slaDeadline')" prop="slaDeadline">
            <el-date-picker
              v-model="formData.slaDeadline"
              type="datetime"
              value-format="x"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item :label="t('workOrder.content')" prop="content">
            <el-input type="textarea" v-model="formData.content" :placeholder="t('workOrder.contentPlaceholder')" :rows="4" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row v-if="formType === 'update'">
        <el-col :span="24">
          <el-form-item :label="t('workOrder.solution')" prop="solution">
            <el-input type="textarea" v-model="formData.solution" :placeholder="t('workOrder.solutionPlaceholder')" :rows="4" />
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
import * as WorkOrderApi from '@/api/crm/workOrder'
import * as UserApi from '@/api/system/user'
import * as CustomerApi from '@/api/crm/customer'

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
  customerId: undefined,
  assigneeId: undefined,
  slaDeadline: undefined,
  content: undefined,
  solution: undefined
})

const formRules = reactive({
  title: [{ required: true, message: t('workOrder.titleRequired'), trigger: 'blur' }],
  content: [{ required: true, message: t('workOrder.contentRequired'), trigger: 'blur' }]
})

const formRef = ref()
const userOptions = ref<UserApi.UserVO[]>([])
const customerOptions = ref<CustomerApi.CustomerVO[]>([])

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

  userOptions.value = await UserApi.getSimpleUserList()
  customerOptions.value = await CustomerApi.getCustomerSimpleList()
}
defineExpose({ open })

const emit = defineEmits(['success'])
const submitForm = async () => {
  if (!formRef.value) return
  const valid = await formRef.value.validate()
  if (!valid) return

  formLoading.value = true
  try {
    const data = formData.value as unknown as WorkOrderApi.WorkOrderSaveReqVO
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

const resetForm = () => {
  formData.value = {
    id: undefined,
    title: undefined,
    type: undefined,
    priority: 'NORMAL',
    customerId: undefined,
    assigneeId: undefined,
    slaDeadline: undefined,
    content: undefined,
    solution: undefined
  }
  formRef.value?.resetFields()
}
</script>