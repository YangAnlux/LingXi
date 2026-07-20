<template>
  <!-- 23软4胡伟-202305566535-修改于2026.07.14 -->
  <!-- [ADD START] 发票详情页 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14 -->
  <InvoiceDetailsHeader v-loading="loading" :invoice="invoice">
    <el-button v-if="permissionListRef?.validateWrite" @click="openForm('update', invoice.id)">
      {{ t('common.edit') }}
    </el-button>
  </InvoiceDetailsHeader>
  <el-col>
    <el-tabs>
      <el-tab-pane :label="t('invoice.basicInfoTab')">
        <InvoiceDetailsInfo :invoice="invoice" />
      </el-tab-pane>
      <el-tab-pane :label="t('invoice.operateLogTab')">
        <OperateLogV2 :log-list="logList" />
      </el-tab-pane>
      <el-tab-pane :label="t('invoice.teamMemberTab')">
        <PermissionList
          ref="permissionListRef"
          :biz-id="invoice.id!"
          :biz-type="BizTypeEnum.CRM_INVOICE"
          :show-action="true"
          @quit-team="close"
        />
      </el-tab-pane>
    </el-tabs>
  </el-col>
  <InvoiceForm ref="formRef" @success="getInvoice(invoice.id)" />
  <!-- [ADD END] 发票详情页 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14 -->
</template>
<script lang="ts" setup>
// 23软4胡伟-202305566535-修改于2026.07.14
// [ADD START] 发票详情页逻辑 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
import { useTagsViewStore } from '@/store/modules/tagsView'
import * as InvoiceApi from '@/api/crm/invoice'
import InvoiceDetailsHeader from './InvoiceDetailsHeader.vue'
import InvoiceDetailsInfo from './InvoiceDetailsInfo.vue'
import PermissionList from '@/views/crm/permission/components/PermissionList.vue'
import { BizTypeEnum } from '@/api/crm/permission'
import { OperateLogVO } from '@/api/system/operatelog'
import { getOperateLogPage } from '@/api/crm/operateLog'
import InvoiceForm from '@/views/crm/invoice/InvoiceForm.vue'

defineOptions({ name: 'CrmInvoiceDetail' })
const props = defineProps<{ id?: number }>()

const { t } = useI18n('crm')
const route = useRoute()
const message = useMessage()
const invoiceId = ref(0)
const loading = ref(true)
const invoice = ref<InvoiceApi.InvoiceVO>({} as InvoiceApi.InvoiceVO)
const permissionListRef = ref<InstanceType<typeof PermissionList>>()

/** 获取详情 */
const getInvoice = async (id: number) => {
  loading.value = true
  try {
    invoice.value = await InvoiceApi.getInvoice(id)
    await getOperateLog(id)
  } finally {
    loading.value = false
  }
}

/** 编辑 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

/** 获取操作日志 */
const logList = ref<OperateLogVO[]>([])
const getOperateLog = async (invoiceId: number) => {
  if (!invoiceId) return
  const data = await getOperateLogPage({
    bizType: BizTypeEnum.CRM_INVOICE,
    bizId: invoiceId
  })
  logList.value = data.list
}

/** 关闭窗口 */
const { delView } = useTagsViewStore()
const { currentRoute } = useRouter()
const close = () => {
  delView(unref(currentRoute))
}

/** 初始化 */
const { params } = useRoute()
onMounted(async () => {
  const id = props.id || route.params.id
  if (!id) {
    message.warning(t('invoice.paramError'))
    close()
    return
  }
  invoiceId.value = id
  await getInvoice(invoiceId.value)
})
// [ADD END] 发票详情页逻辑 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
</script>
