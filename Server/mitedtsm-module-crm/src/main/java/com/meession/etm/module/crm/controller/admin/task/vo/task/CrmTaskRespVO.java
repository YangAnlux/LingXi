package com.meession.etm.module.crm.controller.admin.task.vo.task;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import com.meession.etm.framework.excel.core.annotations.DictFormat;
import com.meession.etm.framework.excel.core.convert.DictConvert;
import com.meession.etm.framework.excel.core.convert.LocalDateTimeConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 任务 Response VO")
@Data
@ToString(callSuper = true)
@ExcelIgnoreUnannotated
public class CrmTaskRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "任务名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "跟进客户需求")
    @ExcelProperty("任务名称")
    private String name;

    @Schema(description = "任务描述", example = "详细跟进客户关于产品的需求")
    @ExcelProperty("任务描述")
    private String description;

    @Schema(description = "状态", example = "0")
    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat(com.meession.etm.module.crm.enums.DictTypeConstants.CRM_TASK_STATUS)
    private Integer status;

    @Schema(description = "优先级", example = "1")
    @ExcelProperty(value = "优先级", converter = DictConvert.class)
    @DictFormat(com.meession.etm.module.crm.enums.DictTypeConstants.CRM_TASK_PRIORITY)
    private Integer priority;

    @Schema(description = "计划开始时间", example = "2024-01-01 09:00:00")
    @ExcelProperty(value = "计划开始时间", converter = LocalDateTimeConverter.class)
    private LocalDateTime startTime;

    @Schema(description = "截止时间", example = "2024-01-31 18:00:00")
    @ExcelProperty(value = "截止时间", converter = LocalDateTimeConverter.class)
    private LocalDateTime endTime;

    @Schema(description = "实际完成时间", example = "2024-01-25 17:00:00")
    @ExcelProperty(value = "实际完成时间", converter = LocalDateTimeConverter.class)
    private LocalDateTime completedTime;

    @Schema(description = "负责人用户编号", example = "1")
    private Long ownerUserId;

    @Schema(description = "负责人名字", example = "张三")
    @ExcelProperty("负责人")
    private String ownerUserName;

    @Schema(description = "分配人用户编号", example = "2")
    private Long assignerUserId;

    @Schema(description = "分配人名字", example = "李四")
    @ExcelProperty("分配人")
    private String assignerUserName;

    @Schema(description = "关联营销活动编号", example = "1")
    private Long relatedCampaignId;

    @Schema(description = "关联客户编号", example = "1")
    private Long relatedCustomerId;

    @Schema(description = "关联商机编号", example = "1")
    private Long relatedBusinessId;

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
