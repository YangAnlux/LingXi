<template>
  <!-- 23软4胡伟-202305566535-修改于2026.07.14 -->
  <!-- [ADD START] 发票表单弹窗 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14 -->
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
          <el-form-item :label="t('invoice.no')" prop="no">
            <el-input v-model="formData.no" disabled :placeholder="t('invoice.noAutoGenerate')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('invoice.invoiceNo')" prop="invoiceNo">
            <el-input v-model="formData.invoiceNo" :placeholder="t('invoice.invoiceNoPlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('invoice.customerName')" prop="customerId">
            <el-select
              v-model="formData.customerId"
              :disabled="formType !== 'create'"
              class="w-1/1"
              filterable
              clearable
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
          <el-form-item :label="t('invoice.contractName')" prop="contractId">
            <el-select
              v-model="formData.contractId"
              :disabled="formType !== 'create' || !formData.customerId"
              class="w-1/1"
              filterable
              clearable
              :placeholder="t('invoice.contractPlaceholder')"
            >
              <el-option
                v-for="data in contractList"
                :key="data.id"
                :label="data.name"
                :value="data.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('invoice.type')" prop="type">
            <el-select v-model="formData.type" class="w-1/1" :placeholder="t('common.selectPlaceholder')">
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.CRM_INVOICE_TYPE)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('invoice.amount')" prop="amount">
            <el-input-number
              v-model="formData.amount"
              :min="0.01"
              :precision="2"
              class="!w-100%"
              controls-position="right"
              :placeholder="t('invoice.amountPlaceholder')"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('invoice.title_field')" prop="title">
            <el-input v-model="formData.title" :placeholder="t('invoice.titlePlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('invoice.taxNo')" prop="taxNo">
            <el-input v-model="formData.taxNo" :placeholder="t('invoice.taxNoPlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('invoice.issueDate')" prop="issueDate">
            <el-date-picker
              v-model="formData.issueDate"
              :placeholder="t('invoice.issueDatePlaceholder')"
              type="date"
              value-format="YYYY-MM-DD"
              class="!w-100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('invoice.ownerUserName')" prop="ownerUserId">
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
        <el-col :span="24">
          <el-form-item :label="t('invoice.remark')" prop="remark">
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
  <!-- [ADD END] 发票表单弹窗 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14 -->
</template>
<script lang="ts" setup>
// 23软4胡伟-202305566535-修改于2026.07.14
// [ADD START] 发票表单逻辑 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
import * as InvoiceApi from '@/api/crm/invoice'
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
const formData = ref<InvoiceApi.InvoiceVO>({} as InvoiceApi.InvoiceVO)
const formRules = reactive({
  invoiceNo: [{ required: true, message: t('invoice.invoiceNoRequired'), trigger: 'blur' }],
  contractId: [{ required: true, message: t('invoice.contractIdRequired'), trigger: 'blur' }],
  type: [{ required: true, message: t('invoice.typeRequired'), trigger: 'blur' }],
  amount: [{ required: true, message: t('invoice.amountRequired'), trigger: 'blur' }]
})
const formRef = ref()
const customerList = ref<CustomerApi.CustomerVO[]>([])
const contractList = ref<ContractApi.ContractVO[]>([])

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type, { scope: 'common' })
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      const data = await InvoiceApi.getInvoice(id)
      formData.value = data
      await handleCustomerChange(data.customerId!)
      formData.value.contractId = data.contract?.id
    } finally {
      formLoading.value = false
    }
  }
  // 获得用户列表
  userOptions.value = await UserApi.getSimpleUserList()
  // 获得客户列表
  customerList.value = await CustomerApi.getCustomerSimpleList()
  // 默认新建时选中自己
  if (formType.value === 'create') {
    formData.value.ownerUserId = useUserStore().getUser.id
  }
}
defineExpose({ open })

/** 提交表单 */
const emit = defineEmits(['success'])
const submitForm = async () => {
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return
  formLoading.value = true
  try {
    const data = formData.value as unknown as InvoiceApi.InvoiceVO
    if (formType.value === 'create') {
      await InvoiceApi.createInvoice(data)
      message.success(t('common.createSuccess'))
    } else {
      await InvoiceApi.updateInvoice(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    emit('success')
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {} as InvoiceApi.InvoiceVO
  formRef.value?.resetFields()
}

/** 处理切换客户 */
const handleCustomerChange = async (customerId: number) => {
  formData.value.contractId = undefined
  if (customerId) {
    contractList.value = []
    contractList.value = await ContractApi.getContractSimpleList(customerId)
  }
}
// [ADD END] 发票表单逻辑 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
</script>
