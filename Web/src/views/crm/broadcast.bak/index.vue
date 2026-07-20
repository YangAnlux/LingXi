<template>
  <div class="broadcast-container">
    <h2>📢 群发管理</h2>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stat-cards">
      <el-col :span="6">
        <el-card>
          <div class="stat-item">
            <div class="stat-label">总群发</div>
            <div class="stat-value">{{ stats.total }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="stat-item">
            <div class="stat-label">待发送</div>
            <div class="stat-value">{{ stats.pending }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="stat-item">
            <div class="stat-label">发送中</div>
            <div class="stat-value">{{ stats.sending }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card>
          <div class="stat-item">
            <div class="stat-label">已完成</div>
            <div class="stat-value">{{ stats.done }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索和新增 -->
    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">➕ 新建群发</el-button>
      <el-input
        v-model="searchKey"
        placeholder="搜索标题"
        style="width: 200px; margin-left: 10px;"
        @keyup.enter="loadData"
      />
      <el-button @click="loadData">搜索</el-button>
    </div>

    <!-- 表格 -->
    <el-table :data="tableData" border stripe style="width: 100%; margin-top: 10px;">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="no" label="编号" width="150" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="content" label="内容" show-overflow-tooltip />
      <el-table-column prop="customerCount" label="客户数" width="80" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="180" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="handleSend(row)">发送</el-button>
          <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      v-model:page-size="pageSize"
      v-model:current-page="pageNo"
      :total="total"
      :page-sizes="[10, 20, 50, 100]"
      layout="total, sizes, prev, pager, next"
      @size-change="loadData"
      @current-change="loadData"
      style="margin-top: 15px; justify-content: flex-end;"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/config/axios'

// 数据
const tableData = ref<any[]>([])
const total = ref(0)
const pageNo = ref(1)
const pageSize = ref(10)
const searchKey = ref('')

// 统计数据
const stats = reactive({
  total: 0,
  pending: 0,
  sending: 0,
  done: 0
})

// 加载数据
const loadData = () => {
  request.get('/crm/broadcast/page', {
    params: {
      pageNo: pageNo.value,
      pageSize: pageSize.value,
      title: searchKey.value
    }
  }).then((res: any) => {
    tableData.value = res.data?.list || []
    total.value = res.data?.total || 0
  }).catch(() => {
    ElMessage.error('加载数据失败')
  })
}

// 新建
const handleAdd = () => {
  ElMessage.info('新建群发功能开发中...')
}

// 发送
const handleSend = (row: any) => {
  ElMessageBox.confirm(`确定要发送 "${row.title}" 吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    request.put('/crm/broadcast/send', null, {
      params: { id: row.id }
    }).then(() => {
      ElMessage.success('发送成功')
      loadData()
    })
  }).catch(() => {})
}

// 删除
const handleDelete = (row: any) => {
  ElMessageBox.confirm(`确定要删除 "${row.title}" 吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    request.delete('/crm/broadcast/delete', {
      params: { id: row.id }
    }).then(() => {
      ElMessage.success('删除成功')
      loadData()
    })
  }).catch(() => {})
}

// 状态
const getStatusText = (status: number) => {
  const map: Record<number, string> = { 0: '待发送', 1: '发送中', 2: '已完成', 3: '失败' }
  return map[status] || '未知'
}

const getStatusType = (status: number) => {
  const map: Record<number, string> = { 0: 'warning', 1: 'primary', 2: 'success', 3: 'danger' }
  return map[status] || 'info'
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.broadcast-container {
  padding: 20px;
}
.stat-cards {
  margin-bottom: 20px;
}
.stat-item {
  text-align: center;
}
.stat-label {
  font-size: 14px;
  color: #909399;
}
.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-top: 5px;
}
.toolbar {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}
</style>
