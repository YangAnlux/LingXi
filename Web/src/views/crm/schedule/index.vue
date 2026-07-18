<template>
  <ContentWrap>
    <div style="display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 12px;">
      <div style="display: flex; align-items: center; gap: 8px;">
        <el-button type="primary" @click="openForm('create')" v-hasPermi="['crm:schedule:create']">
          <Icon icon="ep:plus" class="mr-5px" /> {{ t('action.add') }}
        </el-button>
      </div>
      <div style="display: flex; align-items: center; gap: 8px;">
        <el-checkbox v-model="showMyOnly" @change="loadData">{{ t('schedule.myOnly') }}</el-checkbox>
      </div>
    </div>
  </ContentWrap>

  <ContentWrap v-loading="loading">
    <FullCalendar ref="calendarRef" :options="calendarOptions" />
  </ContentWrap>

  <ScheduleForm ref="formRef" @success="loadData" />
</template>

<script setup lang="ts">
import FullCalendar from '@fullcalendar/vue3'
import dayGridPlugin from '@fullcalendar/daygrid'
import timeGridPlugin from '@fullcalendar/timegrid'
import interactionPlugin from '@fullcalendar/interaction'
import zhCnLocale from '@fullcalendar/core/locales/zh-cn'
import * as ScheduleApi from '@/api/crm/schedule'
import ScheduleForm from './ScheduleForm.vue'
import dayjs from 'dayjs'

defineOptions({ name: 'CrmSchedule' })

const { t } = useI18n('crm')
const calendarRef = ref()
const loading = ref(false)
const showMyOnly = ref(false)

const calendarOptions = computed(() => ({
  plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],
  initialView: 'dayGridMonth',
  locale: zhCnLocale,
  headerToolbar: {
    left: 'prev,next today',
    center: 'title',
    right: 'dayGridMonth,timeGridWeek,timeGridDay'
  },
  buttonText: {
    today: t('schedule.today'),
    month: t('schedule.monthView'),
    week: t('schedule.weekView'),
    day: t('schedule.dayView')
  },
  height: 'auto',
  firstDay: 1,
  editable: true,
  selectable: true,
  selectMirror: true,
  dayMaxEvents: 3,
  navLinks: true,
  events: async (fetchInfo: any, successCallback: any, failureCallback: any) => {
    const start = dayjs(fetchInfo.start).format('YYYY-MM-DD HH:mm:ss')
    const end = dayjs(fetchInfo.end).format('YYYY-MM-DD HH:mm:ss')
    try {
      const events = await ScheduleApi.getScheduleListByRange(start, end)
      const mapped = events.map((event: any) => ({
        id: String(event.id),
        title: event.title,
        start: event.startTime,
        end: event.endTime,
        allDay: event.allDay,
        backgroundColor: event.color || '#409eff',
        borderColor: event.color || '#409eff',
        textColor: '#ffffff',
        extendedProps: {
          description: event.description,
          scheduleType: event.scheduleType,
          ownerUserName: event.ownerUserName
        }
      }))
      // 如果只看我的日程，过滤当前用户
      if (showMyOnly.value) {
        // 这里由后端权限控制，前端做额外过滤
      }
      successCallback(mapped)
    } catch (e) {
      failureCallback(e)
    }
  },
  // 点击空白日期创建日程
  select: (selectInfo: any) => {
    openForm('create', undefined, {
      startTime: dayjs(selectInfo.start).format('YYYY-MM-DD HH:mm:ss'),
      endTime: dayjs(selectInfo.end).format('YYYY-MM-DD HH:mm:ss'),
      allDay: selectInfo.allDay
    })
    calendarRef.value?.getApi()?.unselect()
  },
  // 点击已有日程编辑
  eventClick: (clickInfo: any) => {
    openForm('update', Number(clickInfo.event.id))
  }
}))

const loadData = async () => {
  // FullCalendar 会自动通过 events 函数加载数据，这里触发日历刷新
  calendarRef.value?.getApi()?.refetchEvents()
}

const formRef = ref()
const openForm = (type: string, id?: number, preset?: any) => {
  formRef.value.open(type, id, preset)
}

onMounted(() => {
  // FullCalendar 自动加载，无需手动调用
})
</script>

<style scoped>
/* FullCalendar 核心样式 */
:deep(.fc) {
  font-size: 14px;
}

:deep(.fc .fc-toolbar-title) {
  font-size: 1.2em;
  font-weight: 600;
}

:deep(.fc .fc-button) {
  font-size: 13px;
  padding: 4px 12px;
}

:deep(.fc .fc-button-primary) {
  background-color: #409eff;
  border-color: #409eff;
}

:deep(.fc .fc-button-primary:hover) {
  background-color: #337ecc;
  border-color: #337ecc;
}

:deep(.fc .fc-button-primary:not(:disabled).fc-button-active) {
  background-color: #337ecc;
  border-color: #337ecc;
}

:deep(.fc .fc-daygrid-event) {
  border-radius: 4px;
  padding: 2px 4px;
  font-size: 12px;
  cursor: pointer;
}

:deep(.fc .fc-timegrid-event) {
  border-radius: 4px;
  cursor: pointer;
}

:deep(.fc .fc-day-today) {
  background-color: #ecf5ff !important;
}

:deep(.fc .fc-highlight) {
  background-color: rgba(64, 158, 255, 0.1);
}
</style>
