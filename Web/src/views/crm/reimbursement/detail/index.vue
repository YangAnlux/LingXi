<template>
  <!-- 23软4胡伟-202305566535-修改于2026.07.16 -->
  <!-- [ADD START] 报销详情页 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16 -->
  <ReimbursementDetailsHeader v-loading="loading" :reimbursement="reimbursement">
    <el-button v-if="permissionListRef?.validateWrite" @click="openForm('update', reimbursement.id)">
      {{ t('common.edit') }}
    </el-button>
  </ReimbursementDetailsHeader>
  <el-col>
    <el-tabs>
      <el-tab-pane :label="t('customer.basicInfoTab')">
        <ReimbursementDetailsInfo :reimbursement="reimbursement" />
      </el-tab-pane>
      <el-tab-pane :label="t('reimbursement.operateLogTab')">
        <OperateLogV2 :log-list="logList" />
      </el-tab-pane>
      <el-tab-pane :label="t('reimbursement.teamMemberTab')">
        <PermissionList
          ref="permissionListRef"
          :biz-id="reimbursement.id!"
          :biz-type="BizTypeEnum.CRM_REIMBURSEMENT"
          :show-action="true"
          @quit-team="close"
        />
      </el-tab-pane>
    </el-tabs>
  </el-col>

  <!-- 表单弹窗 -->
  <ReimbursementForm ref="formRef" @success="getReimbursement(reimbursement.id)" />
</template>
<script lang="ts" setup>
import { useTagsViewStore } from '@/store/modules/tagsView'
import * as ReimbursementApi from '@/api/crm/reimbursement'
import ReimbursementDetailsHeader from './ReimbursementDetailsHeader.vue'
import ReimbursementDetailsInfo from './ReimbursementDetailsInfo.vue'
import PermissionList from '@/views/crm/permission/components/PermissionList.vue'
import { BizTypeEnum } from '@/api/crm/permission'
import { OperateLogVO } from '@/api/system/operatelog'
import { getOperateLogPage } from '@/api/crm/operateLog'
import ReimbursementForm from '@/views/crm/reimbursement/ReimbursementForm.vue'

defineOptions({ name: 'CrmReimbursementDetail' })
const props = defineProps<{ id?: number }>()

const { t } = useI18n('crm')
const route = useRoute()
const message = useMessage()
const loading = ref(true)
const reimbursement = ref<ReimbursementApi.ReimbursementVO>({} as ReimbursementApi.ReimbursementVO)
const permissionListRef = ref<InstanceType<typeof PermissionList>>()

const getReimbursement = async (id: number) => {
  loading.value = true
  try {
    reimbursement.value = await ReimbursementApi.getReimbursement(id)
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
const getOperateLog = async (reimbursementId: number) => {
  if (!reimbursementId) return
  const data = await getOperateLogPage({
    bizType: BizTypeEnum.CRM_REIMBURSEMENT,
    bizId: reimbursementId
  })
  logList.value = data.list
}

const { delView } = useTagsViewStore()
const { currentRoute } = useRouter()
const close = () => {
  delView(unref(currentRoute))
}

const { params } = useRoute()
onMounted(async () => {
  const id = props.id || route.params.id
  if (!id) {
    message.warning(t('reimbursement.paramError'))
    close()
    return
  }
  await getReimbursement(Number(id))
})
</script>
<!-- [ADD END] 报销详情页 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16 -->
