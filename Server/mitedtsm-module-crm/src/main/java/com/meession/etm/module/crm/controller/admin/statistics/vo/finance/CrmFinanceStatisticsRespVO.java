// 23软4胡伟-202305566535-修改于2026.07.17
// [ADD START] 财务汇总响应VO - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
package com.meession.etm.module.crm.controller.admin.statistics.vo.finance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "管理后台 - CRM 财务汇总 Response VO")
@Data
public class CrmFinanceStatisticsRespVO {

    // ========== 回款 ==========
    @Schema(description = "回款笔数", example = "50")
    private Long receivableCount;

    @Schema(description = "回款总额", example = "500000.00")
    private BigDecimal receivableTotal;

    // ========== 发票 ==========
    @Schema(description = "发票数量", example = "30")
    private Long invoiceCount;

    @Schema(description = "开票总额", example = "450000.00")
    private BigDecimal invoiceTotal;

    // ========== 费用 ==========
    @Schema(description = "费用笔数", example = "20")
    private Long expenseCount;

    @Schema(description = "费用总额", example = "80000.00")
    private BigDecimal expenseTotal;

    // ========== 报销 ==========
    @Schema(description = "报销笔数", example = "15")
    private Long reimbursementCount;

    @Schema(description = "报销总额", example = "60000.00")
    private BigDecimal reimbursementTotal;

    // ========== 退款 ==========
    @Schema(description = "退款笔数", example = "5")
    private Long refundCount;

    @Schema(description = "退款总额", example = "30000.00")
    private BigDecimal refundTotal;

}
// [ADD END] 财务汇总响应VO - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
