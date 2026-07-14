// 23软2张奎良-202305566305
package com.meession.etm.module.report.controller.admin.workreport.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

/**
 * 工作报表保存请求 VO
 * 
 * 用于创建和更新工作报表的请求参数封装。
 * 
 * @author 23软2张奎良
 */
@Data
@Schema(description = "工作报表保存请求")
public class WorkReportSaveReqVO {

    /**
     * 编号（更新时必填）
     */
    @Schema(description = "编号", example = "1024")
    private Long id;

    /**
     * 报表类型
     * 
     * 必填，枚举值：1-日报，2-周报，3-月报
     */
    @NotNull(message = "报表类型不能为空")
    @Min(value = 1, message = "报表类型最小值为1")
    @Max(value = 3, message = "报表类型最大值为3")
    @Schema(description = "报表类型（1-日报，2-周报，3-月报）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer type;

    /**
     * 报表标题
     */
    @NotBlank(message = "报表标题不能为空")
    @Size(max = 200, message = "报表标题长度不能超过200个字符")
    @Schema(description = "报表标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "2026年7月14日工作日报")
    private String title;

    /**
     * 报表日期
     */
    @NotNull(message = "报表日期不能为空")
    @Schema(description = "报表日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate reportDate;

    /**
     * 周期结束日期
     */
    @Schema(description = "周期结束日期")
    private LocalDate endDate;

    /**
     * 工作内容
     */
    @Size(max = 5000, message = "工作内容长度不能超过5000个字符")
    @Schema(description = "工作内容")
    private String content;

    /**
     * 工作成果
     */
    @Size(max = 5000, message = "工作成果长度不能超过5000个字符")
    @Schema(description = "工作成果")
    private String achievements;

    /**
     * 问题与困难
     */
    @Size(max = 5000, message = "问题与困难长度不能超过5000个字符")
    @Schema(description = "问题与困难")
    private String problems;

    /**
     * 计划
     */
    @Size(max = 5000, message = "计划长度不能超过5000个字符")
    @Schema(description = "计划")
    private String plan;

    /**
     * 报告人ID
     */
    @Schema(description = "报告人ID", example = "1")
    private Long reporterId;

    /**
     * 报告人姓名
     */
    @Size(max = 50, message = "报告人姓名长度不能超过50个字符")
    @Schema(description = "报告人姓名", example = "张三")
    private String reporterName;

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
     * 状态
     */
    @Schema(description = "状态（0-草稿，1-已提交）", example = "1")
    private Integer status;

}