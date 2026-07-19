package com.meession.etm.module.crm.controller.admin.campaign.vo.campaign;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.meession.etm.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 营销活动创建/更新 Request VO")
@Data
public class CrmCampaignSaveReqVO {

    @Schema(description = "编号", example = "1")
    private Long id;

    @Schema(description = "活动名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "夏季促销活动")
    @NotEmpty(message = "活动名称不能为空")
    private String name;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "开始时间", example = "2023-01-01 00:00:00")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime startTime;

    @Schema(description = "结束时间", example = "2023-01-31 23:59:59")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime endTime;

    @Schema(description = "活动预算(元)", example = "10000.00")
    private BigDecimal budget;

    @Schema(description = "实际花费(元)", example = "8000.00")
    private BigDecimal actualCost;

    @Schema(description = "渠道类型", example = "1")
    private Integer channel;

    @Schema(description = "活动描述", example = "活动描述内容")
    private String description;

    @Schema(description = "负责人用户编号", example = "1")
    private Long ownerUserId;

    @Schema(description = "备注", example = "备注内容")
    private String remark;

}
