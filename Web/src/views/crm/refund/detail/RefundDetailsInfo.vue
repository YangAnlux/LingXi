<template>
  <!-- 23软4胡伟-202305566535-修改于2026.07.17 -->
  <!-- [ADD START] 退款详情信息 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17 -->
  <ContentWrap>
    <el-descriptions :column="3">
      <el-descriptions-item :label="t('refund.no')">
        {{ refund.no }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('refund.customerName')">
        {{ refund.customerName }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('refund.contractName')">
        {{ refund.contractName }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('refund.refundAmount')">
        {{ erpPriceTableColumnFormatter(refund, {}, refund.refundAmount) }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('refund.refundType')">
        <dict-tag :type="DICT_TYPE.CRM_REFUND_TYPE" :value="refund.refundType" />
      </el-descriptions-item>
      <el-descriptions-item :label="t('refund.refundDate')">
        {{ dateFormatter(refund, {}, refund.refundDate) }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('refund.refundReason')">
        {{ refund.refundReason }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('refund.status')">
        <dict-tag :type="DICT_TYPE.CRM_REFUND_STATUS" :value="refund.status" />
      </el-descriptions-item>
      <el-descriptions-item :label="t('refund.ownerUserName')">
        {{ refund.ownerUserName }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('refund.remark')" :span="3">
        {{ refund.remark }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('refund.createTime')">
        {{ dateFormatter(refund, {}, refund.createTime) }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('refund.creatorName')">
        {{ refund.creatorName }}
      </el-descriptions-item>
      <el-descriptions-item />
    </el-descriptions>
  </ContentWrap>
</template>
<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import { erpPriceTableColumnFormatter } from '@/utils'
import type { RefundVO } from '@/api/crm/refund'

defineOptions({ name: 'RefundDetailsInfo' })
defineProps<{ refund: RefundVO }>()
const { t } = useI18n('crm')
</script>
<!-- [ADD END] 退款详情信息 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17 -->
