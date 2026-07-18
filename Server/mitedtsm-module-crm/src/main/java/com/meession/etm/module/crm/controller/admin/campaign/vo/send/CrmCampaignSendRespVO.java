package com.meession.etm.module.crm.controller.admin.campaign.vo.send;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 营销活动群发 Response VO")
@Data
public class CrmCampaignSendRespVO {

    @Schema(description = "日志编号", example = "1")
    private Long id;

    @Schema(description = "营销活动编号", example = "1")
    private Long campaignId;

    @Schema(description = "渠道 1-短信 2-邮件", example = "1")
    private Integer channel;

    @Schema(description = "模板编码", example = "crm_holiday_care")
    private String templateCode;

    @Schema(description = "总发送数", example = "100")
    private Integer totalCount;

    @Schema(description = "成功数", example = "95")
    private Integer successCount;

    @Schema(description = "失败数", example = "5")
    private Integer failCount;

    @Schema(description = "邮件打开次数", example = "30")
    private Integer openCount;

    @Schema(description = "邮件点击次数", example = "12")
    private Integer clickCount;

    @Schema(description = "发送时间")
    private LocalDateTime sendTime;

}
