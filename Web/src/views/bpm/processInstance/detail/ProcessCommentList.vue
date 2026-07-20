<template>
  <div class="process-comment-list">
    <div class="comment-header">
      <el-button type="primary" size="small" @click="loadComments">
        <Icon icon="ep:refresh" class="mr-5px" />
        {{ t('common.refresh') }}
      </el-button>
    </div>

    <el-timeline v-if="comments.length > 0" class="comment-timeline">
      <el-timeline-item
        v-for="comment in comments"
        :key="comment.id"
        :timestamp="formatDate(comment.createTime)"
        placement="top"
      >
        <div class="comment-card">
          <div class="comment-header-row">
            <el-avatar :size="36" v-if="comment.user?.avatar">
              <img :src="comment.user.avatar" />
            </el-avatar>
            <el-avatar :size="36" v-else-if="comment.user?.nickname">
              {{ comment.user.nickname.substring(0, 1) }}
            </el-avatar>
            <el-avatar :size="36" v-else>
              <Icon icon="ep:user" />
            </el-avatar>
            <div class="comment-info">
              <span class="comment-user">{{ comment.user?.nickname || t('common.unknown') }}</span>
              <span class="comment-time">{{ formatDate(comment.createTime) }}</span>
            </div>
            <el-button
              v-if="canDelete(comment)"
              size="small"
              type="danger"
              text
              @click="handleDelete(comment.id)"
            >
              {{ t('common.delete') }}
            </el-button>
          </div>
          <div class="comment-content">{{ comment.content }}</div>
        </div>
      </el-timeline-item>
    </el-timeline>

    <div v-else class="empty-comment">
      <el-empty description="暂无评论，快来发表第一条评论吧" />
    </div>

    <div class="comment-input-area">
      <el-input
        v-model="commentContent"
        type="textarea"
        :placeholder="t('process.instance.flowCommentPlaceholder')"
        :rows="3"
        maxlength="500"
        show-word-limit
        @keyup.enter.exact.prevent="handleSubmit"
      />
      <div class="comment-submit">
        <el-button type="primary" :disabled="!commentContent.trim()" @click="handleSubmit">
          {{ t('common.send') }}
        </el-button>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, watch } from 'vue'
import { formatDate } from '@/utils/formatTime'
import { useUserStore } from '@/store/modules/user'
import {
  createProcessComment,
  getProcessCommentList,
  deleteProcessComment,
  type BpmProcessCommentVO
} from '@/api/bpm/processComment'

defineOptions({ name: 'ProcessCommentList' })

const { t } = useI18n('bpm')
const message = useMessage()

const props = defineProps<{
  processInstanceId: string
}>()

const comments = ref<BpmProcessCommentVO[]>([])
const commentContent = ref('')
const loading = ref(false)

const loadComments = async () => {
  if (!props.processInstanceId) return
  loading.value = true
  try {
    const data = await getProcessCommentList(props.processInstanceId)
    comments.value = data
  } finally {
    loading.value = false
  }
}

const canDelete = (comment: BpmProcessCommentVO) => {
  const currentUserId = useUserStore().getUser.id
  return comment.userId === currentUserId
}

const handleDelete = async (id: number) => {
  await message.confirm(t('common.deleteConfirm'))
  try {
    await deleteProcessComment(id)
    message.success(t('common.deleteSuccess'))
    await loadComments()
  } catch {}
}

const handleSubmit = async () => {
  if (!commentContent.value.trim()) return
  if (!props.processInstanceId) {
    message.warning(t('process.instance.notExist'))
    return
  }
  try {
    await createProcessComment({
      processInstanceId: props.processInstanceId,
      content: commentContent.value.trim()
    })
    message.success(t('common.sendSuccess'))
    commentContent.value = ''
    await loadComments()
  } catch {
    message.error(t('common.sendFailed'))
  }
}

onMounted(() => {
  loadComments()
})

watch(() => props.processInstanceId, () => {
  loadComments()
})
</script>

<style lang="scss" scoped>
.process-comment-list {
  padding: 10px;
  height: 100%;
  display: flex;
  flex-direction: column;
}

.comment-header {
  margin-bottom: 16px;
}

.comment-timeline {
  flex: 1;
  overflow-y: auto;
}

.comment-card {
  background: #fafafa;
  border-radius: 8px;
  padding: 12px 16px;
  margin-bottom: 8px;
}

.comment-header-row {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.comment-info {
  display: flex;
  flex-direction: column;
  margin-left: 10px;
  flex: 1;
}

.comment-user {
  font-weight: 500;
  font-size: 14px;
}

.comment-time {
  font-size: 12px;
  color: #909399;
}

.comment-content {
  font-size: 14px;
  line-height: 1.6;
  color: #303133;
}

.empty-comment {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.comment-input-area {
  margin-top: 16px;
  padding-top: 16px;
  border-top: 1px solid #ebeef5;
}

.comment-submit {
  display: flex;
  justify-content: flex-end;
  margin-top: 10px;
}
</style>