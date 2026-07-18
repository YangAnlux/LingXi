<!-- 待审核退款 -->
<template>
  <!-- 23软4胡伟-202305566535-修改于2026.07.17 -->
  <!-- [ADD START] 退款审批待办列表 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17 -->
  <ContentWrap>
    <div class="pb-5 text-xl">{{ t('backlog.refundAudit') }}</div>
    <!-- 搜索工作区 -->
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      :inline="true"
      label-width="68px"
    >
      <el-form-item :label="t('contract.auditStatus')" prop="auditStatus">
        <el-select
          v-model="queryParams.auditStatus"
          class="!w-240px"
          :placeholder="t('common.status')"
          @change="handleQuery"
        >
          <el-option
            v-for="(option, index) in AUDIT_STATUS"
            :label="option.label"
            :value="option.value"
            :key="index"
          />
        </el-select>
      </el-form-item>
    </el-form>
  </ContentWrap>
  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column align="center" fixed="left" :label="t('refund.no')" prop="no" min-width="180">
        <template #default="scope">
          <el-link :underline="false" type="primary" @click="openDetail(scope.row.id)">
            {{ scope.row.no }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="t('refund.customerName')" prop="customerName" min-width="120">
        <template #default="scope">
          <el-link
            :underline="false"
            type="primary"
            @click="openCustomerDetail(scope.row.customerId)"
          >
            {{ scope.row.customerName }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="t('refund.contractName')" prop="contractName" min-width="120" />
      <el-table-column
        align="center"
        :label="t('refund.refundAmount') + '（元）'"
        min-width="140"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column align="center" :label="t('refund.refundType')" min-width="130">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_REFUND_TYPE" :value="scope.row.refundType" />
        </template>
      </el-table-column>
      <el-table-column
        :formatter="dateFormatter2"
        align="center"
        :label="t('refund.refundDate')"
        prop="refundDate"
        min-width="150"
      />
      <el-table-column align="center" :label="t('refund.refundReason')" prop="refundReason" min-width="200" />
      <el-table-column align="center" :label="t('refund.ownerUserName')" prop="ownerUserName" min-width="120" />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('refund.createTime')"
        prop="createTime"
        min-width="180"
      />
      <el-table-column align="center" fixed="right" :label="t('refund.auditStatus')" prop="auditStatus" min-width="120">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_AUDIT_STATUS" :value="scope.row.auditStatus" />
        </template>
      </el-table-column>
      <el-table-column align="center" fixed="right" :label="t('common.action')" min-width="180">
        <template #default="scope">
          <el-button
            v-hasPermi="['crm:refund:update']"
            link
            type="primary"
            @click="handleProcessDetail(scope.row)"
          >
            {{ t('backlog.viewApproval') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>
  <!-- [ADD END] 退款审批待办列表 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17 -->
</template>

<script lang="ts" setup>
import { dateFormatter, dateFormatter2 } from '@/utils/formatTime'
import { DICT_TYPE } from '@/utils/dict'
import * as RefundApi from '@/api/crm/refund'
import { AUDIT_STATUS } from '@/views/crm/backlog/components/common'
import { erpPriceTableColumnFormatter } from '@/utils'
import { useRouter } from 'vue-router'

const { t } = useI18n('crm')
const loading = ref(false)
const list = ref([])
const total = ref(0)
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  auditStatus: undefined
})
const queryFormRef = ref()

const getList = async () => {
  loading.value = true
  try {
    const data = await RefundApi.getRefundPage({
      ...queryParams,
      status: 1 // 审批中
    })
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

const { push } = useRouter()
const openDetail = (id: number) => {
  push({ name: 'CrmRefundDetail', params: { id } })
}

const openCustomerDetail = (id: number) => {
  push({ name: 'CrmCustomerDetail', params: { id } })
}

const handleProcessDetail = (row) => {
  push({ name: 'BpmProcessInstanceDetail', query: { id: row.processInstanceId } })
}

onMounted(() => {
  getList()
})
</script>
