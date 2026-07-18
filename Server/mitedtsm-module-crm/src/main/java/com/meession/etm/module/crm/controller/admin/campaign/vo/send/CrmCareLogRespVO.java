package com.meession.etm.module.crm.controller.admin.campaign.vo.send;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 关怀日志 Response VO")
@Data
public class CrmCareLogRespVO {

    @Schema(description = "编号", example = "1")
    private Long id;

    @Schema(description = "客户编号", example = "1")
    private Long customerId;

    @Schema(description = "客户名称", example = "张三")
    private String customerName;

    @Schema(description = "营销活动编号", example = "1")
    private Long campaignId;

    @Schema(description = "关怀类型", example = "1")
    private Integer careType;

    @Schema(description = "渠道", example = "1")
    private Integer channel;

    @Schema(description = "模板编码", example = "crm_birthday_care")
    private String templateCode;

    @Schema(description = "发送内容", example = "尊敬的张三，祝您生日快乐！")
    private String content;

    @Schema(description = "状态 0-成功 1-失败", example = "0")
    private Integer status;

    @Schema(description = "发送时间")
    private LocalDateTime sendTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
