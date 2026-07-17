<template>
  <!-- 23软4胡伟-202305566535-修改于2026.07.17 -->
  <!-- [ADD START] 退款详情页 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17 -->
  <RefundDetailsHeader v-loading="loading" :refund="refund">
    <el-button v-if="permissionListRef?.validateWrite" @click="openForm('update', refund.id)">
      {{ t('common.edit') }}
    </el-button>
  </RefundDetailsHeader>
  <el-col>
    <el-tabs>
      <el-tab-pane :label="t('refund.basicInfoTab')">
        <RefundDetailsInfo :refund="refund" />
      </el-tab-pane>
      <el-tab-pane :label="t('refund.operateLogTab')">
        <OperateLogV2 :log-list="logList" />
      </el-tab-pane>
      <el-tab-pane :label="t('refund.teamMemberTab')">
        <PermissionList
          ref="permissionListRef"
          :biz-id="refund.id!"
          :biz-type="BizTypeEnum.CRM_REFUND"
          :show-action="true"
          @quit-team="close"
        />
      </el-tab-pane>
    </el-tabs>
  </el-col>

  <RefundForm ref="formRef" @success="getRefund(refund.id)" />
</template>
<script lang="ts" setup>
import { useTagsViewStore } from '@/store/modules/tagsView'
import * as RefundApi from '@/api/crm/refund'
import RefundDetailsHeader from './RefundDetailsHeader.vue'
import RefundDetailsInfo from './RefundDetailsInfo.vue'
import PermissionList from '@/views/crm/permission/components/PermissionList.vue'
import { BizTypeEnum } from '@/api/crm/permission'
import { OperateLogVO } from '@/api/system/operatelog'
import { getOperateLogPage } from '@/api/crm/operateLog'
import RefundForm from '@/views/crm/refund/RefundForm.vue'

defineOptions({ name: 'CrmRefundDetail' })
const props = defineProps<{ id?: number }>()

const { t } = useI18n('crm')
const route = useRoute()
const message = useMessage()
const loading = ref(true)
const refund = ref<RefundApi.RefundVO>({} as RefundApi.RefundVO)
const permissionListRef = ref<InstanceType<typeof PermissionList>>()

const getRefund = async (id: number) => {
  loading.value = true
  try {
    refund.value = await RefundApi.getRefund(id)
    await getOperateLog(id)
  } finally {
    loading.value = false
  }
}

const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

const logList = ref<OperateLogVO[]>([])
const getOperateLog = async (refundId: number) => {
  if (!refundId) return
  const data = await getOperateLogPage({
    bizType: BizTypeEnum.CRM_REFUND,
    bizId: refundId
  })
  logList.value = data.list
}

const { delView } = useTagsViewStore()
const { currentRoute } = useRouter()
const close = () => {
  delView(unref(currentRoute))
  message.success(t('common.quitSuccess'))
}

watch(() => props.id, (val) => {
  if (val) getRefund(val)
}, { immediate: true })

watch(() => route.query.reload, () => {
  if (props.id) getRefund(props.id)
})
</script>
<!-- [ADD END] 退款详情页 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17 -->
