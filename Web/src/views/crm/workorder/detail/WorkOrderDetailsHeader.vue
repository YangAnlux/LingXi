// 2023级软4蔡磊202305566515,2026年7月14日
<template>
  <div>
    <div v-loading="loading">
      <div class="flex items-start justify-between">
        <div>
          <span class="text-xl font-bold">{{ workOrder?.title || '-' }}</span>
        </div>
        <div>
          <slot></slot>
        </div>
      </div>
    </div>
    <ContentWrap class="mt-10px">
      <el-descriptions :column="5" direction="vertical">
        <el-descriptions-item :label="t('workorder.type')">{{ workOrder?.type || '-' }}</el-descriptions-item>
        <el-descriptions-item :label="t('workorder.priority')">
          <el-tag v-if="workOrder?.priority === 'URGENT'" type="danger" size="small">{{ t('workorder.priorityUrgent') }}</el-tag>
          <el-tag v-else-if="workOrder?.priority === 'HIGH'" type="warning" size="small">{{ t('workorder.priorityHigh') }}</el-tag>
          <span v-else>{{ workOrder?.priority || '-' }}</span>
        </el-descriptions-item>
        <el-descriptions-item :label="t('workorder.status')">
          <el-tag v-if="workOrder?.status === '待处理'" type="info" size="small">{{ t('workorder.statusPending') }}</el-tag>
          <el-tag v-else-if="workOrder?.status === '处理中'" type="primary" size="small">{{ t('workorder.statusProcessing') }}</el-tag>
          <el-tag v-else-if="workOrder?.status === '已完结'" type="success" size="small">{{ t('workorder.statusResolved') }}</el-tag>
          <el-tag v-else-if="workOrder?.status === '已退回'" type="danger" size="small">{{ t('workorder.statusReturned') }}</el-tag>
          <span v-else>{{ workOrder?.status || '-' }}</span>
        </el-descriptions-item>
        <el-descriptions-item :label="t('workorder.createTime')">{{ formatDate(workOrder?.createTime) || '-' }}</el-descriptions-item>
      </el-descriptions>
    </ContentWrap>
  </div>
</template>
<script lang="ts" setup>
import * as WorkOrderApi from '@/api/crm/workorder'
import { formatDate } from '@/utils/formatTime'

defineOptions({ name: 'CrmWorkOrderDetailsHeader' })

const { t } = useI18n('crm')

defineProps<{
  workOrder: WorkOrderApi.WorkOrderVO
  loading: boolean
}>()
</script>
