// 23软2张奎良-202305566305
import request from '@/config/axios'

/**
 * 工作报表 API 接口
 * 
 * 提供工作报表的前端 API 调用方法，包括创建、更新、删除、查询等操作。
 * 
 * @author 23软2张奎良
 */

/**
 * 工作报表类型枚举
 */
export enum WorkReportType {
  DAILY = 1,
  WEEKLY = 2,
  MONTHLY = 3
}

/**
 * 工作报表状态枚举
 */
export enum WorkReportStatus {
  DRAFT = 0,
  SUBMITTED = 1,
  APPROVED = 2,
  REJECTED = 3
}

/**
 * 工作报表分页查询请求参数
 */
export interface WorkReportPageReqVO {
  type?: number
  title?: string
  reporterId?: number
  deptId?: number
  status?: number
  dateRange?: [string, string]
}

/**
 * 工作报表保存请求参数
 */
export interface WorkReportSaveReqVO {
  id?: number
  type: number
  title: string
  reportDate: string
  endDate?: string
  content?: string
  achievements?: string
  problems?: string
  plan?: string
  reporterId?: number
  reporterName?: string
  deptId?: number
  deptName?: string
  status?: number
}

/**
 * 工作报表响应数据
 */
export interface WorkReportRespVO {
  id: number
  type: number
  title: string
  reportDate: string
  endDate?: string
  content?: string
  achievements?: string
  problems?: string
  plan?: string
  reporterId?: number
  reporterName?: string
  deptId?: number
  deptName?: string
  status: number
  approverId?: number
  approverName?: string
  approveTime?: string
  approveComment?: string
  createTime: string
}

/**
 * 工作报表审批请求参数
 */
export interface WorkReportApproveReqVO {
  id: number
  status: number
  approveComment?: string
}

/**
 * 创建工作报表
 * 
 * @param data 创建请求参数
 * @returns 报表ID
 */
export const createWorkReport = (data: WorkReportSaveReqVO) => {
  return request.post({ url: '/report/work-report/create', data })
}

/**
 * 更新工作报表
 * 
 * @param data 更新请求参数
 * @returns 更新结果
 */
export const updateWorkReport = (data: WorkReportSaveReqVO) => {
  return request.put({ url: '/report/work-report/update', data })
}

/**
 * 删除工作报表
 * 
 * @param id 报表ID
 * @returns 删除结果
 */
export const deleteWorkReport = (id: number) => {
  return request.delete({ url: '/report/work-report/delete', params: { id } })
}

/**
 * 获取工作报表详情
 * 
 * @param id 报表ID
 * @returns 报表详情
 */
export const getWorkReport = (id: number) => {
  return request.get({ url: '/report/work-report/get', params: { id } })
}

/**
 * 获取工作报表分页列表
 * 
 * @param params 分页查询条件
 * @returns 分页结果
 */
export const getWorkReportPage = (params: WorkReportPageReqVO) => {
  return request.get({ url: '/report/work-report/page', params })
}

/**
 * 提交工作报表
 * 
 * @param id 报表ID
 * @returns 提交结果
 */
export const submitWorkReport = (id: number) => {
  return request.put({ url: '/report/work-report/submit', params: { id } })
}

/**
 * 审批工作报表
 * 
 * @param data 审批请求参数
 * @returns 审批结果
 */
export const approveWorkReport = (data: WorkReportApproveReqVO) => {
  return request.put({ url: '/report/work-report/approve', data })
}