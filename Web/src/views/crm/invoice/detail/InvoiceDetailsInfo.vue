<template>
  <!-- 23软4胡伟-202305566535-修改于2026.07.14 -->
  <!-- [ADD START] 发票详情信息 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14 -->
  <ContentWrap>
    <el-collapse v-model="activeNames">
      <el-collapse-item name="basicInfo">
        <template #title>
          <span class="text-base font-bold">{{ t('invoice.basicInfoTab') }}</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item :label="t('invoice.no')">{{ invoice.no }}</el-descriptions-item>
          <el-descriptions-item :label="t('invoice.invoiceNo')">{{ invoice.invoiceNo }}</el-descriptions-item>
          <el-descriptions-item :label="t('invoice.customerName')">{{ invoice.customerName }}</el-descriptions-item>
          <el-descriptions-item :label="t('invoice.contractNo')">{{ invoice.contract?.no }}</el-descriptions-item>
          <el-descriptions-item :label="t('invoice.contractName')">{{ invoice.contractName || invoice.contract?.name }}</el-descriptions-item>
          <el-descriptions-item :label="t('invoice.amount')">{{ erpPriceInputFormatter(invoice.amount) }}</el-descriptions-item>
          <el-descriptions-item :label="t('invoice.type')">
            <dict-tag :type="DICT_TYPE.CRM_INVOICE_TYPE" :value="invoice.type" />
          </el-descriptions-item>
          <el-descriptions-item :label="t('invoice.status')">
            <dict-tag :type="DICT_TYPE.CRM_INVOICE_STATUS" :value="invoice.status" />
          </el-descriptions-item>
          <el-descriptions-item :label="t('invoice.title_field')">{{ invoice.title }}</el-descriptions-item>
          <el-descriptions-item :label="t('invoice.taxNo')">{{ invoice.taxNo }}</el-descriptions-item>
          <el-descriptions-item :label="t('invoice.issueDate')">{{ formatDate(invoice.issueDate, 'YYYY-MM-DD') }}</el-descriptions-item>
          <el-descriptions-item :label="t('invoice.remark')">{{ invoice.remark }}</el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
      <el-collapse-item name="systemInfo">
        <template #title>
          <span class="text-base font-bold">{{ t('invoice.systemInfo') }}</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item :label="t('invoice.ownerUserName')">{{ invoice.ownerUserName }}</el-descriptions-item>
          <el-descriptions-item :label="t('invoice.creatorName')">{{ invoice.creatorName }}</el-descriptions-item>
          <el-descriptions-item :label="t('invoice.createTime')">{{ formatDate(invoice.createTime) }}</el-descriptions-item>
          <el-descriptions-item :label="t('invoice.updateTime')">{{ formatDate(invoice.updateTime) }}</el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
    </el-collapse>
  </ContentWrap>
  <!-- [ADD END] 发票详情信息 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14 -->
</template>
<script setup lang="ts">
// 23软4胡伟-202305566535-修改于2026.07.14
// [ADD START] 发票详情信息逻辑 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
import * as InvoiceApi from '@/api/crm/invoice'
import { DICT_TYPE } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
import { erpPriceInputFormatter } from '@/utils'

const { t } = useI18n('crm')

const { invoice } = defineProps<{
  invoice: InvoiceApi.InvoiceVO
}>()

const activeNames = ref(['basicInfo', 'systemInfo'])
// [ADD END] 发票详情信息逻辑 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
</script>
