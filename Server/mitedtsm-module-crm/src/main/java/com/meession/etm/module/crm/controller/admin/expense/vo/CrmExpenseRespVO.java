// 23软4胡伟-202305566535-修改于2026.07.16
// [ADD START] 费用响应VO - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
package com.meession.etm.module.crm.controller.admin.expense.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import com.meession.etm.framework.excel.core.annotations.DictFormat;
import com.meession.etm.framework.excel.core.convert.DictConvert;
import com.meession.etm.module.crm.enums.DictTypeConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - CRM 费用 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CrmExpenseRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "费用编号（自动生成）", requiredMode = Schema.RequiredMode.REQUIRED, example = "FY20260716000001")
    @ExcelProperty("费用编号")
    private String no;

    @Schema(description = "客户编号", example = "2")
    private Long customerId;

    @Schema(description = "客户名称", example = "XX科技有限公司")
    @ExcelProperty("客户名称")
    private String customerName;

    @Schema(description = "合同编号", example = "1")
    private Long contractId;

    @Schema(description = "合同名称", example = "XX项目合同")
    @ExcelProperty("合同名称")
    private String contractName;

    @Schema(description = "负责人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long ownerUserId;

    @Schema(description = "负责人", example = "张三")
    @ExcelProperty("负责人")
    private String ownerUserName;

    @Schema(description = "费用类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "费用类型", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.CRM_EXPENSE_TYPE)
    private Integer type;

    @Schema(description = "费用金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "500.00")
    @ExcelProperty("费用金额")
    private BigDecimal amount;

    @Schema(description = "费用日期", example = "2026-07-16")
    @ExcelProperty("费用日期")
    private LocalDate expenseDate;

    @Schema(description = "备注", example = "出差费用")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建人", example = "admin")
    @ExcelProperty("创建人")
    private String creatorName;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @ExcelProperty("更新时间")
    private LocalDateTime updateTime;

}
// [ADD END] 费用响应VO - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
