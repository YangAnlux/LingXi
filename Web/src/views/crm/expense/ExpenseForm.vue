<template>
  <!-- 23软4胡伟-202305566535-修改于2026.07.16 -->
  <!-- [ADD START] 费用表单弹窗 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16 -->
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
          <el-form-item :label="t('expense.no')" prop="no">
            <el-input v-model="formData.no" disabled :placeholder="t('expense.noAutoGenerate')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('expense.expenseDate')" prop="expenseDate">
            <el-date-picker
              v-model="formData.expenseDate"
              :placeholder="t('expense.expenseDatePlaceholder')"
              type="date"
              value-format="YYYY-MM-DD"
              class="!w-100%"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('expense.customerName')" prop="customerId">
            <el-select
              v-model="formData.customerId"
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
          <el-form-item :label="t('expense.type')" prop="type">
            <el-select v-model="formData.type" class="w-1/1" :placeholder="t('common.selectPlaceholder')">
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.CRM_EXPENSE_TYPE)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('expense.amount')" prop="amount">
            <el-input-number
              v-model="formData.amount"
              :min="0.01"
              :precision="2"
              class="!w-100%"
              controls-position="right"
              :placeholder="t('expense.amountPlaceholder')"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('expense.contractName')" prop="contractId">
            <el-select
              v-model="formData.contractId"
              class="w-1/1"
              filterable
              clearable
              :placeholder="t('expense.contractPlaceholder')"
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
          <el-form-item :label="t('expense.ownerUserName')" prop="ownerUserId">
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
          <el-form-item :label="t('expense.remark')" prop="remark">
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
  <!-- [ADD END] 费用表单弹窗 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16 -->
</template>
<script lang="ts" setup>
// 23软4胡伟-202305566535-修改于2026.07.16
// [ADD START] 费用表单逻辑 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
import * as ExpenseApi from '@/api/crm/expense'
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
const formData = ref<ExpenseApi.ExpenseVO>({} as ExpenseApi.ExpenseVO)
const formRules = reactive({
  type: [{ required: true, message: t('expense.typeRequired'), trigger: 'blur' }],
  amount: [{ required: true, message: t('expense.amountRequired'), trigger: 'blur' }]
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
  if (id) {
    formLoading.value = true
    try {
      formData.value = await ExpenseApi.getExpense(id)
      await handleCustomerChange(formData.value.customerId!)
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

/** 提交表单 */
const emit = defineEmits(['success'])
const submitForm = async () => {
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return
  formLoading.value = true
  try {
    const data = formData.value as unknown as ExpenseApi.ExpenseVO
    if (formType.value === 'create') {
      await ExpenseApi.createExpense(data)
      message.success(t('common.createSuccess'))
    } else {
      await ExpenseApi.updateExpense(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    emit('success')
  } finally {
    formLoading.value = false
  }
}

const resetForm = () => {
  formData.value = {} as ExpenseApi.ExpenseVO
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
// [ADD END] 费用表单逻辑 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
</script>
