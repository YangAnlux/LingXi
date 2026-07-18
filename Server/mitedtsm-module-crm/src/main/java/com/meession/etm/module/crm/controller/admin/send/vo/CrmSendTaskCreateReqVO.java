package com.meession.etm.module.crm.controller.admin.send.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 创建发送任务 Request VO")
@Data
public class CrmSendTaskCreateReqVO {

    @Schema(description = "关联活动编号（可为空）", example = "1")
    private Long activityId;

    @Schema(description = "任务类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "MARKETING")
    @NotEmpty(message = "任务类型不能为空")
    private String taskType;

    @Schema(description = "模板ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "模板ID不能为空")
    private Long templateId;

    @Schema(description = "模板编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "crm_birthday_care")
    @NotEmpty(message = "模板编码不能为空")
    private String templateCode;

    @Schema(description = "发送渠道 1-短信 2-邮件", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "发送渠道不能为空")
    private Integer channel;

    @Schema(description = "客户选择模式 ALL_ACTIVE/CSV/BY_TAG", requiredMode = Schema.RequiredMode.REQUIRED, example = "ALL_ACTIVE")
    @NotEmpty(message = "客户选择模式不能为空")
    private String customerSelectMode;

    @Schema(description = "客户ID列表（手动选择时）")
    private List<Long> customerIds;

    @Schema(description = "客户标签（BY_TAG 模式）")
    private String customerTag;

    @Schema(description = "发送模式 IMMEDIATE/SCHEDULED", requiredMode = Schema.RequiredMode.REQUIRED, example = "IMMEDIATE")
    @NotEmpty(message = "发送模式不能为空")
    private String sendMode;

    @Schema(description = "定时发送时间（SCHEDULED 模式必填）")
    private LocalDateTime scheduledTime;

    @Schema(description = "是否强制发送（忽略变量缺失警告）", example = "false")
    private Boolean forceSend;

}
