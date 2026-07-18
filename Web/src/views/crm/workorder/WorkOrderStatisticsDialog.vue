// 23软件工程4班蔡磊202305566515
<template>
  <el-dialog v-model="dialogVisible" :title="t('workorder.statisticsTitle')" width="80%" top="5vh" @update:model-value="handleUpdateVisible">
    <WorkOrderStatistics ref="statisticsRef" :visible="dialogVisible" />
  </el-dialog>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import WorkOrderStatistics from './WorkOrderStatistics.vue'

const props = defineProps<{ visible: boolean }>()
const emit = defineEmits(['update:visible'])

const { t } = useI18n('crm')

const dialogVisible = computed({
  get: () => props.visible,
  set: (val) => emit('update:visible', val)
})

const handleUpdateVisible = (val: boolean) => {
  emit('update:visible', val)
}
</script>
