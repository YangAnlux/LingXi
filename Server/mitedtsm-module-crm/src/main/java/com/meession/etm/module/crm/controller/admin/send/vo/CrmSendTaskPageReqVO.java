package com.meession.etm.module.crm.controller.admin.send.vo;

import com.meession.etm.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 发送任务分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmSendTaskPageReqVO extends PageParam {

    @Schema(description = "任务类型", example = "MARKETING")
    private String taskType;

    @Schema(description = "发送渠道 1-短信 2-邮件", example = "1")
    private Integer channel;

    @Schema(description = "状态", example = "PENDING")
    private String status;

    @Schema(description = "关联活动编号", example = "1")
    private Long activityId;

}
