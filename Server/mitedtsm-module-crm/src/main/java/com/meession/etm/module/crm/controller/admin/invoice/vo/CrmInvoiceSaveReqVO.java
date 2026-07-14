// 23软4胡伟-202305566535-修改于2026.07.14
// [ADD START] 发票新增/修改请求VO - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
package com.meession.etm.module.crm.controller.admin.invoice.vo;

import com.meession.etm.framework.common.validation.InEnum;
import com.meession.etm.module.crm.enums.invoice.CrmInvoiceTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "管理后台 - CRM 发票新增/修改 Request VO")
@Data
public class CrmInvoiceSaveReqVO {

    @Schema(description = "编号", example = "1024")
    private Long id;

    @Schema(description = "负责人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "负责人编号不能为空")
    private Long ownerUserId;

    @Schema(description = "客户编号", example = "2")
    private Long customerId;

    @Schema(description = "合同编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "合同编号不能为空")
    private Long contractId;

    @Schema(description = "关联订单ID（预留）", example = "1")
    private Long orderId;

    @Schema(description = "发票号码", requiredMode = Schema.RequiredMode.REQUIRED, example = "INV-20240001")
    @NotEmpty(message = "发票号码不能为空")
    private String invoiceNo;

    @Schema(description = "发票类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "发票类型不能为空")
    @InEnum(CrmInvoiceTypeEnum.class)
    private Integer type;

    @Schema(description = "开票金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "5000.00")
    @NotNull(message = "开票金额不能为空")
    private BigDecimal amount;

    @Schema(description = "发票抬头", example = "XX科技有限公司")
    private String title;

    @Schema(description = "税号", example = "91110108MA01XXXXX")
    private String taxNo;

    @Schema(description = "开票日期", example = "2024-01-15")
    private LocalDate issueDate;

    @Schema(description = "备注", example = "备注信息")
    private String remark;

}
// [ADD END] 发票新增/修改请求VO - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
