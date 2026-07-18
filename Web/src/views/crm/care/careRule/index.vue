<template>
  <ContentWrap>
    <el-form class="-mb-15px" :model="queryParams" ref="queryFormRef" label-width="auto">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="t('careRule.triggerType')" prop="triggerType">
            <el-select v-model="queryParams.triggerType" :placeholder="t('common.all')" class="!w-240px" clearable>
              <el-option value="BIRTHDAY" :label="t('careRule.triggerBirthday')" />
              <el-option value="HOLIDAY" :label="t('careRule.triggerHoliday')" />
              <el-option value="MANUAL" :label="t('careRule.triggerManual')" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('careRule.status')" prop="isEnabled">
            <el-select v-model="queryParams.isEnabled" :placeholder="t('common.all')" class="!w-240px" clearable>
              <el-option :value="1" :label="t('careRule.enabled')" />
              <el-option :value="0" :label="t('careRule.disabled')" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item>
            <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> {{ t('common.search') }}</el-button>
            <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> {{ t('common.reset') }}</el-button>
            <el-button type="primary" @click="openForm('create')" v-hasPermi="['crm:care-rule:create']">
              <Icon icon="ep:plus" class="mr-5px" /> {{ t('action.add') }}
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column :label="t('careRule.name')" align="center" prop="name" min-width="160" />
      <el-table-column :label="t('careRule.triggerType')" align="center" min-width="100">
        <template #default="scope">
          <el-tag :type="scope.row.triggerType === 'BIRTHDAY' ? 'success' : scope.row.triggerType === 'HOLIDAY' ? 'warning' : 'info'" size="small">
            {{ scope.row.triggerType === 'BIRTHDAY' ? t('careRule.triggerBirthday') : scope.row.triggerType === 'HOLIDAY' ? t('careRule.triggerHoliday') : t('careRule.triggerManual') }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="t('careRule.templateCode')" align="center" prop="templateCode" min-width="160" />
      <el-table-column :label="t('careRule.channel')" align="center" min-width="80">
        <template #default="scope">
          <el-tag :type="scope.row.channel === 1 ? '' : 'warning'" size="small">
            {{ scope.row.channel === 1 ? t('campaign.send.sms') : t('campaign.send.mail') }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="t('careRule.lastExecTime')" align="center" min-width="160">
        <template #default="scope">
          {{ scope.row.lastExecTime || '-' }}
        </template>
      </el-table-column>
      <el-table-column :label="t('careRule.status')" align="center" min-width="80">
        <template #default="scope">
          <el-tag :type="scope.row.isEnabled ? 'success' : 'danger'" size="small">
            {{ scope.row.isEnabled ? t('careRule.enabled') : t('careRule.disabled') }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="t('common.action')" align="center" min-width="260" fixed="right">
        <template #default="scope">
          <el-button link type="primary" @click="openForm('update', scope.row.id)" v-hasPermi="['crm:care-rule:update']">
            {{ t('common.edit') }}
          </el-button>
          <el-button link type="success" @click="handleExecute(scope.row)" v-hasPermi="['crm:care-rule:update']">
            {{ t('careRule.executeNow') }}
          </el-button>
          <el-button link type="danger" @click="handleDelete(scope.row.id)" v-hasPermi="['crm:care-rule:delete']">
            {{ t('common.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <Pagination :total="total" v-model:page="queryParams.pageNo" v-model:limit="queryParams.pageSize" @pagination="getList" />
  </ContentWrap>

  <CareRuleForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import * as CareRuleApi from '@/api/crm/care/rule'
import CareRuleForm from './CareRuleForm.vue'

defineOptions({ name: 'CrmCareRule' })

const { t } = useI18n('crm')
const message = useMessage()
const loading = ref(true)
const total = ref(0)
const list = ref([])
const queryParams = reactive({
  pageNo: 1, pageSize: 10,
  triggerType: null as string | null,
  isEnabled: null as number | null
})
const queryFormRef = ref()
const formRef = ref()

const getList = async () => {
  loading.value = true
  try {
    const data = await CareRuleApi.getCareRulePage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally { loading.value = false }
}

const handleQuery = () => { queryParams.pageNo = 1; getList() }
const resetQuery = () => { queryFormRef.value.resetFields(); handleQuery() }

const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

const handleDelete = async (id: number) => {
  try {
    await message.delConfirm()
    await CareRuleApi.deleteCareRule(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}

const handleExecute = async (row: any) => {
  try {
    await message.confirm(t('careRule.executeConfirm', { name: row.name }))
    await CareRuleApi.executeCareRule(row.id)
    message.success(t('careRule.executeSuccess'))
    await getList()
  } catch {}
}

onMounted(() => { getList() })
</script>
