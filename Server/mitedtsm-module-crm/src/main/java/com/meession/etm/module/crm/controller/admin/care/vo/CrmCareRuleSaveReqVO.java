package com.meession.etm.module.crm.controller.admin.care.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Schema(description = "管理后台 - 关怀规则创建/修改 Request VO")
@Data
public class CrmCareRuleSaveReqVO {

    @Schema(description = "规则编号", example = "1")
    private Long id;

    @Schema(description = "规则名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "生日关怀规则")
    @NotEmpty(message = "规则名称不能为空")
    private String name;

    @Schema(description = "触发类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "BIRTHDAY")
    @NotEmpty(message = "触发类型不能为空")
    private String triggerType;

    @Schema(description = "提前N天触发", example = "0")
    private Integer triggerDaysBefore;

    @Schema(description = "关联节日ID", example = "1")
    private Long holidayId;

    @Schema(description = "模板ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "模板ID不能为空")
    private Long templateId;

    @Schema(description = "模板编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "crm_birthday_care")
    @NotEmpty(message = "模板编码不能为空")
    private String templateCode;

    @Schema(description = "发送渠道 1-短信 2-邮件", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "发送渠道不能为空")
    private Integer channel;

    @Schema(description = "发送窗口开始（HH:mm）", example = "08:00:00")
    private LocalTime sendWindowStart;

    @Schema(description = "发送窗口结束（HH:mm）", example = "20:00:00")
    private LocalTime sendWindowEnd;

    @Schema(description = "是否启用 0-停用 1-启用", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer isEnabled;

    @Schema(description = "备注")
    private String remark;

}
