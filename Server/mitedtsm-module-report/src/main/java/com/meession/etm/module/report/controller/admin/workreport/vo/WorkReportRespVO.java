// 23软2张奎良-202305566305
package com.meession.etm.module.report.controller.admin.workreport.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 工作报表响应 VO
 * 
 * 用于返回工作报表详情的响应数据封装。
 * 
 * @author 23软2张奎良
 */
@Data
@Schema(description = "工作报表响应")
public class WorkReportRespVO {

    /**
     * 编号
     */
    @Schema(description = "编号", example = "1024")
    private Long id;

    /**
     * 报表类型
     */
    @Schema(description = "报表类型（1-日报，2-周报，3-月报）", example = "1")
    private Integer type;

    /**
     * 报表标题
     */
    @Schema(description = "报表标题", example = "2026年7月14日工作日报")
    private String title;

    /**
     * 报表日期
     */
    @Schema(description = "报表日期")
    private LocalDate reportDate;

    /**
     * 周期结束日期
     */
    @Schema(description = "周期结束日期")
    private LocalDate endDate;

    /**
     * 工作内容
     */
    @Schema(description = "工作内容")
    private String content;

    /**
     * 工作成果
     */
    @Schema(description = "工作成果")
    private String achievements;

    /**
     * 问题与困难
     */
    @Schema(description = "问题与困难")
    private String problems;

    /**
     * 计划
     */
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
    @Schema(description = "部门名称", example = "技术部")
    private String deptName;

    /**
     * 状态
     */
    @Schema(description = "状态（0-草稿，1-已提交）", example = "1")
    private Integer status;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}