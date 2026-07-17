// 23软2张奎良-202305566305
package com.meession.etm.module.report.controller.admin.task.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import com.meession.etm.framework.common.pojo.PageParam;
import java.time.LocalDate;

/**
 * 任务管理分页查询请求 VO
 * 
 * 用于查询任务列表的分页请求参数封装。
 * 
 * @author 23软2张奎良
 */
@Data
@Schema(description = "任务管理分页查询请求")
public class TaskPageReqVO extends PageParam {

    

    /**
     * 任务标题
     */
    @Schema(description = "任务标题", example = "完成报表开发")
    private String title;

    /**
     * 任务类型
     */
    @Schema(description = "任务类型（1-日常任务，2-紧急任务，3-项目任务，4-临时任务）", example = "1")
    private Integer type;

    /**
     * 状态
     */
    @Schema(description = "状态（0-待分配，1-进行中，2-已完成，3-已取消）", example = "1")
    private Integer status;

    /**
     * 优先级
     */
    @Schema(description = "优先级（1-低，2-中，3-高，4-紧急）", example = "2")
    private Integer priority;

    /**
     * 负责人ID
     */
    @Schema(description = "负责人ID", example = "1")
    private Long assigneeId;

    /**
     * 部门ID
     */
    @Schema(description = "部门ID", example = "1")
    private Long deptId;

    /**
     * 日期范围
     */
    @Schema(description = "日期范围")
    private LocalDate[] dateRange;

}