<template>
  <!-- 23软4胡伟-202305566535-修改于2026.07.14 -->
  <!-- [ADD START] 发票列表页 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14 -->
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      ref="queryFormRef"
      :model="queryParams"
      class="-mb-15px"
      label-width="auto"
    >
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="t('invoice.no')" prop="no">
            <el-input
              v-model="queryParams.no"
              class="!w-240px"
              clearable
              :placeholder="t('invoice.noPlaceholder')"
              @keyup.enter="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('invoice.customerName')" prop="customerId">
            <el-select
              v-model="queryParams.customerId"
              class="!w-240px"
              :placeholder="t('customer.ownerUserPlaceholder')"
              clearable
              @keyup.enter="handleQuery"
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
              v-hasPermi="['crm:invoice:create']"
              plain
              type="primary"
              @click="openForm('create')"
            >
              <Icon class="mr-5px" icon="ep:plus" />
              {{ t('action.add') }}
            </el-button>
            <el-button
              v-hasPermi="['crm:invoice:export']"
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
    <el-table v-loading="loading" :data="list" :show-overflow-tooltip="true" :stripe="true" :table-layout="'auto'">
      <el-table-column align="center" fixed="left" :label="t('invoice.no')" prop="no" min-width="180">
        <template #default="scope">
          <el-link :underline="false" type="primary">
            {{ scope.row.no }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="t('invoice.invoiceNo')" prop="invoiceNo" min-width="180" />
      <el-table-column align="center" :label="t('invoice.customerName')" prop="customerName" min-width="120" />
      <el-table-column align="center" :label="t('invoice.contractNo')" min-width="180">
        <template #default="scope">
          <span>{{ scope.row.contract?.no || '' }}</span>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        :label="t('invoice.amount') + '（元）'"
        prop="amount"
        min-width="140"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column align="center" :label="t('invoice.type')" prop="type" min-width="130">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_INVOICE_TYPE" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column align="center" :label="t('invoice.status')" prop="status" min-width="120">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_INVOICE_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column align="center" :label="t('invoice.title_field')" prop="title" min-width="180" />
      <el-table-column align="center" :label="t('invoice.taxNo')" prop="taxNo" min-width="150" />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('invoice.issueDate')"
        prop="issueDate"
        min-width="150"
      />
      <el-table-column align="center" :label="t('invoice.remark')" prop="remark" min-width="200" />
      <el-table-column align="center" :label="t('invoice.ownerUserName')" prop="ownerUserName" min-width="120" />
      <el-table-column align="center" :label="t('invoice.creatorName')" prop="creatorName" min-width="120" />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('invoice.createTime')"
        prop="createTime"
        min-width="180"
      />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('invoice.updateTime')"
        prop="updateTime"
        min-width="180"
      />
      <el-table-column align="center" fixed="right" :label="t('common.action')" min-width="150">
        <template #default="scope">
          <el-button
            v-hasPermi="['crm:invoice:update']"
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            v-hasPermi="['crm:invoice:delete']"
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
          >
            {{ t('common.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
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
import * as InvoiceApi from '@/api/crm/invoice'
import * as CustomerApi from '@/api/crm/customer'
import { erpPriceTableColumnFormatter } from '@/utils'

defineOptions({ name: 'CrmInvoice' })

const message = useMessage()
const { t } = useI18n('crm')
const loading = ref(true)
const total = ref(0)
const list = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  no: undefined,
  customerId: undefined
})
const queryFormRef = ref()
const exportLoading = ref(false)
const customerList = ref<CustomerApi.CustomerVO[]>([])

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await InvoiceApi.getInvoicePage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

/** 添加/修改操作 */
const openForm = (_type: string, _id?: number) => {
  // TODO: Day 2 实现表单弹窗
  message.info(t('common.comingSoon'))
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    await message.delConfirm()
    await InvoiceApi.deleteInvoice(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    await message.exportConfirm()
    exportLoading.value = true
    const data = await InvoiceApi.exportInvoice(queryParams)
    download.excel(data, t('invoice.exportFileName') + '.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 初始化 **/
onMounted(async () => {
  await getList()
  customerList.value = await CustomerApi.getCustomerSimpleList()
})
</script>
<!-- [ADD END] 发票列表页 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14 -->
