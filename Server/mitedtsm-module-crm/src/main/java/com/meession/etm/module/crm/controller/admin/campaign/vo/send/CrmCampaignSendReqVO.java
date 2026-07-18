package com.meession.etm.module.crm.controller.admin.campaign.vo.send;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - 营销活动群发 Request VO")
@Data
public class CrmCampaignSendReqVO {

    @Schema(description = "营销活动编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "营销活动编号不能为空")
    private Long campaignId;

    @Schema(description = "模板编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "crm_holiday_care")
    @NotEmpty(message = "模板编码不能为空")
    private String templateCode;

    @Schema(description = "客户编号列表", requiredMode = Schema.RequiredMode.REQUIRED, example = "[1,2,3]")
    @NotEmpty(message = "客户列表不能为空")
    private List<Long> customerIds;

}
