<template>
  <!-- 23软4胡伟-202305566535-修改于2026.07.16 -->
  <!-- [ADD START] 报销列表页 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16 -->
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form ref="queryFormRef" :model="queryParams" class="-mb-15px" label-width="auto">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="t('reimbursement.no')" prop="no">
            <el-input
              v-model="queryParams.no"
              class="!w-240px"
              clearable
              :placeholder="t('reimbursement.noPlaceholder')"
              @keyup.enter="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('reimbursement.customerName')" prop="customerId">
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
              v-hasPermi="['crm:reimbursement:create']"
              plain
              type="primary"
              @click="openForm('create')"
            >
              <Icon class="mr-5px" icon="ep:plus" />
              {{ t('action.add') }}
            </el-button>
            <el-button
              v-hasPermi="['crm:reimbursement:export']"
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
      <el-table-column align="center" :label="t('reimbursement.no')" prop="no" min-width="180">
        <template #default="scope">
          <el-link :underline="false" type="primary" @click="openDetail(scope.row.id)">
            {{ scope.row.no }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="t('reimbursement.customerName')" prop="customerName" min-width="120" />
      <el-table-column align="center" :label="t('reimbursement.contractName')" prop="contractName" min-width="120" />
      <el-table-column align="center" :label="t('reimbursement.totalAmount') + '（元）'" min-width="140">
        <template #default="scope">
          {{ erpPriceTableColumnFormatter(scope.row, {}, scope.row.totalAmount) }}
        </template>
      </el-table-column>
      <el-table-column align="center" :label="t('reimbursement.type')" min-width="130">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_EXPENSE_TYPE" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column align="center" :label="t('reimbursement.status')" min-width="130">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_REIMBURSEMENT_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('reimbursement.reimbursementDate')"
        prop="reimbursementDate"
        min-width="150"
      />
      <el-table-column align="center" :label="t('reimbursement.ownerUserName')" prop="ownerUserName" min-width="120" />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('reimbursement.createTime')"
        prop="createTime"
        min-width="180"
      />
      <el-table-column align="center" fixed="right" :label="t('common.action')" min-width="250">
        <template #default="scope">
          <el-button
            v-hasPermi="['crm:reimbursement:update']"
            v-if="scope.row.status === 0"
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            v-hasPermi="['crm:reimbursement:submit']"
            v-if="scope.row.status === 0"
            link
            type="success"
            @click="handleSubmit(scope.row.id)"
          >
            {{ t('reimbursement.submit') }}
          </el-button>
          <el-button
            v-hasPermi="['crm:reimbursement:delete']"
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
    <ReimbursementForm ref="formRef" @success="getList" />
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
import * as ReimbursementApi from '@/api/crm/reimbursement'
import ReimbursementForm from './ReimbursementForm.vue'
import * as CustomerApi from '@/api/crm/customer'
import { erpPriceTableColumnFormatter } from '@/utils'

defineOptions({ name: 'CrmReimbursement' })

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
    const data = await ReimbursementApi.getReimbursementPage(queryParams)
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

/** 提交审批 */
const handleSubmit = async (id: number) => {
  try {
    await message.confirm(t('reimbursement.submitConfirm'))
    await ReimbursementApi.submitReimbursement(id)
    message.success(t('reimbursement.submitSuccess'))
    await getList()
  } catch {}
}

const handleDelete = async (id: number) => {
  try {
    await message.delConfirm()
    await ReimbursementApi.deleteReimbursement(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}

const handleExport = async () => {
  try {
    await message.exportConfirm()
    exportLoading.value = true
    const data = await ReimbursementApi.exportReimbursement(queryParams)
    download.excel(data, t('reimbursement.exportFileName') + '.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

onMounted(async () => {
  await getList()
  customerList.value = await CustomerApi.getCustomerSimpleList()
})

/** 打开详情页 */
const { push } = useRouter()
const openDetail = (id: number) => {
  push({ name: 'CrmReimbursementDetail', params: { id } })
}
</script>
<!-- [ADD END] 报销列表页 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16 -->
