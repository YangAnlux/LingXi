// 2023级软4蔡磊202305566515,2026年7月14日
<template>
  <div>
    <WorkOrderDetailsHeader :work-order="workOrder" :loading="loading" />
    <ContentWrap class="mt-15px">
      <el-tabs v-model="activeTab">
        <el-tab-pane :label="t('workorder.basicInfoTab')" name="basic">
          <WorkOrderDetailsInfo :work-order="workOrder" />
          <div v-if="workOrder.status === '已完结' && !workOrder.satisfactionScore" class="mt-15px">
            <el-button type="warning" @click="openSatisfactionDialog">
              {{ t('workorder.actionSatisfaction') }}
            </el-button>
          </div>
        </el-tab-pane>
        <el-tab-pane :label="t('workorder.recordTab')" name="record">
          <el-table v-loading="recordLoading" :data="recordList"
            :stripe="true" :show-overflow-tooltip="true" :table-layout="'auto'">
            <el-table-column :label="t('workorder.id')" align="center" prop="id" width="80" />
            <el-table-column :label="t('workorder.recordFromStatus')" align="center" prop="fromStatus" min-width="120">
              <template #default="scope">
                <el-tag v-if="scope.row.fromStatus === '待处理'" type="info" size="small">{{ t('workorder.statusPending') }}</el-tag>
                <el-tag v-else-if="scope.row.fromStatus === '处理中'" type="primary" size="small">{{ t('workorder.statusProcessing') }}</el-tag>
                <el-tag v-else-if="scope.row.fromStatus === '已完结'" type="success" size="small">{{ t('workorder.statusResolved') }}</el-tag>
                <el-tag v-else-if="scope.row.fromStatus === '已退回'" type="danger" size="small">{{ t('workorder.statusReturned') }}</el-tag>
                <span v-else>{{ scope.row.fromStatus || '-' }}</span>
              </template>
            </el-table-column>
            <el-table-column :label="t('workorder.recordToStatus')" align="center" prop="toStatus" min-width="120">
              <template #default="scope">
                <el-tag v-if="scope.row.toStatus === '待处理'" type="info" size="small">{{ t('workorder.statusPending') }}</el-tag>
                <el-tag v-else-if="scope.row.toStatus === '处理中'" type="primary" size="small">{{ t('workorder.statusProcessing') }}</el-tag>
                <el-tag v-else-if="scope.row.toStatus === '已完结'" type="success" size="small">{{ t('workorder.statusResolved') }}</el-tag>
                <el-tag v-else-if="scope.row.toStatus === '已退回'" type="danger" size="small">{{ t('workorder.statusReturned') }}</el-tag>
                <span v-else>{{ scope.row.toStatus || '-' }}</span>
              </template>
            </el-table-column>
            <el-table-column :label="t('workorder.recordCreator')" align="center" prop="creator" min-width="100">
              <template #default="scope">
                {{ scope.row.creator || '-' }}
              </template>
            </el-table-column>
            <el-table-column
              :label="t('workorder.createTime')"
              align="center"
              prop="createTime"
              :formatter="dateFormatter"
              min-width="180"
            />
          </el-table>
          <el-empty v-if="!recordLoading && (!recordList || recordList.length === 0)" :description="t('workorder.noRecord')" />
        </el-tab-pane>
      </el-tabs>
    </ContentWrap>
    <!-- 23软件工程4班蔡磊202305566515 满意度回访弹窗 -->
    <el-dialog v-model="satisfactionDialogVisible" :title="t('workorder.satisfactionTitle')" width="450px">
    <el-form :model="satisfactionForm">
      <el-form-item :label="t('workorder.satisfactionScore')">
        <el-rate v-model="satisfactionForm.satisfactionScore" show-score text-color="#ff9900" />
      </el-form-item>
      <el-form-item :label="t('workorder.satisfactionComment')">
        <el-input
          v-model="satisfactionForm.satisfactionComment"
          type="textarea"
          :placeholder="t('workorder.satisfactionCommentPlaceholder')"
          :rows="3"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button type="primary" @click="submitSatisfaction" :disabled="!satisfactionForm.satisfactionScore">
        {{ t('common.confirm') }}
      </el-button>
      <el-button @click="satisfactionDialogVisible = false">{{ t('common.cancel') }}</el-button>
    </template>
  </el-dialog>
  </div>
</template>
<script lang="ts" setup>
import { useTagsViewStore } from '@/store/modules/tagsView'
import { dateFormatter } from '@/utils/formatTime'
import * as WorkOrderApi from '@/api/crm/workorder'
import WorkOrderDetailsHeader from './WorkOrderDetailsHeader.vue'
import WorkOrderDetailsInfo from './WorkOrderDetailsInfo.vue'

defineOptions({ name: 'CrmWorkOrderDetail' })

const { t } = useI18n('crm')
const message = useMessage()
const { delView } = useTagsViewStore()
const { currentRoute } = useRouter()

const workOrderId = ref(0)
const loading = ref(true)
const recordLoading = ref(false)
const activeTab = ref('basic')
const recordList = ref<WorkOrderApi.WorkOrderRecordVO[]>([])

/** 工单详情 */
const workOrder = ref<WorkOrderApi.WorkOrderVO>({} as WorkOrderApi.WorkOrderVO)
const getWorkOrder = async () => {
  loading.value = true
  try {
    const data = await WorkOrderApi.getWorkOrder(workOrderId.value)
    if (data) {
      workOrder.value = data
    }
    await getRecordList()
  } catch {
    // ignore
  } finally {
    loading.value = false
  }
}

/** 获取处理记录 */
const getRecordList = async () => {
  recordLoading.value = true
  try {
    const data = await WorkOrderApi.getRecordList(workOrderId.value)
    if (data) {
      recordList.value = data
    }
  } catch {
    recordList.value = []
  } finally {
    recordLoading.value = false
  }
}

const close = () => {
  delView(unref(currentRoute))
}

/** 初始化 */
const { params } = useRoute()
onMounted(() => {
  if (!params.id) {
    message.warning(t('workorder.paramError'))
    return
  }
  workOrderId.value = Number(params.id)
  getWorkOrder()
})

// 23软件工程4班蔡磊202305566515
/** 满意度回访 */
const satisfactionDialogVisible = ref(false)
const satisfactionForm = reactive({
  satisfactionScore: 0,
  satisfactionComment: ''
})

const openSatisfactionDialog = () => {
  satisfactionForm.satisfactionScore = 0
  satisfactionForm.satisfactionComment = ''
  satisfactionDialogVisible.value = true
}

const submitSatisfaction = async () => {
  if (!satisfactionForm.satisfactionScore) return
  try {
    await WorkOrderApi.submitSatisfaction({
      id: workOrderId.value,
      satisfactionScore: satisfactionForm.satisfactionScore,
      satisfactionComment: satisfactionForm.satisfactionComment || undefined
    })
    message.success(t('workorder.satisfactionSuccess'))
    satisfactionDialogVisible.value = false
    await getWorkOrder()
  } catch {
    // ignore
  }
}
</script>
