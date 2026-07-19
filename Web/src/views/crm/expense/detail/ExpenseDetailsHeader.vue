<template>
  <!-- 23软4胡伟-202305566535-修改于2026.07.16 -->
  <!-- [ADD START] 费用详情头部 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16 -->
  <div>
    <div class="flex items-start justify-between">
      <div>
        <el-col>
          <el-row>
            <span class="text-xl font-bold">{{ expense.no }}</span>
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
      <el-descriptions-item :label="t('expense.customerName')">
        {{ expense.customerName }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('expense.contractName')">
        {{ expense.contractName }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('expense.amount') + '（元）'">
        {{ erpPriceInputFormatter(expense.amount) }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('expense.type')">
        <dict-tag :type="DICT_TYPE.CRM_EXPENSE_TYPE" :value="expense.type" />
      </el-descriptions-item>
      <el-descriptions-item :label="t('expense.ownerUserName')">
        {{ expense.ownerUserName }}
      </el-descriptions-item>
    </el-descriptions>
  </ContentWrap>
</template>
<script lang="ts" setup>
import * as ExpenseApi from '@/api/crm/expense'
import { DICT_TYPE } from '@/utils/dict'
import { erpPriceInputFormatter } from '@/utils'

const { t } = useI18n('crm')
const { expense } = defineProps<{ expense: ExpenseApi.ExpenseVO }>()
</script>
<!-- [ADD END] 费用详情头部 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16 -->
