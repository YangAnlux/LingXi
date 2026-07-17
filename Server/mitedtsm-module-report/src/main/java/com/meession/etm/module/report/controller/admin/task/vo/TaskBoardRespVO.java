// 23软2张奎良-202305566305
package com.meession.etm.module.report.controller.admin.task.vo;

import com.meession.etm.module.report.controller.admin.task.vo.TaskRespVO;
import lombok.*;

import java.util.List;

/**
 * 任务看板响应 VO
 * 
 * 包含任务看板所需的所有数据：待办任务、进行中任务、已完成任务列表，
 * 以及即将到期和已逾期的任务统计信息。
 * 
 * @author 23软2张奎良
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskBoardRespVO {

    /**
     * 待办任务列表
     */
    private List<TaskRespVO> todoTasks;

    /**
     * 进行中任务列表
     */
    private List<TaskRespVO> inProgressTasks;

    /**
     * 已完成任务列表
     */
    private List<TaskRespVO> completedTasks;

    /**
     * 即将到期任务数量（3天内）
     */
    private Integer upcomingExpiredCount;

    /**
     * 已逾期任务数量
     */
    private Integer expiredCount;

}