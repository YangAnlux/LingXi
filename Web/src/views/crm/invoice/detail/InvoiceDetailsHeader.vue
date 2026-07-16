<template>
  <!-- 23软4胡伟-202305566535-修改于2026.07.14 -->
  <!-- [ADD START] 发票详情头部 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14 -->
  <div>
    <div class="flex items-start justify-between">
      <div>
        <el-col>
          <el-row>
            <span class="text-xl font-bold">{{ invoice.no }}</span>
          </el-row>
        </el-col>
      </div>
      <div>
        <slot></slot>
      </div>
    </div>
  </div>
  <ContentWrap class="mt-10px">
    <el-descriptions :column="5" direction="vertical">
      <el-descriptions-item :label="t('invoice.customerName')">
        {{ invoice.customerName }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('invoice.contractNo')">
        {{ invoice.contract?.no }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('invoice.contractName')">
        {{ invoice.contractName || invoice.contract?.name }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('invoice.amount')">
        {{ erpPriceInputFormatter(invoice.amount) }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('invoice.type')">
        <dict-tag :type="DICT_TYPE.CRM_INVOICE_TYPE" :value="invoice.type" />
      </el-descriptions-item>
      <el-descriptions-item :label="t('invoice.status')">
        <dict-tag :type="DICT_TYPE.CRM_INVOICE_STATUS" :value="invoice.status" />
      </el-descriptions-item>
    </el-descriptions>
  </ContentWrap>
  <!-- [ADD END] 发票详情头部 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14 -->
</template>
<script lang="ts" setup>
// 23软4胡伟-202305566535-修改于2026.07.14
// [ADD START] 发票详情头部逻辑 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
import * as InvoiceApi from '@/api/crm/invoice'
import { DICT_TYPE } from '@/utils/dict'
import { erpPriceInputFormatter } from '@/utils'

const { t } = useI18n('crm')
const { invoice } = defineProps<{ invoice: InvoiceApi.InvoiceVO }>()
// [ADD END] 发票详情头部逻辑 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
</script>
