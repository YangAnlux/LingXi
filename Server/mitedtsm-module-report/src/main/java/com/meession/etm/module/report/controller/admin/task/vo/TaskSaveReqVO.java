// 23软2张奎良-202305566305
package com.meession.etm.module.report.controller.admin.task.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

/**
 * 任务管理保存请求 VO
 * 
 * 用于创建和更新任务的请求参数封装。
 * 
 * @author 23软2张奎良
 */
@Data
@Schema(description = "任务管理保存请求")
public class TaskSaveReqVO {

    /**
     * 编号（更新时必填）
     */
    @Schema(description = "编号", example = "1024")
    private Long id;

    /**
     * 任务标题
     */
    @NotBlank(message = "任务标题不能为空")
    @Size(max = 200, message = "任务标题长度不能超过200个字符")
    @Schema(description = "任务标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "完成报表开发")
    private String title;

    /**
     * 任务描述
     */
    @Size(max = 5000, message = "任务描述长度不能超过5000个字符")
    @Schema(description = "任务描述")
    private String description;

    /**
     * 任务类型
     * 
     * 必填，枚举值：1-日常任务，2-紧急任务，3-项目任务，4-临时任务
     */
    @NotNull(message = "任务类型不能为空")
    @Min(value = 1, message = "任务类型最小值为1")
    @Max(value = 4, message = "任务类型最大值为4")
    @Schema(description = "任务类型（1-日常任务，2-紧急任务，3-项目任务，4-临时任务）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer type;

    /**
     * 优先级
     * 
     * 必填，枚举值：1-低，2-中，3-高，4-紧急
     */
    @NotNull(message = "优先级不能为空")
    @Min(value = 1, message = "优先级最小值为1")
    @Max(value = 4, message = "优先级最大值为4")
    @Schema(description = "优先级（1-低，2-中，3-高，4-紧急）", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer priority;

    /**
     * 状态
     */
    @Schema(description = "状态（0-待分配，1-进行中，2-已完成，3-已取消）", example = "0")
    private Integer status;

    /**
     * 负责人ID
     */
    @Schema(description = "负责人ID", example = "1")
    private Long assigneeId;

    /**
     * 负责人姓名
     */
    @Size(max = 50, message = "负责人姓名长度不能超过50个字符")
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
    @Size(max = 50, message = "创建人姓名长度不能超过50个字符")
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
    @Size(max = 100, message = "部门名称长度不能超过100个字符")
    @Schema(description = "部门名称", example = "技术部")
    private String deptName;

    /**
     * 开始日期
     */
    @NotNull(message = "开始日期不能为空")
    @Schema(description = "开始日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate startDate;

    /**
     * 截止日期
     */
    @NotNull(message = "截止日期不能为空")
    @Schema(description = "截止日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate endDate;

    /**
     * 完成进度（0-100）
     */
    @Min(value = 0, message = "完成进度最小值为0")
    @Max(value = 100, message = "完成进度最大值为100")
    @Schema(description = "完成进度（0-100）", example = "50")
    private Integer progress;

    /**
     * 备注
     */
    @Size(max = 1000, message = "备注长度不能超过1000个字符")
    @Schema(description = "备注")
    private String remark;

}