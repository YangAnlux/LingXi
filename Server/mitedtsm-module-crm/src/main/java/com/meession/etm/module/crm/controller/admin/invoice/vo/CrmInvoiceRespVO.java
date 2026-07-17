// 23软4胡伟-202305566535-修改于2026.07.14
// [ADD START] 发票响应VO - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
package com.meession.etm.module.crm.controller.admin.invoice.vo;

import com.meession.etm.framework.excel.core.annotations.DictFormat;
import com.meession.etm.framework.excel.core.convert.DictConvert;
import com.meession.etm.module.crm.enums.DictTypeConstants;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - CRM 发票 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CrmInvoiceRespVO {

    @Schema(description = "编号", example = "1024")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "发票编号", example = "FP2024010100001")
    @ExcelProperty("发票编号")
    private String no;

    @Schema(description = "发票号码", example = "INV-20240001")
    @ExcelProperty("发票号码")
    private String invoiceNo;

    @Schema(description = "发票类型", example = "1")
    @ExcelProperty(value = "发票类型", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.CRM_INVOICE_TYPE)
    private Integer type;

    @Schema(description = "开票金额", example = "5000.00")
    @ExcelProperty("开票金额")
    private BigDecimal amount;

    @Schema(description = "发票抬头", example = "XX科技有限公司")
    @ExcelProperty("发票抬头")
    private String title;

    @Schema(description = "税号", example = "91110108MA01XXXXX")
    @ExcelProperty("税号")
    private String taxNo;

    @Schema(description = "发票状态", example = "0")
    @ExcelProperty(value = "发票状态", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.CRM_INVOICE_STATUS)
    private Integer status;

    @Schema(description = "开票日期", example = "2024-01-15")
    @ExcelProperty("开票日期")
    private LocalDate issueDate;

    @Schema(description = "客户编号", example = "2")
    private Long customerId;
    @Schema(description = "客户名称", example = "XX公司")
    @ExcelProperty("客户名称")
    private String customerName;

    @Schema(description = "合同编号", example = "1")
    private Long contractId;
    @Schema(description = "合同名称", example = "销售合同A")
    @ExcelProperty("合同名称")
    private String contractName;

    @Schema(description = "负责人编号", example = "1")
    private Long ownerUserId;
    @Schema(description = "负责人名称", example = "张三")
    @ExcelProperty("负责人名称")
    private String ownerUserName;

    @Schema(description = "备注", example = "备注信息")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建者")
    private String creator;
    @Schema(description = "创建者名称")
    @ExcelProperty("创建者名称")
    private String creatorName;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @ExcelProperty("更新时间")
    private LocalDateTime updateTime;

}
// [ADD END] 发票响应VO - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
