package com.meession.etm.module.crm.controller.admin.task.vo.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.meession.etm.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 任务创建/更新 Request VO")
@Data
public class CrmTaskSaveReqVO {

    @Schema(description = "编号", example = "1")
    private Long id;

    @Schema(description = "任务名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "跟进客户需求")
    @NotEmpty(message = "任务名称不能为空")
    private String name;

    @Schema(description = "任务描述", example = "详细跟进客户关于产品的需求")
    private String description;

    @Schema(description = "状态", example = "0")
    private Integer status;

    @Schema(description = "优先级", example = "1")
    private Integer priority;

    @Schema(description = "计划开始时间", example = "2024-01-01 09:00:00")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime startTime;

    @Schema(description = "截止时间", example = "2024-01-31 18:00:00")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime endTime;

    @Schema(description = "实际完成时间", example = "2024-01-25 17:00:00")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime completedTime;

    @Schema(description = "负责人用户编号", example = "1")
    private Long ownerUserId;

    @Schema(description = "分配人用户编号", example = "2")
    private Long assignerUserId;

    @Schema(description = "关联营销活动编号", example = "1")
    private Long relatedCampaignId;

    @Schema(description = "关联客户编号", example = "1")
    private Long relatedCustomerId;

    @Schema(description = "关联商机编号", example = "1")
    private Long relatedBusinessId;

    @Schema(description = "备注", example = "备注内容")
    private String remark;

}
