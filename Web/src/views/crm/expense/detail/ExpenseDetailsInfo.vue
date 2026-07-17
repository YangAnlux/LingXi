<template>
  <!-- 23软4胡伟-202305566535-修改于2026.07.16 -->
  <!-- [ADD START] 费用详情信息 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16 -->
  <ContentWrap>
    <el-collapse v-model="activeNames">
      <el-collapse-item name="basicInfo">
        <template #title>
          <span class="text-base font-bold">{{ t('customer.basicInfoTab') }}</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item :label="t('expense.no')">{{ expense.no }}</el-descriptions-item>
          <el-descriptions-item :label="t('expense.customerName')">{{ expense.customerName }}</el-descriptions-item>
          <el-descriptions-item :label="t('expense.contractName')">{{ expense.contractName }}</el-descriptions-item>
          <el-descriptions-item :label="t('expense.expenseDate')">{{ expense.expenseDate }}</el-descriptions-item>
          <el-descriptions-item :label="t('expense.amount') + '（元）'">
            {{ erpPriceInputFormatter(expense.amount) }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('expense.type')">
            <dict-tag :type="DICT_TYPE.CRM_EXPENSE_TYPE" :value="expense.type" />
          </el-descriptions-item>
          <el-descriptions-item :label="t('expense.remark')">{{ expense.remark }}</el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
      <el-collapse-item name="systemInfo">
        <template #title>
          <span class="text-base font-bold">{{ t('invoice.systemInfo') }}</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item :label="t('expense.ownerUserName')">{{ expense.ownerUserName }}</el-descriptions-item>
          <el-descriptions-item :label="t('common.creator')">{{ expense.creatorName }}</el-descriptions-item>
          <el-descriptions-item :label="t('expense.createTime')">{{ formatDate(expense.createTime) }}</el-descriptions-item>
          <el-descriptions-item :label="t('common.updateTime')">{{ formatDate(expense.updateTime) }}</el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
    </el-collapse>
  </ContentWrap>
</template>
<script setup lang="ts">
import * as ExpenseApi from '@/api/crm/expense'
import { DICT_TYPE } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
import { erpPriceInputFormatter } from '@/utils'

const { t } = useI18n('crm')
const { expense } = defineProps<{ expense: ExpenseApi.ExpenseVO }>()
const activeNames = ref(['basicInfo', 'systemInfo'])
</script>
<!-- [ADD END] 费用详情信息 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16 -->
