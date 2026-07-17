package com.meession.etm.module.report.enums;

import com.meession.etm.framework.common.exception.ErrorCode;

/**
 * Report 错误码枚举类
 *
 * report 系统，使用 1-003-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== GoView 模块 1-003-000-000 ==========
    ErrorCode GO_VIEW_PROJECT_NOT_EXISTS = new ErrorCode(1_003_000_000, "GoView 项目不存在");

    // ========== 工作报表模块 1-003-001-000 ==========
    ErrorCode WORK_REPORT_NOT_EXISTS = new ErrorCode(1_003_001_000, "工作报表不存在");
    ErrorCode WORK_REPORT_NOT_DRAFT = new ErrorCode(1_003_001_001, "工作报表不是草稿状态，无法提交");
    ErrorCode WORK_REPORT_NOT_SUBMITTED = new ErrorCode(1_003_001_002, "工作报表不是已提交状态，无法审批");

    // ========== 任务管理模块 1-003-002-000 ==========
    ErrorCode TASK_NOT_EXISTS = new ErrorCode(1_003_002_000, "任务不存在");
    ErrorCode TASK_STATUS_ERROR = new ErrorCode(1_003_002_001, "任务状态错误，无法执行此操作");

}
