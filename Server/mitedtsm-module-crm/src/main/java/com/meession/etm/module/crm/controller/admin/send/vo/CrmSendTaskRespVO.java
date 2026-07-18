package com.meession.etm.module.crm.controller.admin.send.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 发送任务 Response VO")
@Data
public class CrmSendTaskRespVO {

    @Schema(description = "任务编号")
    private Long id;

    @Schema(description = "关联活动编号")
    private Long activityId;

    @Schema(description = "任务类型")
    private String taskType;

    @Schema(description = "模板编码")
    private String templateCode;

    @Schema(description = "发送渠道 1-短信 2-邮件")
    private Integer channel;

    @Schema(description = "客户选择模式")
    private String customerSelectMode;

    @Schema(description = "目标客户数")
    private Integer customerCount;

    @Schema(description = "发送模式")
    private String sendMode;

    @Schema(description = "定时发送时间")
    private LocalDateTime scheduledTime;

    @Schema(description = "状态")
    private String status;

    @Schema(description = "缺失变量客户数")
    private Integer missingVarCount;

    @Schema(description = "是否有变量缺失警告")
    private Boolean hasWarning;

    @Schema(description = "缺失的变量列表")
    private List<String> missingVariables;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
