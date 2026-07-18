package com.meession.etm.module.crm.controller.admin.workreport.vo.workreport;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 工作报告创建/更新 Request VO")
@Data
public class CrmWorkReportSaveReqVO {

    @Schema(description = "编号", example = "1")
    private Long id;

    @Schema(description = "报告标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024Q1工作总结")
    @NotEmpty(message = "报告标题不能为空")
    private String title;

    @Schema(description = "报告内容", example = "本季度完成了...")
    private String content;

    @Schema(description = "报告日期", example = "2024-03-31")
    private LocalDate reportDate;

    @Schema(description = "报告类型", example = "2")
    private Integer reportType;

    @Schema(description = "审批状态", example = "0")
    private Integer status;

    @Schema(description = "提交人用户编号", example = "1")
    private Long ownerUserId;

    @Schema(description = "审批人用户编号", example = "2")
    private Long auditUserId;

    @Schema(description = "审批时间")
    private LocalDateTime auditTime;

    @Schema(description = "审批意见", example = "同意")
    private String auditRemark;

    @Schema(description = "关联营销活动编号", example = "1")
    private Long relatedCampaignId;

    @Schema(description = "关联客户编号", example = "1")
    private Long relatedCustomerId;

    @Schema(description = "备注", example = "备注内容")
    private String remark;

}
