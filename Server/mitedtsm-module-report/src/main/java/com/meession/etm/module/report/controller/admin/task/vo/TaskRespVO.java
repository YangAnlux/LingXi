// 23软2张奎良-202305566305
package com.meession.etm.module.report.controller.admin.task.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 任务管理响应 VO
 * 
 * 用于返回任务详情的响应数据封装。
 * 
 * @author 23软2张奎良
 */
@Data
@Schema(description = "任务管理响应")
public class TaskRespVO {

    /**
     * 编号
     */
    @Schema(description = "编号", example = "1024")
    private Long id;

    /**
     * 任务标题
     */
    @Schema(description = "任务标题", example = "完成报表开发")
    private String title;

    /**
     * 任务描述
     */
    @Schema(description = "任务描述")
    private String description;

    /**
     * 任务类型
     */
    @Schema(description = "任务类型（1-日常任务，2-紧急任务，3-项目任务，4-临时任务）", example = "1")
    private Integer type;

    /**
     * 优先级
     */
    @Schema(description = "优先级（1-低，2-中，3-高，4-紧急）", example = "2")
    private Integer priority;

    /**
     * 状态
     */
    @Schema(description = "状态（0-待分配，1-进行中，2-已完成，3-已取消）", example = "1")
    private Integer status;

    /**
     * 负责人ID
     */
    @Schema(description = "负责人ID", example = "1")
    private Long assigneeId;

    /**
     * 负责人姓名
     */
    @Schema(description = "负责人姓名", example = "张三")
    private String assigneeName;

    /**
     * 创建人ID
     */
    @Schema(description = "创建人ID", example = "1")
    private Long creatorId;

    /**
     * 创建人姓名
     */
    @Schema(description = "创建人姓名", example = "李四")
    private String creatorName;

    /**
     * 部门ID
     */
    @Schema(description = "部门ID", example = "1")
    private Long deptId;

    /**
     * 部门名称
     */
    @Schema(description = "部门名称", example = "技术部")
    private String deptName;

    /**
     * 开始日期
     */
    @Schema(description = "开始日期")
    private LocalDate startDate;

    /**
     * 截止日期
     */
    @Schema(description = "截止日期")
    private LocalDate endDate;

    /**
     * 完成日期
     */
    @Schema(description = "完成日期")
    private LocalDate completeDate;

    /**
     * 完成进度（0-100）
     */
    @Schema(description = "完成进度（0-100）", example = "50")
    private Integer progress;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}