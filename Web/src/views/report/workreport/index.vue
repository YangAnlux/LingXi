<!-- 23软2张奎良-202305566305 -->
<template>
  <ContentWrap>
    <el-form
      class="-mb-15px"
      :model="queryParams"
      ref="queryFormRef"
      label-width="auto"
    >
      <el-row :gutter="20" justify="center">
        <el-col :span="6">
          <el-form-item :label="t('report.workReport.type')" prop="type">
            <el-select
              v-model="queryParams.type"
              :placeholder="t('report.workReport.typePlaceholder')"
              clearable
              class="!w-240px"
            >
              <el-option
                v-for="item in typeOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item :label="t('report.workReport.title')" prop="title">
            <el-input
              v-model="queryParams.title"
              :placeholder="t('report.workReport.titlePlaceholder')"
              clearable
              @keyup.enter="handleQuery"
              class="!w-240px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item :label="t('report.workReport.status')" prop="status">
            <el-select
              v-model="queryParams.status"
              :placeholder="t('report.workReport.statusPlaceholder')"
              clearable
              class="!w-240px"
            >
              <el-option
                v-for="item in statusOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item :label="t('common.createTime')" prop="dateRange">
            <el-date-picker
              v-model="queryParams.dateRange"
              value-format="YYYY-MM-DD"
              type="daterange"
              :start-placeholder="t('common.startTimeText')"
              :end-placeholder="t('common.endTimeText')"
              class="!w-240px"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row justify="center">
        <el-col :span="24">
          <el-form-item class="flex justify-center">
            <el-button @click="handleQuery"><Icon icon="ep:search" />{{ t('common.query') }}</el-button>
            <el-button @click="resetQuery"><Icon icon="ep:refresh" />{{ t('common.reset') }}</el-button>
            <el-button
              type="primary"
              plain
              @click="openForm('create')"
              v-hasPermi="['report:work-report:create']"
            >
              <Icon icon="ep:plus" /> {{ t('action.add') }}
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :table-layout="'auto'" @selection-change="handleRowCheckboxChange">
      <el-table-column type="selection" width="55" />
      <el-table-column :label="t('common.id')" align="center" prop="id" width="80" />
      <el-table-column
        :label="t('report.workReport.type')"
        align="center"
        prop="type"
        width="100"
      >
        <template #default="scope">
          <el-tag :type="getTypeTagType(scope.row.type)">
            {{ getTypeLabel(scope.row.type) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('report.workReport.title')"
        align="center"
        prop="title"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        :label="t('report.workReport.reportDate')"
        align="center"
        prop="reportDate"
        width="120"
      />
      <el-table-column
        :label="t('report.workReport.endDate')"
        align="center"
        prop="endDate"
        width="120"
      />
      <el-table-column
        :label="t('report.workReport.reporterName')"
        align="center"
        prop="reporterName"
        width="120"
      />
      <el-table-column
        :label="t('report.workReport.deptName')"
        align="center"
        prop="deptName"
        width="120"
      />
      <el-table-column
        :label="t('report.workReport.status')"
        align="center"
        prop="status"
        width="100"
      >
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'warning'">
            {{ getStatusLabel(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        :label="t('common.createTime')"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        min-width="180"
      />
      <el-table-column :label="t('common.operation')" align="center" min-width="280">
        <template #default="scope">
          <div class="flex items-center justify-center">
            <el-button
              type="primary"
              link
              @click="openForm('update', scope.row.id)"
              v-hasPermi="['report:work-report:update']"
              v-if="scope.row.status === WorkReportStatus.DRAFT"
            >
              <Icon icon="ep:edit" />{{ t('action.edit') }}
            </el-button>
            <el-button
              type="success"
              link
              @click="handleSubmit(scope.row.id)"
              v-hasPermi="['report:work-report:submit']"
              v-if="scope.row.status === WorkReportStatus.DRAFT"
            >
              <Icon icon="ep:send" />{{ t('report.workReport.submit') }}
            </el-button>
            <el-button
              type="primary"
              link
              @click="handleApprove(scope.row, true)"
              v-hasPermi="['report:work-report:approve']"
              v-if="scope.row.status === WorkReportStatus.SUBMITTED"
            >
              <Icon icon="ep:check" />{{ t('report.workReport.approve') }}
            </el-button>
            <el-button
              type="danger"
              link
              @click="handleApprove(scope.row, false)"
              v-hasPermi="['report:work-report:approve']"
              v-if="scope.row.status === WorkReportStatus.SUBMITTED"
            >
              <Icon icon="ep:close" />{{ t('report.workReport.reject') }}
            </el-button>
            <el-button
              type="danger"
              link
              @click="handleDelete(scope.row.id)"
              v-hasPermi="['report:work-report:delete']"
            >
              <Icon icon="ep:delete" />{{ t('action.del') }}
            </el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>

  <WorkReportForm ref="formRef" @success="getList" />
</template>
<script lang="ts" setup>
// 23软2张奎良-202305566305
import { dateFormatter } from '@/utils/formatTime'
import { WorkReportType, WorkReportStatus } from '@/api/report/workreport'
import * as WorkReportApi from '@/api/report/workreport'
import WorkReportForm from './WorkReportForm.vue'
import { ElMessageBox } from 'element-plus'

/** 组件名称 */
defineOptions({ name: 'WorkReport' })

/** 消息弹窗 */
const message = useMessage()
/** 国际化 */
const { t } = useI18n()

/** 列表加载状态 */
const loading = ref(true)
/** 列表总数 */
const total = ref(0)
/** 列表数据 */
const list = ref([])
/** 查询参数 */
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  type: undefined,
  title: undefined,
  status: undefined,
  dateRange: []
})
/** 查询表单引用 */
const queryFormRef = ref()

/** 报表类型选项 */
const typeOptions = [
  { label: t('report.workReport.typeDaily'), value: WorkReportType.DAILY },
  { label: t('report.workReport.typeWeekly'), value: WorkReportType.WEEKLY },
  { label: t('report.workReport.typeMonthly'), value: WorkReportType.MONTHLY }
]

/** 报表状态选项 */
const statusOptions = [
  { label: t('report.workReport.statusDraft'), value: WorkReportStatus.DRAFT },
  { label: t('report.workReport.statusSubmitted'), value: WorkReportStatus.SUBMITTED },
  { label: t('report.workReport.statusApproved'), value: WorkReportStatus.APPROVED },
  { label: t('report.workReport.statusRejected'), value: WorkReportStatus.REJECTED }
]

/**
 * 获取报表类型标签样式
 * @param type 类型值
 * @returns 标签样式
 */
const getTypeTagType = (type: number) => {
  switch (type) {
    case WorkReportType.DAILY:
      return 'primary'
    case WorkReportType.WEEKLY:
      return 'success'
    case WorkReportType.MONTHLY:
      return 'warning'
    default:
      return 'info'
  }
}

/**
 * 获取报表类型标签文字
 * @param type 类型值
 * @returns 类型名称
 */
const getTypeLabel = (type: number) => {
  switch (type) {
    case WorkReportType.DAILY:
      return t('report.workReport.typeDaily')
    case WorkReportType.WEEKLY:
      return t('report.workReport.typeWeekly')
    case WorkReportType.MONTHLY:
      return t('report.workReport.typeMonthly')
    default:
      return '-'
  }
}

/**
 * 获取报表状态标签文字
 * @param status 状态值
 * @returns 状态名称
 */
const getStatusLabel = (status: number) => {
  switch (status) {
    case WorkReportStatus.DRAFT:
      return t('report.workReport.statusDraft')
    case WorkReportStatus.SUBMITTED:
      return t('report.workReport.statusSubmitted')
    case WorkReportStatus.APPROVED:
      return t('report.workReport.statusApproved')
    case WorkReportStatus.REJECTED:
      return t('report.workReport.statusRejected')
    default:
      return '-'
  }
}

/**
 * 查询列表数据
 */
const getList = async () => {
  loading.value = true
  try {
    const data = await WorkReportApi.getWorkReportPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/**
 * 搜索按钮操作
 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/**
 * 重置按钮操作
 */
const resetQuery = () => {
  queryFormRef.value?.resetFields()
  handleQuery()
}

/**
 * 打开表单弹窗
 * @param type 操作类型（create/update）
 * @param id 报表ID（更新时传入）
 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

/**
 * 删除操作
 * @param id 报表ID
 */
const handleDelete = async (id: number) => {
  try {
    await message.delConfirm()
    await WorkReportApi.deleteWorkReport(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}

/**
 * 提交工作报表
 * @param id 报表ID
 */
const handleSubmit = async (id: number) => {
  try {
    await message.confirm(t('report.workReport.submitConfirm'))
    await WorkReportApi.submitWorkReport(id)
    message.success(t('report.workReport.submitSuccess'))
    await getList()
  } catch {}
}

/**
 * 审批工作报表
 * @param row 报表数据
 * @param isApprove 是否通过
 */
const handleApprove = async (row: any, isApprove: boolean) => {
  try {
    const title = isApprove ? t('report.workReport.approve') : t('report.workReport.reject')
    const confirmMsg = isApprove ? t('report.workReport.approveConfirm') : t('report.workReport.rejectConfirm')
    const successMsg = isApprove ? t('report.workReport.approveSuccess') : t('report.workReport.rejectSuccess')
    
    const { value: comment } = await ElMessageBox.prompt(
      confirmMsg,
      title,
      {
        inputType: 'textarea',
        inputPlaceholder: t('report.workReport.approveCommentPlaceholder'),
        confirmButtonText: t('common.confirm'),
        cancelButtonText: t('common.cancel')
      }
    )
    
    await WorkReportApi.approveWorkReport({
      id: row.id,
      status: isApprove ? WorkReportStatus.APPROVED : WorkReportStatus.REJECTED,
      approveComment: comment
    })
    message.success(successMsg)
    await getList()
  } catch {}
}

/**
 * 批量删除操作
 */
const checkedIds = ref<number[]>([])
const handleRowCheckboxChange = (rows: any[]) => {
  checkedIds.value = rows.map((row) => row.id)
}

/**
 * 组件挂载时初始化列表
 */
onMounted(() => {
  getList()
})
</script>