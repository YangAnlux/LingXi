// 23软2张奎良-202305566305
package com.meession.etm.module.report.controller.admin.workreport.vo;

import com.meession.etm.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

/**
 * 工作报表分页查询请求 VO
 * 
 * 用于分页查询工作报表的请求参数封装。
 * 
 * @author 23软2张奎良
 */
@Data
@Schema(description = "工作报表分页查询请求")
public class WorkReportPageReqVO extends PageParam {

    /**
     * 报表类型
     */
    @Schema(description = "报表类型（1-日报，2-周报，3-月报）", example = "1")
    private Integer type;

    /**
     * 报表标题
     */
    @Schema(description = "报表标题", example = "工作日报")
    private String title;

    /**
     * 报告人ID
     */
    @Schema(description = "报告人ID", example = "1")
    private Long reporterId;

    /**
     * 部门ID
     */
    @Schema(description = "部门ID", example = "1")
    private Long deptId;

    /**
     * 状态
     */
    @Schema(description = "状态（0-草稿，1-已提交）", example = "1")
    private Integer status;

    /**
     * 日期范围（开始日期到结束日期）
     */
    @Schema(description = "日期范围")
    private LocalDate[] dateRange;

}