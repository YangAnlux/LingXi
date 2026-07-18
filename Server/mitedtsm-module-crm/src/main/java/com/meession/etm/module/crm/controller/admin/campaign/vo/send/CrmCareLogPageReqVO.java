package com.meession.etm.module.crm.controller.admin.campaign.vo.send;

import com.meession.etm.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.meession.etm.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 关怀日志分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmCareLogPageReqVO extends PageParam {

    @Schema(description = "关怀类型 1-生日 2-节日 3-手动", example = "1")
    private Integer careType;

    @Schema(description = "渠道 1-短信 2-邮件", example = "1")
    private Integer channel;

    @Schema(description = "状态 0-成功 1-失败", example = "0")
    private Integer status;

    @Schema(description = "客户编号", example = "1")
    private Long customerId;

    @Schema(description = "发送时间开始")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime sendTimeStart;

    @Schema(description = "发送时间结束")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime sendTimeEnd;

}
