// 23软4胡伟-202305566535-修改于2026.07.17
// [ADD START] 退款响应VO - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
package com.meession.etm.module.crm.controller.admin.refund.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - CRM 退款 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CrmRefundRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "退款编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "TK20260717001")
    @ExcelProperty("退款编号")
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

    @Schema(description = "退款金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "1000.00")
    @ExcelProperty("退款金额")
    private BigDecimal refundAmount;

    @Schema(description = "退款日期", example = "2026-07-17")
    @ExcelProperty("退款日期")
    private LocalDate refundDate;

    @Schema(description = "退款原因", example = "订单取消退回")
    @ExcelProperty("退款原因")
    private String refundReason;

    @Schema(description = "退款类型", example = "1")
    @ExcelProperty("退款类型")
    private Integer refundType;

    @Schema(description = "退款状态（0=待提交 1=审批中 2=已通过 3=已驳回）", example = "0")
    @ExcelProperty("退款状态")
    private Integer status;

    @Schema(description = "备注", example = "备注")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "工作流编号", example = "1043")
    private String processInstanceId;

    @Schema(description = "创建人")
    private String creatorName;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("更新时间")
    private LocalDateTime updateTime;

}
// [ADD END] 退款响应VO - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
