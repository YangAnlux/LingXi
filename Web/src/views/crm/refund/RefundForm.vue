<template>
  <!-- 23软4胡伟-202305566535-修改于2026.07.17 -->
  <!-- [ADD START] 退款表单页 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17 -->
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
          <el-form-item :label="t('refund.no')" prop="no">
            <el-input v-model="formData.no" disabled :placeholder="t('refund.noAutoGenerate')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('refund.ownerUserName')" prop="ownerUserId">
            <el-select
              v-model="formData.ownerUserId"
              :disabled="formType !== 'create'"
              class="w-1/1"
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
        <el-col :span="12">
          <el-form-item :label="t('refund.customerName')" prop="customerId">
            <el-select
              v-model="formData.customerId"
              :disabled="formType !== 'create'"
              class="w-1/1"
              filterable
              :placeholder="t('customer.ownerUserPlaceholder')"
              @change="handleCustomerChange"
            >
              <el-option
                v-for="item in customerList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('refund.contractName')" prop="contractId">
            <el-select
              v-model="formData.contractId"
              :disabled="formType !== 'create' || !formData.customerId"
              class="w-1/1"
              filterable
              :placeholder="t('refund.contractPlaceholder')"
            >
              <el-option
                v-for="data in contractList"
                :key="data.id"
                :label="data.name"
                :value="data.id!"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('refund.refundAmount')" prop="refundAmount">
            <el-input-number
              v-model="formData.refundAmount"
              :min="0.01"
              :precision="2"
              class="!w-100%"
              controls-position="right"
              :placeholder="t('refund.amountPlaceholder')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('refund.refundDate')" prop="refundDate">
            <el-date-picker
              v-model="formData.refundDate"
              :placeholder="t('refund.refundDate')"
              type="date"
              value-format="YYYY-MM-DD"
              class="!w-100%"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('refund.refundType')" prop="refundType">
            <el-select v-model="formData.refundType" class="w-1/1" :placeholder="t('common.selectPlaceholder')">
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.CRM_REFUND_TYPE)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('refund.refundReason')" prop="refundReason">
            <el-input v-model="formData.refundReason" :placeholder="t('refund.reasonPlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item :label="t('refund.remark')" prop="remark">
            <el-input v-model="formData.remark" :placeholder="t('customer.remarkPlaceholder')" type="textarea" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{ t('common.confirm') }}</el-button>
      <el-button @click="dialogVisible = false">{{ t('common.cancel') }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import * as RefundApi from '@/api/crm/refund'
import * as UserApi from '@/api/system/user'
import * as CustomerApi from '@/api/crm/customer'
import * as ContractApi from '@/api/crm/contract'
import { useUserStore } from '@/store/modules/user'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'

const { t } = useI18n('crm')
const message = useMessage()
const userOptions = ref<UserApi.UserVO[]>([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const formData = ref<RefundApi.RefundVO>({} as RefundApi.RefundVO)
const formRules = reactive({
  customerId: [{ required: true, message: t('refund.customerIdRequired'), trigger: 'blur' }],
  contractId: [{ required: true, message: t('refund.contractIdRequired'), trigger: 'blur' }],
  refundAmount: [{ required: true, message: t('refund.amountRequired'), trigger: 'blur' }],
  refundType: [{ required: true, message: t('refund.typeRequired'), trigger: 'blur' }],
  ownerUserId: [{ required: true, message: t('customer.ownerUserRequired'), trigger: 'blur' }]
})
const formRef = ref()
const customerList = ref<CustomerApi.CustomerVO[]>([])
const contractList = ref<ContractApi.ContractVO[]>([])

const handleCustomerChange = async (customerId: number) => {
  formData.value.contractId = undefined
  if (customerId) {
    contractList.value = await ContractApi.getContractSimpleList(customerId)
  } else {
    contractList.value = []
  }
}

const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type, { scope: 'common' })
  formType.value = type
  resetForm()
  if (id) {
    formLoading.value = true
    try {
      const data = await RefundApi.getRefund(id)
      formData.value = data
      if (data.customerId) {
        contractList.value = await ContractApi.getContractSimpleList(data.customerId)
      }
    } finally {
      formLoading.value = false
    }
  }
  userOptions.value = await UserApi.getSimpleUserList()
  customerList.value = await CustomerApi.getCustomerSimpleList()
  if (formType.value === 'create') {
    formData.value.ownerUserId = useUserStore().getUser.id
  }
}
defineExpose({ open })

const emit = defineEmits(['success'])
const submitForm = async () => {
  if (!(await formRef.value.validate())) return
  formLoading.value = true
  try {
    const data = formData.value
    if (formType.value === 'create') {
      await RefundApi.createRefund(data)
      message.success(t('common.createSuccess'))
    } else {
      await RefundApi.updateRefund(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    emit('success')
  } finally {
    formLoading.value = false
  }
}

const resetForm = () => {
  formData.value = {} as RefundApi.RefundVO
  formRef.value?.resetFields()
}
</script>
<!-- [ADD END] 退款表单页 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17 -->
