// 23软4胡伟-202305566535-修改于2026.07.16
// [ADD START] 报销响应VO - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
package com.meession.etm.module.crm.controller.admin.reimbursement.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - CRM 报销 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CrmReimbursementRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "报销编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "BX20260716001")
    @ExcelProperty("报销编号")
    private String no;

    @Schema(description = "客户编号", example = "2")
    private Long customerId;

    @Schema(description = "客户名称", example = "密讯科技")
    @ExcelProperty("客户名称")
    private String customerName;

    @Schema(description = "合同编号", example = "1")
    private Long contractId;

    @Schema(description = "合同名称", example = "销售合同")
    @ExcelProperty("合同名称")
    private String contractName;

    @Schema(description = "负责人编号", example = "1")
    private Long ownerUserId;

    @Schema(description = "负责人", example = "张三")
    @ExcelProperty("负责人")
    private String ownerUserName;

    @Schema(description = "报销日期", example = "2026-07-16")
    @ExcelProperty("报销日期")
    private LocalDate reimbursementDate;

    @Schema(description = "报销总金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "500.00")
    @ExcelProperty("报销总金额")
    private BigDecimal totalAmount;

    @Schema(description = "报销类型", example = "1")
    @ExcelProperty("报销类型")
    private Integer type;

    @Schema(description = "报销状态（0=待提交 1=审批中 2=已通过 3=已驳回）", example = "0")
    @ExcelProperty("报销状态")
    private Integer status;

    @Schema(description = "备注", example = "备注")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "工作流编号", example = "1043")
    private String processInstanceId;

    @Schema(description = "创建人")
    private String creatorName;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}
// [ADD END] 报销响应VO - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
