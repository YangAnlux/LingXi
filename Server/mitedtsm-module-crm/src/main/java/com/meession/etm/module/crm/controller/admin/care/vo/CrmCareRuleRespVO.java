package com.meession.etm.module.crm.controller.admin.care.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Schema(description = "管理后台 - 关怀规则 Response VO")
@Data
public class CrmCareRuleRespVO {

    @Schema(description = "规则编号")
    private Long id;

    @Schema(description = "规则名称")
    private String name;

    @Schema(description = "触发类型")
    private String triggerType;

    @Schema(description = "提前N天触发")
    private Integer triggerDaysBefore;

    @Schema(description = "关联节日ID")
    private Long holidayId;

    @Schema(description = "模板ID")
    private Long templateId;

    @Schema(description = "模板编码")
    private String templateCode;

    @Schema(description = "发送渠道 1-短信 2-邮件")
    private Integer channel;

    @Schema(description = "发送窗口开始")
    private LocalTime sendWindowStart;

    @Schema(description = "发送窗口结束")
    private LocalTime sendWindowEnd;

    @Schema(description = "是否启用 0-停用 1-启用")
    private Integer isEnabled;

    @Schema(description = "最近一次执行时间")
    private LocalDateTime lastExecTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
