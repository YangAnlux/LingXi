<template>
  <ContentWrap>
    <el-form class="-mb-15px" :model="queryParams" ref="queryFormRef" label-width="auto">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="t('campaign.holiday.name')" prop="name">
            <el-input v-model="queryParams.name" :placeholder="t('campaign.holiday.namePlaceholder')" clearable class="!w-240px" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('campaign.holiday.status')" prop="status">
            <el-select v-model="queryParams.status" :placeholder="t('common.all')" class="!w-240px" clearable>
              <el-option :value="0" :label="t('campaign.holiday.statusEnabled')" />
              <el-option :value="1" :label="t('campaign.holiday.statusDisabled')" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('campaign.holiday.holidayDate')" prop="holidayDate">
            <el-date-picker
              v-model="queryParams.holidayDate"
              type="date"
              :placeholder="t('campaign.holiday.holidayDatePlaceholder')"
              class="!w-240px"
              value-format="YYYY-MM-DD"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item>
            <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> {{ t('common.search') }}</el-button>
            <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> {{ t('common.reset') }}</el-button>
            <el-button type="primary" @click="openForm('create')" v-hasPermi="['crm:holiday:create']">
              <Icon icon="ep:plus" class="mr-5px" /> {{ t('action.add') }}
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true">
      <el-table-column :label="t('campaign.holiday.name')" align="center" prop="name" min-width="120" />
      <el-table-column :label="t('campaign.holiday.holidayDate')" align="center" min-width="120">
        <template #default="scope">{{ scope.row.holidayDate }}</template>
      </el-table-column>
      <el-table-column :label="t('campaign.holiday.templateCode')" align="center" prop="templateCode" min-width="160" />
      <el-table-column :label="t('campaign.holiday.status')" align="center" min-width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 0 ? 'success' : 'info'">
            {{ scope.row.status === 0 ? t('campaign.holiday.statusEnabled') : t('campaign.holiday.statusDisabled') }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="t('campaign.holiday.remark')" align="center" prop="remark" min-width="150" />
      <el-table-column :label="t('common.action')" align="center" min-width="160" fixed="right">
        <template #default="scope">
          <el-button link type="primary" @click="openForm('update', scope.row.id)" v-hasPermi="['crm:holiday:update']">
            {{ t('common.edit') }}
          </el-button>
          <el-button link type="danger" @click="handleDelete(scope.row.id)" v-hasPermi="['crm:holiday:delete']">
            {{ t('common.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <Pagination :total="total" v-model:page="queryParams.pageNo" v-model:limit="queryParams.pageSize" @pagination="getList" />
  </ContentWrap>

  <Dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form ref="formRef" v-loading="formLoading" :model="formData" :rules="formRules" label-width="auto">
      <el-form-item :label="t('campaign.holiday.name')" prop="name">
        <el-input v-model="formData.name" :placeholder="t('campaign.holiday.namePlaceholder')" />
      </el-form-item>
      <el-form-item :label="t('campaign.holiday.holidayDate')" prop="holidayDate">
        <el-date-picker
          v-model="formData.holidayDate"
          type="date"
          :placeholder="t('campaign.holiday.holidayDatePlaceholder')"
          class="w-1/1"
          value-format="YYYY-MM-DD"
        />
      </el-form-item>
      <el-form-item :label="t('campaign.holiday.templateCode')" prop="templateCode">
        <el-input v-model="formData.templateCode" :placeholder="t('campaign.holiday.templateCodePlaceholder')" />
      </el-form-item>
      <el-form-item :label="t('campaign.holiday.status')" prop="status">
        <el-select v-model="formData.status" class="w-1/1">
          <el-option :value="0" :label="t('campaign.holiday.statusEnabled')" />
          <el-option :value="1" :label="t('campaign.holiday.statusDisabled')" />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('campaign.holiday.remark')" prop="remark">
        <el-input v-model="formData.remark" :placeholder="t('campaign.holiday.remarkPlaceholder')" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="dialogVisible = false">{{ t('common.cancel') }}</el-button>
      <el-button type="primary" @click="submitForm">{{ t('common.confirm') }}</el-button>
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import * as HolidayApi from '@/api/crm/care/holiday'

defineOptions({ name: 'CrmHolidayConfig' })

const { t } = useI18n('crm')
const message = useMessage()
const loading = ref(true)
const total = ref(0)
const list = ref([])
const queryParams = reactive({
  pageNo: 1, pageSize: 10,
  name: null, holidayDate: null, status: null
})
const queryFormRef = ref()

const getList = async () => {
  loading.value = true
  try {
    const data = await HolidayApi.getHolidayPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally { loading.value = false }
}

const handleQuery = () => { queryParams.pageNo = 1; getList() }
const resetQuery = () => { queryFormRef.value.resetFields(); handleQuery() }

const handleDelete = async (id: number) => {
  try {
    await message.delConfirm()
    await HolidayApi.deleteHoliday(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}

// Dialog
const dialogVisible = ref(false)
const formType = ref('create')
const formLoading = ref(false)
const formRef = ref()
const formData = reactive({
  id: null, name: '', holidayDate: '', templateCode: 'crm_holiday_care', status: 0, remark: ''
})
const formRules = {
  name: [{ required: true, message: t('campaign.holiday.nameRequired'), trigger: 'blur' }],
  holidayDate: [{ required: true, message: t('campaign.holiday.holidayDateRequired'), trigger: 'change' }]
}

const dialogTitle = computed(() =>
  formType.value === 'create' ? t('common.add') + t('campaign.holiday.title') : t('common.edit') + t('campaign.holiday.title')
)

const openForm = async (type: string, id?: number) => {
  formType.value = type
  formLoading.value = true
  try {
    resetForm()
    if (type === 'update' && id) {
      const data = await HolidayApi.getHoliday(id)
      Object.assign(formData, data)
    }
    dialogVisible.value = true
  } finally { formLoading.value = false }
}

const resetForm = () => {
  formData.id = null; formData.name = ''
  formData.holidayDate = ''; formData.templateCode = 'crm_holiday_care'
  formData.status = 0; formData.remark = ''
}

const submitForm = async () => {
  try {
    await formRef.value.validate()
    formLoading.value = true
    if (formType.value === 'create') {
      await HolidayApi.createHoliday(formData)
      message.success(t('common.addSuccess'))
    } else {
      await HolidayApi.updateHoliday(formData)
      message.success(t('common.editSuccess'))
    }
    dialogVisible.value = false
    getList()
  } finally { formLoading.value = false }
}

onMounted(() => getList())
</script>
