<template>
  <!-- 23软4胡伟-202305566535-修改于2026.07.16 -->
  <!-- [ADD START] 报销详情信息 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16 -->
  <ContentWrap>
    <el-collapse v-model="activeNames">
      <el-collapse-item name="basicInfo">
        <template #title>
          <span class="text-base font-bold">{{ t('customer.basicInfoTab') }}</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item :label="t('reimbursement.no')">{{ reimbursement.no }}</el-descriptions-item>
          <el-descriptions-item :label="t('reimbursement.customerName')">
            {{ reimbursement.customerName }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('reimbursement.contractName')">
            {{ reimbursement.contractName }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('reimbursement.reimbursementDate')">
            {{ reimbursement.reimbursementDate }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('reimbursement.totalAmount') + '（元）'">
            {{ erpPriceInputFormatter(reimbursement.totalAmount) }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('reimbursement.type')">
            <dict-tag :type="DICT_TYPE.CRM_EXPENSE_TYPE" :value="reimbursement.type" />
          </el-descriptions-item>
          <el-descriptions-item :label="t('reimbursement.status')">
            <dict-tag :type="DICT_TYPE.CRM_REIMBURSEMENT_STATUS" :value="reimbursement.status" />
          </el-descriptions-item>
          <el-descriptions-item :label="t('reimbursement.remark')">{{ reimbursement.remark }}</el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
      <el-collapse-item name="systemInfo">
        <template #title>
          <span class="text-base font-bold">{{ t('invoice.systemInfo') }}</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item :label="t('reimbursement.ownerUserName')">
            {{ reimbursement.ownerUserName }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('common.creator')">
            {{ reimbursement.creatorName }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('reimbursement.createTime')">
            {{ formatDate(reimbursement.createTime) }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('common.updateTime')">
            {{ formatDate(reimbursement.updateTime) }}
          </el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
    </el-collapse>
  </ContentWrap>
</template>
<script setup lang="ts">
import * as ReimbursementApi from '@/api/crm/reimbursement'
import { DICT_TYPE } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
import { erpPriceInputFormatter } from '@/utils'

const { t } = useI18n('crm')

const { reimbursement } = defineProps<{ reimbursement: ReimbursementApi.ReimbursementVO }>()

const activeNames = ref(['basicInfo', 'systemInfo'])
</script>
<!-- [ADD END] 报销详情信息 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16 -->
