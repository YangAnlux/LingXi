<template>
  <!-- 23软4胡伟-202305566535-修改于2026.07.16 -->
  <!-- [ADD START] 报销详情头部 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16 -->
  <div>
    <div class="flex items-start justify-between">
      <div>
        <el-col>
          <el-row>
            <span class="text-xl font-bold">{{ reimbursement.no }}</span>
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
      <el-descriptions-item :label="t('reimbursement.customerName')">
        {{ reimbursement.customerName }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('reimbursement.contractName')">
        {{ reimbursement.contractName }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('reimbursement.totalAmount') + '（元）'">
        {{ erpPriceInputFormatter(reimbursement.totalAmount) }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('reimbursement.type')">
        <dict-tag :type="DICT_TYPE.CRM_EXPENSE_TYPE" :value="reimbursement.type" />
      </el-descriptions-item>
      <el-descriptions-item :label="t('reimbursement.ownerUserName')">
        {{ reimbursement.ownerUserName }}
      </el-descriptions-item>
    </el-descriptions>
  </ContentWrap>
</template>
<script lang="ts" setup>
import * as ReimbursementApi from '@/api/crm/reimbursement'
import { DICT_TYPE } from '@/utils/dict'
import { erpPriceInputFormatter } from '@/utils'

const { t } = useI18n('crm')
const { reimbursement } = defineProps<{ reimbursement: ReimbursementApi.ReimbursementVO }>()
</script>
<!-- [ADD END] 报销详情头部 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16 -->
