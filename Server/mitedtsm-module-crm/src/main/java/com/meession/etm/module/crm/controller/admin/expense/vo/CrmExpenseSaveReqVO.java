// 23软4胡伟-202305566535-修改于2026.07.16
// [ADD START] 费用新增/修改请求VO - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
package com.meession.etm.module.crm.controller.admin.expense.vo;

import com.meession.etm.framework.common.validation.InEnum;
import com.meession.etm.module.crm.enums.expense.CrmExpenseTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.meession.etm.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;

@Schema(description = "管理后台 - CRM 费用新增/修改 Request VO")
@Data
public class CrmExpenseSaveReqVO {

    @Schema(description = "编号", example = "1024")
    private Long id;

    @Schema(description = "负责人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "负责人编号不能为空")
    private Long ownerUserId;

    @Schema(description = "客户编号", example = "2")
    private Long customerId;

    @Schema(description = "合同编号", example = "1")
    private Long contractId;

    @Schema(description = "关联订单ID（预留）", example = "1")
    private Long orderId;

    @Schema(description = "费用类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "费用类型不能为空")
    @InEnum(CrmExpenseTypeEnum.class)
    private Integer type;

    @Schema(description = "费用金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "500.00")
    @NotNull(message = "费用金额不能为空")
    private BigDecimal amount;

    @Schema(description = "费用日期", example = "2026-07-16")
    @JsonFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    private LocalDate expenseDate;

    @Schema(description = "备注", example = "出差费用")
    private String remark;

}
// [ADD END] 费用新增/修改请求VO - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
