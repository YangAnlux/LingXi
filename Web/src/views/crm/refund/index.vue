<template>
  <!-- 23软4胡伟-202305566535-修改于2026.07.17 -->
  <!-- [ADD START] 退款列表页 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17 -->
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form ref="queryFormRef" :model="queryParams" class="-mb-15px" label-width="auto">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="t('refund.no')" prop="no">
            <el-input
              v-model="queryParams.no"
              class="!w-240px"
              clearable
              :placeholder="t('refund.noPlaceholder')"
              @keyup.enter="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('refund.customerName')" prop="customerId">
            <el-select
              v-model="queryParams.customerId"
              class="!w-240px"
              :placeholder="t('customer.ownerUserPlaceholder')"
              clearable
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
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item>
            <el-button @click="handleQuery">
              <Icon class="mr-5px" icon="ep:search" />
              {{ t('common.search') }}
            </el-button>
            <el-button @click="resetQuery">
              <Icon class="mr-5px" icon="ep:refresh" />
              {{ t('common.reset') }}
            </el-button>
            <el-button
              v-hasPermi="['crm:refund:create']"
              plain
              type="primary"
              @click="openForm('create')"
            >
              <Icon class="mr-5px" icon="ep:plus" />
              {{ t('action.add') }}
            </el-button>
            <el-button
              v-hasPermi="['crm:refund:export']"
              :loading="exportLoading"
              plain
              type="success"
              @click="handleExport"
            >
              <Icon class="mr-5px" icon="ep:download" />
              {{ t('common.export') }}
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :show-overflow-tooltip="true" :stripe="true">
      <el-table-column align="center" :label="t('refund.no')" prop="no" min-width="180">
        <template #default="scope">
          <el-link :underline="false" type="primary" @click="openDetail(scope.row.id)">
            {{ scope.row.no }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="t('refund.customerName')" prop="customerName" min-width="120" />
      <el-table-column align="center" :label="t('refund.contractName')" prop="contractName" min-width="120" />
      <el-table-column align="center" :label="t('refund.refundAmount') + '（元）'" min-width="140">
        <template #default="scope">
          {{ erpPriceTableColumnFormatter(scope.row, {}, scope.row.refundAmount) }}
        </template>
      </el-table-column>
      <el-table-column align="center" :label="t('refund.refundType')" min-width="130">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_REFUND_TYPE" :value="scope.row.refundType" />
        </template>
      </el-table-column>
      <el-table-column align="center" :label="t('refund.status')" min-width="130">
        <template #default="scope">
          <!-- [MODIFY] 退款状态使用 CRM_REFUND_STATUS 字典，值 0/1/2/3 对应 待提交/审批中/已通过/已驳回 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17 -->
          <dict-tag :type="DICT_TYPE.CRM_REFUND_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('refund.refundDate')"
        prop="refundDate"
        min-width="150"
      />
      <el-table-column align="center" :label="t('refund.ownerUserName')" prop="ownerUserName" min-width="120" />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('refund.createTime')"
        prop="createTime"
        min-width="180"
      />
      <el-table-column align="center" fixed="right" :label="t('common.action')" min-width="250">
        <template #default="scope">
          <el-button
            v-hasPermi="['crm:refund:update']"
            v-if="scope.row.status === 0"
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            v-hasPermi="['crm:refund:submit']"
            v-if="scope.row.status === 0"
            link
            type="success"
            @click="handleSubmit(scope.row.id)"
          >
            {{ t('refund.submit') }}
          </el-button>
          <el-button
            v-if="scope.row.status !== 0"
            v-hasPermi="['crm:refund:update']"
            link
            type="primary"
            @click="handleProcessDetail(scope.row)"
          >
            {{ t('refund.viewApproval') }}
          </el-button>
          <el-button
            v-hasPermi="['crm:refund:delete']"
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
          >
            {{ t('common.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 表单弹窗 -->
    <RefundForm ref="formRef" @success="getList" />
    <!-- 分页 -->
    <Pagination
      v-model:limit="queryParams.pageSize"
      v-model:page="queryParams.pageNo"
      :total="total"
      @pagination="getList"
    />
  </ContentWrap>
</template>
<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import * as RefundApi from '@/api/crm/refund'
import RefundForm from './RefundForm.vue'
import * as CustomerApi from '@/api/crm/customer'
import { erpPriceTableColumnFormatter } from '@/utils'

defineOptions({ name: 'CrmRefund' })

const message = useMessage()
const { t } = useI18n('crm')
const loading = ref(true)
const total = ref(0)
const list = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  no: undefined,
  customerId: undefined,
  status: undefined
})
const queryFormRef = ref()
const exportLoading = ref(false)
const customerList = ref<CustomerApi.CustomerVO[]>([])

const getList = async () => {
  loading.value = true
  try {
    const data = await RefundApi.getRefundPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

const handleSubmit = async (id: number) => {
  try {
    await message.confirm(t('refund.submitConfirm'))
    await RefundApi.submitRefund(id)
    message.success(t('refund.submitSuccess'))
    await getList()
  } catch {
    // 用户取消
  }
}

const handleDelete = async (id: number) => {
  try {
    await message.delConfirm()
    await RefundApi.deleteRefund(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {
    // 用户取消
  }
}

const handleExport = async () => {
  try {
    exportLoading.value = true
    const data = await RefundApi.exportRefund(queryParams)
    await download.excel(data, t('refund.exportFileName') + '.xls')
  } finally {
    exportLoading.value = false
  }
}

const { push } = useRouter()
const openDetail = (id: number) => {
  push({ name: 'CrmRefundDetail', params: { id } })
}

const handleProcessDetail = (row) => {
  // 打开BPM审批详情
  push({ name: 'BpmProcessInstanceDetail', query: { id: row.processInstanceId } })
}

const getCustomerList = async () => {
  customerList.value = await CustomerApi.getCustomerSimpleList()
}

onMounted(() => {
  getList()
  getCustomerList()
})

// [ADD START] 页面重新激活时刷新列表，保证审批后状态更新 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
onActivated(() => {
  getList()
})
// [ADD END]
</script>
<!-- [ADD END] 退款列表页 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17 -->
