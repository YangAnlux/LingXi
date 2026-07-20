// 23软2张奎良-202305566305
package com.meession.etm.module.report.controller.admin.workreport.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * 工作报表审批请求 VO
 * 
 * 用于审批工作报表的请求参数封装。
 * 
 * @author 23软2张奎良
 */
@Data
@Schema(description = "工作报表审批请求")
public class WorkReportApproveReqVO {

    /**
     * 编号
     */
    @NotNull(message = "报表ID不能为空")
    @Schema(description = "报表ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    /**
     * 审批状态
     * 
     * 枚举值：2-已审批，3-已驳回
     */
    @NotNull(message = "审批状态不能为空")
    @Min(value = 2, message = "审批状态最小值为2")
    @Max(value = 3, message = "审批状态最大值为3")
    @Schema(description = "审批状态（2-已审批，3-已驳回）", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer status;

    /**
     * 审批意见
     */
    @Size(max = 5000, message = "审批意见长度不能超过5000个字符")
    @Schema(description = "审批意见")
    private String approveComment;

}