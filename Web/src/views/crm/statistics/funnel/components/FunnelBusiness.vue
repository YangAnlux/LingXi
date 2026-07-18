<!-- 销售漏斗分�?-->
<template>
  <!-- Echarts�?-->
  <el-card shadow="never">
    <el-row>
      <el-col :span="24">
        <el-button-group class="mb-10px">
          <el-button type="primary" @click="handleActive(true)">{{ t('funnel.customerView') }}</el-button>
          <el-button type="primary" @click="handleActive(false)">{{ t('funnel.dynamicView') }}</el-button>
        </el-button-group>
        <el-skeleton :loading="loading" animated>
          <Echart :height="500" :options="echartsOption" />
        </el-skeleton>
      </el-col>
    </el-row>
  </el-card>

  <!-- 统计列表 -->
  <el-card class="mt-16px" shadow="never">
    <el-table v-loading="loading" :data="list" :table-layout="'auto'"> 
       <el-table-column align="center" :label="t('customer.index')" type="index" width="80" /> 
       <el-table-column align="center" :label="t('funnel.stage')" prop="statusName" min-width="200" /> 
       <el-table-column align="center" :label="t('funnel.percent')" prop="percent" min-width="100">
         <template #default="scope">
           {{ scope.row.percent ? (scope.row.percent * 100).toFixed(0) + '%' : '-' }}
         </template>
       </el-table-column>
       <el-table-column align="center" :label="t('funnel.businessCount')" min-width="150" prop="businessCount" /> 
       <el-table-column align="center" :label="t('funnel.businessTotalPrice')" min-width="150" prop="totalPrice" /> 
     </el-table>
  </el-card>
</template>
<script lang="ts" setup>
import { CrmStatisticFunnelRespVO, CrmStatisticsBusinessSummaryByStatusRespVO, StatisticFunnelApi } from '@/api/crm/statistics/funnel'
import { EChartsOption } from 'echarts'
import { DICT_TYPE } from '@/utils/dict'
import { Echart } from '@/components/Echart'
import { useI18n } from 'vue-i18n'

defineOptions({ name: 'FunnelBusiness' })

const { t } = useI18n('crm.statistics') // 国际�?

const props = defineProps<{ queryParams: any }>() // 搜索参数 
 
 const active = ref(true) 
 const loading = ref(false) // 加载�? 
 const list = ref<CrmStatisticsBusinessSummaryByStatusRespVO[]>([]) // 列表的数�?

/** 销售漏�?*/
const echartsOption = reactive<EChartsOption>({
  title: {
    text: t('funnel.funnel')
  },
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b}'
  },
  toolbox: {
    feature: {
      dataView: { readOnly: false },
      restore: {},
      saveAsImage: {}
    }
  },
  legend: {
    data: [t('funnel.customer'), t('funnel.business'), t('funnel.win')]
  },
  series: [
    {
      name: t('funnel.funnel'),
      type: 'funnel',
      left: '10%',
      top: 60,
      bottom: 60,
      width: '80%',
      min: 0,
      max: 100,
      minSize: '0%',
      maxSize: '100%',
      sort: 'descending',
      gap: 2,
      label: {
        show: true,
        position: 'inside'
      },
      labelLine: {
        length: 10,
        lineStyle: {
          width: 1,
          type: 'solid'
        }
      },
      itemStyle: {
        borderColor: '#fff',
        borderWidth: 1
      },
      emphasis: {
        label: {
          fontSize: 20
        }
      },
      data: [
        { value: 60, name: `${t('funnel.customer')}-0${t('funnel.unit')}` },
        { value: 40, name: `${t('funnel.business')}-0${t('funnel.unit')}` },
        { value: 20, name: `${t('funnel.win')}-0${t('funnel.unit')}` }
      ]
    }
  ]
}) as EChartsOption

const handleActive = async (val: boolean) => { 
   active.value = val 
   await loadData() 
 } 
 
 /** 获取统计数据 */ 
 const loadData = async () => { 
   loading.value = true 
   // 1. 加载按阶段统计数据 
   const result = await StatisticFunnelApi.getBusinessSummaryByStatus(props.queryParams)
   const statusList = result.data as CrmStatisticsBusinessSummaryByStatusRespVO[] 
   // 2.1 更新 Echarts 数据 
   if ( 
     !!statusList && 
     statusList.length > 0 && 
     echartsOption.series && 
     echartsOption.series[0] && 
     echartsOption.series[0]['data'] 
   ) { 
     const dataList: { value: number; name: string }[] = [] 
     statusList.forEach((item, index) => { 
       const percent = item.percent || ((statusList.length - index) * 20) 
       const value = active.value ? percent : (item.businessCount || 0) 
       dataList.push({ 
         value: value, 
         name: `${item.statusName}-${item.businessCount || 0}${t('funnel.unit')}` 
       }) 
     }) 
     echartsOption.series[0]['data'] = dataList 
   } 
   // 2.2 设置列表数据 
   list.value = statusList 
   loading.value = false 
 }
defineExpose({ loadData })

/** 初始�?*/
onMounted(() => {
  loadData()
})
</script>