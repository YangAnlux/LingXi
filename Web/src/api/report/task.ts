// 23软2张奎良-202305566305
import request from '@/config/axios'

/**
 * 任务管理 API 接口
 * 
 * 提供任务管理的前端 API 调用方法，包括创建、更新、删除、查询等操作。
 * 
 * @author 23软2张奎良
 */

/**
 * 任务类型枚举
 */
export enum TaskType {
  DAILY = 1,
  EMERGENCY = 2,
  PROJECT = 3,
  TEMPORARY = 4
}

/**
 * 任务优先级枚举
 */
export enum TaskPriority {
  LOW = 1,
  MEDIUM = 2,
  HIGH = 3,
  URGENT = 4
}

/**
 * 任务状态枚举
 */
export enum TaskStatus {
  PENDING = 0,
  IN_PROGRESS = 1,
  COMPLETED = 2,
  CANCELLED = 3
}

/**
 * 任务分页查询请求参数
 */
export interface TaskPageReqVO {
  pageNo?: number
  pageSize?: number
  title?: string
  type?: number
  status?: number
  priority?: number
  assigneeId?: number
  deptId?: number
  dateRange?: [string, string]
}

/**
 * 任务保存请求参数
 */
export interface TaskSaveReqVO {
  id?: number
  title: string
  description?: string
  type: number
  priority: number
  status?: number
  assigneeId?: number
  assigneeName?: string
  creatorId?: number
  creatorName?: string
  deptId?: number
  deptName?: string
  startDate: string
  endDate: string
  progress?: number
  remark?: string
}

/**
 * 任务响应数据
 */
export interface TaskRespVO {
  id: number
  title: string
  description?: string
  type: number
  priority: number
  status: number
  assigneeId?: number
  assigneeName?: string
  creatorId?: number
  creatorName?: string
  deptId?: number
  deptName?: string
  startDate: string
  endDate: string
  completeDate?: string
  progress?: number
  remark?: string
  createTime: string
  updateTime?: string
}

/**
 * 创建任务
 * 
 * @param data 创建请求参数
 * @returns 任务ID
 */
export const createTask = (data: TaskSaveReqVO) => {
  return request.post({ url: '/report/task/create', data })
}

/**
 * 更新任务
 * 
 * @param data 更新请求参数
 * @returns 更新结果
 */
export const updateTask = (data: TaskSaveReqVO) => {
  return request.put({ url: '/report/task/update', data })
}

/**
 * 删除任务
 * 
 * @param id 任务ID
 * @returns 删除结果
 */
export const deleteTask = (id: number) => {
  return request.delete({ url: '/report/task/delete', params: { id } })
}

/**
 * 获取任务详情
 * 
 * @param id 任务ID
 * @returns 任务详情
 */
export const getTask = (id: number) => {
  return request.get({ url: '/report/task/get', params: { id } })
}

/**
 * 获取任务分页列表
 * 
 * @param params 分页查询条件
 * @returns 分页结果
 */
export const getTaskPage = (params: TaskPageReqVO) => {
  return request.get({ url: '/report/task/page', params })
}

/**
 * 分配任务
 * 
 * @param id 任务ID
 * @param assigneeId 负责人ID
 * @param assigneeName 负责人姓名
 * @returns 分配结果
 */
export const assignTask = (id: number, assigneeId: number, assigneeName: string) => {
  return request.put({ url: '/report/task/assign', params: { id, assigneeId, assigneeName } })
}

/**
 * 开始任务
 * 
 * @param id 任务ID
 * @returns 开始结果
 */
export const startTask = (id: number) => {
  return request.put({ url: '/report/task/start', params: { id } })
}

/**
 * 完成任务
 * 
 * @param id 任务ID
 * @returns 完成结果
 */
export const completeTask = (id: number) => {
  return request.put({ url: '/report/task/complete', params: { id } })
}

/**
 * 取消任务
 * 
 * @param id 任务ID
 * @returns 取消结果
 */
export const cancelTask = (id: number) => {
  return request.put({ url: '/report/task/cancel', params: { id } })
}