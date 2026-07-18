// 23软4胡伟-202305566535-修改于2026.07.17
// [ADD START] 退款新增/修改请求VO - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
package com.meession.etm.module.crm.controller.admin.refund.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meession.etm.framework.common.validation.InEnum;
import com.meession.etm.module.crm.enums.refund.CrmRefundTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.meession.etm.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;

@Schema(description = "管理后台 - CRM 退款新增/修改 Request VO")
@Data
public class CrmRefundSaveReqVO {

    @Schema(description = "编号", example = "1024")
    private Long id;

    @Schema(description = "负责人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "负责人编号不能为空")
    private Long ownerUserId;

    @Schema(description = "客户编号", example = "2")
    private Long customerId;

    @Schema(description = "合同编号", example = "1")
    private Long contractId;

    @Schema(description = "退款金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "1000.00")
    @NotNull(message = "退款金额不能为空")
    private BigDecimal refundAmount;

    @Schema(description = "退款日期", example = "2026-07-17")
    @JsonFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    private LocalDate refundDate;

    @Schema(description = "退款原因", example = "订单取消退回")
    private String refundReason;

    @Schema(description = "退款类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "退款类型不能为空")
    @InEnum(CrmRefundTypeEnum.class)
    private Integer refundType;

    @Schema(description = "备注", example = "备注")
    private String remark;

}
// [ADD END] 退款新增/修改请求VO - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
