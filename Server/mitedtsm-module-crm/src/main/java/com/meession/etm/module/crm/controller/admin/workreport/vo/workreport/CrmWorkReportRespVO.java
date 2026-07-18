package com.meession.etm.module.crm.controller.admin.workreport.vo.workreport;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import com.meession.etm.framework.excel.core.annotations.DictFormat;
import com.meession.etm.framework.excel.core.convert.DictConvert;
import com.meession.etm.framework.excel.core.convert.LocalDateTimeConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 工作报告 Response VO")
@Data
@ToString(callSuper = true)
@ExcelIgnoreUnannotated
public class CrmWorkReportRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "报告标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "Q1工作总结")
    @ExcelProperty("报告标题")
    private String title;

    @Schema(description = "报告内容", example = "本季度完成了...")
    @ExcelProperty("报告内容")
    private String content;

    @Schema(description = "报告日期", example = "2024-03-31")
    @ExcelProperty("报告日期")
    private LocalDate reportDate;

    @Schema(description = "报告类型", example = "2")
    @ExcelProperty(value = "报告类型", converter = DictConvert.class)
    @DictFormat(com.meession.etm.module.crm.enums.DictTypeConstants.CRM_WORK_REPORT_TYPE)
    private Integer reportType;

    @Schema(description = "审批状态", example = "0")
    @ExcelProperty(value = "审批状态", converter = DictConvert.class)
    @DictFormat(com.meession.etm.module.crm.enums.DictTypeConstants.CRM_WORK_REPORT_STATUS)
    private Integer status;

    @Schema(description = "提交人用户编号", example = "1")
    private Long ownerUserId;

    @Schema(description = "提交人名字", example = "张三")
    @ExcelProperty("提交人")
    private String ownerUserName;

    @Schema(description = "审批人用户编号", example = "2")
    private Long auditUserId;

    @Schema(description = "审批人名字", example = "李四")
    @ExcelProperty("审批人")
    private String auditUserName;

    @Schema(description = "审批时间")
    @ExcelProperty(value = "审批时间", converter = LocalDateTimeConverter.class)
    private LocalDateTime auditTime;

    @Schema(description = "审批意见", example = "同意")
    @ExcelProperty("审批意见")
    private String auditRemark;

    @Schema(description = "关联营销活动编号", example = "1")
    private Long relatedCampaignId;

    @Schema(description = "关联客户编号", example = "1")
    private Long relatedCustomerId;

    @Schema(description = "备注", example = "备注内容")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建人", example = "1")
    @ExcelProperty("创建人")
    private String creator;

    @Schema(description = "创建人名字", example = "李四")
    @ExcelProperty("创建人名字")
    private String creatorName;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "创建时间", converter = LocalDateTimeConverter.class)
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "更新时间", converter = LocalDateTimeConverter.class)
    private LocalDateTime updateTime;

}
