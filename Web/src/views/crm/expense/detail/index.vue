<template>
  <!-- 23软4胡伟-202305566535-修改于2026.07.16 -->
  <!-- [ADD START] 费用详情页 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16 -->
  <ExpenseDetailsHeader v-loading="loading" :expense="expense">
    <el-button v-if="permissionListRef?.validateWrite" @click="openForm('update', expense.id)">
      {{ t('common.edit') }}
    </el-button>
  </ExpenseDetailsHeader>
  <el-col>
    <el-tabs>
      <el-tab-pane :label="t('customer.basicInfoTab')">
        <ExpenseDetailsInfo :expense="expense" />
      </el-tab-pane>
      <el-tab-pane :label="t('common.operateLog')">
        <OperateLogV2 :log-list="logList" />
      </el-tab-pane>
      <el-tab-pane :label="t('receivable.teamMemberTab')">
        <PermissionList
          ref="permissionListRef"
          :biz-id="expense.id!"
          :biz-type="BizTypeEnum.CRM_EXPENSE"
          :show-action="true"
          @quit-team="close"
        />
      </el-tab-pane>
    </el-tabs>
  </el-col>

  <ExpenseForm ref="formRef" @success="getExpense(expense.id)" />
</template>
<script lang="ts" setup>
import { useTagsViewStore } from '@/store/modules/tagsView'
import * as ExpenseApi from '@/api/crm/expense'
import ExpenseDetailsHeader from './ExpenseDetailsHeader.vue'
import ExpenseDetailsInfo from './ExpenseDetailsInfo.vue'
import PermissionList from '@/views/crm/permission/components/PermissionList.vue'
import { BizTypeEnum } from '@/api/crm/permission'
import { OperateLogVO } from '@/api/system/operatelog'
import { getOperateLogPage } from '@/api/crm/operateLog'
import ExpenseForm from '@/views/crm/expense/ExpenseForm.vue'

defineOptions({ name: 'CrmExpenseDetail' })
const props = defineProps<{ id?: number }>()

const { t } = useI18n('crm')
const route = useRoute()
const message = useMessage()
const loading = ref(true)
const expense = ref<ExpenseApi.ExpenseVO>({} as ExpenseApi.ExpenseVO)
const permissionListRef = ref<InstanceType<typeof PermissionList>>()

const getExpense = async (id: number) => {
  loading.value = true
  try {
    expense.value = await ExpenseApi.getExpense(id)
    await getOperateLog(id)
  } finally {
    loading.value = false
  }
}

const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

const logList = ref<OperateLogVO[]>([])
const getOperateLog = async (expenseId: number) => {
  if (!expenseId) return
  const data = await getOperateLogPage({ bizType: BizTypeEnum.CRM_EXPENSE, bizId: expenseId })
  logList.value = data.list
}

const { delView } = useTagsViewStore()
const { currentRoute } = useRouter()
const close = () => delView(unref(currentRoute))

const { params } = useRoute()
onMounted(async () => {
  const id = props.id || route.params.id
  if (!id) {
    message.warning(t('expense.paramError'))
    close()
    return
  }
  await getExpense(Number(id))
})
</script>
<!-- [ADD END] 费用详情页 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16 -->
