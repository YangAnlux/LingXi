import request from '@/config/axios'

export type BpmProcessCommentVO = {
  id: number
  processInstanceId: string
  taskId?: string
  userId: number
  content: string
  createTime: string
  user?: {
    id: number
    nickname: string
    avatar?: string
  }
}

export const createProcessComment = async (data: {
  processInstanceId: string
  taskId?: string
  content: string
}) => {
  return await request.post({ url: '/bpm/process-comment/create', data: data })
}

export const getProcessCommentList = async (processInstanceId: string) => {
  return await request.get({ url: '/bpm/process-comment/list', params: { processInstanceId } })
}

export const deleteProcessComment = async (id: number) => {
  return await request.delete({ url: '/bpm/process-comment/delete?id=' + id })
}