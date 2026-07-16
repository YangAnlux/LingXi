<!-- 23软2张奎良-202305566305 -->
<template>
  <el-dialog
    v-model="visible"
    :title="type === 'create' ? t('action.add') : t('action.edit')"
    width="600px"
    destroy-on-close
  >
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
      <el-form-item :label="t('report.workReport.type')" prop="type">
        <el-select
          v-model="form.type"
          :placeholder="t('report.workReport.typePlaceholder')"
          class="!w-full"
        >
          <el-option
            v-for="item in typeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('report.workReport.title')" prop="title">
        <el-input
          v-model="form.title"
          :placeholder="t('report.workReport.titlePlaceholder')"
          class="!w-full"
        />
      </el-form-item>
      <el-form-item :label="t('report.workReport.reportDate')" prop="reportDate">
        <el-date-picker
          v-model="form.reportDate"
          value-format="YYYY-MM-DD"
          type="date"
          :placeholder="t('report.workReport.reportDatePlaceholder')"
          class="!w-full"
        />
      </el-form-item>
      <el-form-item :label="t('report.workReport.endDate')" prop="endDate" v-if="form.type !== 1">
        <el-date-picker
          v-model="form.endDate"
          value-format="YYYY-MM-DD"
          type="date"
          :placeholder="t('report.workReport.endDatePlaceholder')"
          class="!w-full"
        />
      </el-form-item>
      <el-form-item :label="t('report.workReport.content')" prop="content">
        <el-input
          v-model="form.content"
          :placeholder="t('report.workReport.contentPlaceholder')"
          type="textarea"
          :rows="4"
          class="!w-full"
        />
      </el-form-item>
      <el-form-item :label="t('report.workReport.achievements')" prop="achievements">
        <el-input
          v-model="form.achievements"
          :placeholder="t('report.workReport.achievementsPlaceholder')"
          type="textarea"
          :rows="4"
          class="!w-full"
        />
      </el-form-item>
      <el-form-item :label="t('report.workReport.problems')" prop="problems">
        <el-input
          v-model="form.problems"
          :placeholder="t('report.workReport.problemsPlaceholder')"
          type="textarea"
          :rows="4"
          class="!w-full"
        />
      </el-form-item>
      <el-form-item :label="t('report.workReport.plan')" prop="plan">
        <el-input
          v-model="form.plan"
          :placeholder="t('report.workReport.planPlaceholder')"
          type="textarea"
          :rows="4"
          class="!w-full"
        />
      </el-form-item>
      <el-form-item :label="t('report.workReport.reporterName')" prop="reporterName">
        <el-input
          v-model="form.reporterName"
          :placeholder="t('report.workReport.reporterNamePlaceholder')"
          class="!w-full"
        />
      </el-form-item>
      <el-form-item :label="t('report.workReport.deptName')" prop="deptName">
        <el-input
          v-model="form.deptName"
          :placeholder="t('report.workReport.deptNamePlaceholder')"
          class="!w-full"
        />
      </el-form-item>
      <el-form-item :label="t('report.workReport.status')" prop="status">
        <el-select
          v-model="form.status"
          :placeholder="t('report.workReport.statusPlaceholder')"
          class="!w-full"
        >
          <el-option
            v-for="item in statusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="visible = false">{{ t('common.cancel') }}</el-button>
      <el-button type="primary" @click="submitForm" :loading="loading">
        {{ t('common.confirm') }}
      </el-button>
    </template>
  </el-dialog>
</template>
<script lang="ts" setup>
// 23软2张奎良-202305566305
import { ref, reactive, watch } from 'vue'
import { WorkReportType, WorkReportStatus } from '@/api/report/workreport'
import * as WorkReportApi from '@/api/report/workreport'

/** 组件名称 */
defineOptions({ name: 'WorkReportForm' })

/** 消息弹窗 */
const message = useMessage()
/** 国际化 */
const { t } = useI18n()

/** 弹窗可见性 */
const visible = ref(false)
/** 操作类型（create/update） */
const type = ref('create')
/** 表单加载状态 */
const loading = ref(false)
/** 表单引用 */
const formRef = ref()

/** 表单数据 */
const form = reactive<WorkReportApi.WorkReportSaveReqVO>({
  id: undefined,
  type: WorkReportType.DAILY,
  title: '',
  reportDate: '',
  endDate: '',
  content: '',
  achievements: '',
  problems: '',
  plan: '',
  reporterId: undefined,
  reporterName: '',
  deptId: undefined,
  deptName: '',
  status: WorkReportStatus.DRAFT
})

/** 表单验证规则 */
const rules = {
  type: [{ required: true, message: t('report.workReport.typeRequired'), trigger: 'change' }],
  title: [{ required: true, message: t('report.workReport.titleRequired'), trigger: 'blur' }],
  reportDate: [{ required: true, message: t('report.workReport.reportDateRequired'), trigger: 'change' }]
}

/** 报表类型选项 */
const typeOptions = [
  { label: t('report.workReport.typeDaily'), value: WorkReportType.DAILY },
  { label: t('report.workReport.typeWeekly'), value: WorkReportType.WEEKLY },
  { label: t('report.workReport.typeMonthly'), value: WorkReportType.MONTHLY }
]

/** 报表状态选项 */
const statusOptions = [
  { label: t('report.workReport.statusDraft'), value: WorkReportStatus.DRAFT },
  { label: t('report.workReport.statusSubmitted'), value: WorkReportStatus.SUBMITTED }
]

/**
 * 打开表单弹窗
 * @param formType 操作类型
 * @param id 报表ID（更新时传入）
 */
const open = async (formType: string, id?: number) => {
  type.value = formType
  // 重置表单数据
  Object.assign(form, {
    id: undefined,
    type: WorkReportType.DAILY,
    title: '',
    reportDate: '',
    endDate: '',
    content: '',
    achievements: '',
    problems: '',
    plan: '',
    reporterId: undefined,
    reporterName: '',
    deptId: undefined,
    deptName: '',
    status: WorkReportStatus.DRAFT
  })
  // 重置表单验证
  formRef.value?.resetFields()
  // 如果是更新操作，获取报表详情
  if (id) {
    const data = await WorkReportApi.getWorkReport(id)
    Object.assign(form, data)
    // 处理日期字段：后端返回数组格式 [year, month, day]，转换为 YYYY-MM-DD 字符串
    if (Array.isArray(form.reportDate)) {
      const [year, month, day] = form.reportDate
      form.reportDate = `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`
    }
    if (Array.isArray(form.endDate)) {
      const [year, month, day] = form.endDate
      form.endDate = `${year}-${String(month).padStart(2, '0')}-${String(day).padStart(2, '0')}`
    }
  }
  // 显示弹窗
  visible.value = true
}

/**
 * 提交表单
 */
const submitForm = async () => {
  // 表单验证
  await formRef.value?.validate()
  loading.value = true
  try {
    if (type.value === 'create') {
      // 创建报表
      await WorkReportApi.createWorkReport(form)
      message.success(t('common.addSuccess'))
    } else {
      // 更新报表
      await WorkReportApi.updateWorkReport(form)
      message.success(t('common.updateSuccess'))
    }
    // 关闭弹窗并触发成功回调
    visible.value = false
    emit('success')
  } finally {
    loading.value = false
  }
}

/**
 * 监听报表类型变化，日报不需要结束日期
 */
watch(() => form.type, (newType) => {
  if (newType === WorkReportType.DAILY) {
    form.endDate = ''
  }
})

/** 成功回调事件 */
const emit = defineEmits(['success'])

/** 暴露方法给父组件 */
defineExpose({
  open
})
</script>