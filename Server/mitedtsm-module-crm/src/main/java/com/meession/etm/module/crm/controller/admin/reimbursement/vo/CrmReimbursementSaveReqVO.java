// 23软4胡伟-202305566535-修改于2026.07.16
// [ADD START] 报销新增/修改请求VO - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
package com.meession.etm.module.crm.controller.admin.reimbursement.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.meession.etm.framework.common.validation.InEnum;
import com.meession.etm.module.crm.enums.expense.CrmExpenseTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.meession.etm.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;

@Schema(description = "管理后台 - CRM 报销新增/修改 Request VO")
@Data
public class CrmReimbursementSaveReqVO {

    @Schema(description = "编号", example = "1024")
    private Long id;

    @Schema(description = "负责人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "负责人编号不能为空")
    private Long ownerUserId;

    @Schema(description = "客户编号", example = "2")
    private Long customerId;

    @Schema(description = "合同编号", example = "1")
    private Long contractId;

    @Schema(description = "报销日期", example = "2026-07-16")
    @JsonFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    private LocalDate reimbursementDate;

    @Schema(description = "报销总金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "500.00")
    @NotNull(message = "报销金额不能为空")
    private BigDecimal totalAmount;

    @Schema(description = "报销类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "报销类型不能为空")
    @InEnum(CrmExpenseTypeEnum.class)
    private Integer type;

    @Schema(description = "备注", example = "差旅报销")
    private String remark;

}
// [ADD END] 报销新增/修改请求VO - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
