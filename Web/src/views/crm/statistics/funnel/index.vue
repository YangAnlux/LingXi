<!-- 数据统计 - 销售漏斗 -->
<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      ref="queryFormRef"
      :model="queryParams"
      class="-mb-15px"
      label-width="auto"
    >
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="t('timeRange')" prop="orderDate">
            <el-date-picker
              v-model="queryParams.times"
              :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
              :shortcuts="defaultShortcuts"
              class="!w-240px"
              :end-placeholder="t('common.endTime')"
              :start-placeholder="t('common.startTime')"
              type="daterange"
              value-format="YYYY-MM-DD HH:mm:ss"
              @change="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('timeInterval')" prop="interval">
            <el-select
              v-model="queryParams.interval"
              class="!w-240px"
              :placeholder="t('timeInterval')"
              @change="handleQuery"
            >
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.DATE_INTERVAL)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('dept')" prop="deptId">
            <el-tree-select
              v-model="queryParams.deptId"
              :data="deptList"
              :props="defaultProps"
              check-strictly
              class="!w-240px"
              node-key="id"
              :placeholder="t('dept')"
              @change="(queryParams.userId = undefined), handleQuery()"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="t('user')" prop="userId">
            <el-select
              v-model="queryParams.userId"
              class="!w-240px"
              clearable
              :placeholder="t('user')"
              @change="handleQuery"
            >
              <el-option
                v-for="(user, index) in userListByDeptId"
                :key="index"
                :label="user.nickname"
                :value="user.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item>
            <el-button @click="handleQuery">
              <Icon class="mr-5px" icon="ep:search" />
              {{ t('search') }}
            </el-button>
            <el-button @click="resetQuery">
              <Icon class="mr-5px" icon="ep:refresh" />
              {{ t('reset') }}
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>

  <!-- 客户统计 -->
  <el-col>
    <el-tabs v-model="activeTab">
      <el-tab-pane :label="t('funnel.funnel')" lazy name="funnelRef">
        <FunnelBusiness ref="funnelRef" :query-params="queryParams" />
      </el-tab-pane>
      <el-tab-pane :label="t('funnel.businessSummary')" lazy name="businessSummaryRef">
        <BusinessSummary ref="businessSummaryRef" :query-params="queryParams" />
      </el-tab-pane>
      <el-tab-pane :label="t('funnel.inversionRate')" lazy name="businessInversionRateSummaryRef">
        <BusinessInversionRateSummary
          ref="businessInversionRateSummaryRef"
          :query-params="queryParams"
        />
      </el-tab-pane>
      <el-tab-pane :label="t('funnel.businessReport')" lazy name="businessReportRef">
        <BusinessReport ref="businessReportRef" :query-params="queryParams" />
      </el-tab-pane>
    </el-tabs>
  </el-col>
</template>

<script lang="ts" setup>
import * as DeptApi from '@/api/system/dept'
import * as UserApi from '@/api/system/user'
import { useUserStore } from '@/store/modules/user'
import { beginOfDay, defaultShortcuts, endOfDay, formatDate } from '@/utils/formatTime'
import { defaultProps, handleTree } from '@/utils/tree'
import FunnelBusiness from './components/FunnelBusiness.vue'
import BusinessSummary from './components/BusinessSummary.vue'
import BusinessInversionRateSummary from './components/BusinessInversionRateSummary.vue'
import BusinessReport from './components/BusinessReport.vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'

defineOptions({ name: 'CrmStatisticsFunnel' })

const { t } = useI18n('crm.statistics')

const queryParams = reactive({
  interval: 2,
  deptId: useUserStore().getUser.deptId,
  userId: undefined,
  times: [
    formatDate(beginOfDay(new Date(new Date().getTime() - 3600 * 1000 * 24 * 7))),
    formatDate(endOfDay(new Date(new Date().getTime() - 3600 * 1000 * 24)))
  ]
})

const queryFormRef = ref()
const deptList = ref<Tree[]>([])
const userList = ref<UserApi.UserVO[]>([])

const userListByDeptId = computed(() =>
  queryParams.deptId
    ? userList.value.filter((u: UserApi.UserVO) => u.deptId === queryParams.deptId)
    : []
)

const activeTab = ref('funnelRef')
const funnelRef = ref()
const businessSummaryRef = ref()
const businessInversionRateSummaryRef = ref()
const businessReportRef = ref()

const handleQuery = () => {
  switch (activeTab.value) {
    case 'funnelRef':
      funnelRef.value?.loadData?.()
      break
    case 'businessSummaryRef':
      businessSummaryRef.value?.loadData?.()
      break
    case 'businessInversionRateSummaryRef':
      businessInversionRateSummaryRef.value?.loadData?.()
      break
    case 'businessReportRef':
      businessReportRef.value?.loadData?.()
      break
  }
}

watch(activeTab, () => {
  handleQuery()
})

const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

onMounted(async () => {
  deptList.value = handleTree(await DeptApi.getSimpleDeptList())
  userList.value = handleTree(await UserApi.getSimpleUserList())
})
</script>
