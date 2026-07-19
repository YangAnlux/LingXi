// 2023级软4蔡磊202305566515,2026年7月14日
<template>
  <ContentWrap>
    <el-descriptions :column="2" border>
      <el-descriptions-item :label="t('workorder.title')">
        {{ workOrder.title }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('workorder.type')">{{ workOrder.type || '-' }}</el-descriptions-item>
      <el-descriptions-item :label="t('workorder.priority')">
        <el-tag v-if="workOrder.priority === 'URGENT'" type="danger" size="small">{{ t('workorder.priorityUrgent') }}</el-tag>
        <el-tag v-else-if="workOrder.priority === 'HIGH'" type="warning" size="small">{{ t('workorder.priorityHigh') }}</el-tag>
        <el-tag v-else-if="workOrder.priority === 'NORMAL'" type="info" size="small">{{ t('workorder.priorityNormal') }}</el-tag>
        <el-tag v-else-if="workOrder.priority === 'LOW'" size="small">{{ t('workorder.priorityLow') }}</el-tag>
        <span v-else>{{ workOrder.priority }}</span>
      </el-descriptions-item>
      <el-descriptions-item :label="t('workorder.status')">
        <el-tag v-if="workOrder.status === '待处理'" type="info" size="small">{{ t('workorder.statusPending') }}</el-tag>
        <el-tag v-else-if="workOrder.status === '处理中'" type="primary" size="small">{{ t('workorder.statusProcessing') }}</el-tag>
        <el-tag v-else-if="workOrder.status === '已完结'" type="success" size="small">{{ t('workorder.statusResolved') }}</el-tag>
        <el-tag v-else-if="workOrder.status === '已退回'" type="danger" size="small">{{ t('workorder.statusReturned') }}</el-tag>
        <span v-else>{{ workOrder.status }}</span>
      </el-descriptions-item>
      <el-descriptions-item :label="t('workorder.customerName')">
        {{ workOrder.customerName || '-' }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('workorder.assigneeName')">
        {{ workOrder.assigneeName || '-' }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('workorder.slaDeadline')">
        {{ formatDate(workOrder.slaDeadline) || '-' }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('workorder.isSlaBreached')">
        <el-tag v-if="workOrder.isSlaBreached" type="danger" size="small">{{ t('workorder.slaBreached') }}</el-tag>
        <el-tag v-else type="success" size="small">{{ t('workorder.slaNormal') }}</el-tag>
      </el-descriptions-item>
      <el-descriptions-item :label="t('workorder.createTime')">
        {{ formatDate(workOrder.createTime) }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('workorder.creatorName')">
        {{ workOrder.creatorName || '-' }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('workorder.resolvedTime')">
        {{ formatDate(workOrder.resolvedTime) || '-' }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('workorder.content')" :span="2">
        <div style="white-space: pre-wrap;">{{ workOrder.content || '-' }}</div>
      </el-descriptions-item>
      <el-descriptions-item :label="t('workorder.solution')" :span="2">
        <div style="white-space: pre-wrap;">{{ workOrder.solution || '-' }}</div>
      </el-descriptions-item>
      <el-descriptions-item :label="t('workorder.satisfactionScore')">
        <template v-if="workOrder.satisfactionScore">
          <el-rate :model-value="workOrder.satisfactionScore" disabled show-score text-color="#ff9900" />
        </template>
        <span v-else>-</span>
      </el-descriptions-item>
      <el-descriptions-item :label="t('workorder.satisfactionComment')">
        {{ workOrder.satisfactionComment || '-' }}
      </el-descriptions-item>
    </el-descriptions>
  </ContentWrap>
</template>
<script lang="ts" setup>
import * as WorkOrderApi from '@/api/crm/workorder'
import { formatDate } from '@/utils/formatTime'

defineOptions({ name: 'CrmWorkOrderDetailsInfo' })

const { t } = useI18n('crm')

defineProps<{
  workOrder: WorkOrderApi.WorkOrderVO
}>()
</script>
