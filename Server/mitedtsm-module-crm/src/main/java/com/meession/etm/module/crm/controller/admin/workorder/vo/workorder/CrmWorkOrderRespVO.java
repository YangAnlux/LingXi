// 2023级软4蔡磊202305566515,2026年7月14日
package com.meession.etm.module.crm.controller.admin.workorder.vo.workorder;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 工单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CrmWorkOrderRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "工单标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "服务请求")
    @ExcelProperty("工单标题")
    private String title;

    @Schema(description = "工单类型", example = "技术服务")
    @ExcelProperty("工单类型")
    private String type;

    @Schema(description = "优先级", example = "NORMAL")
    @ExcelProperty("优先级")
    private String priority;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "待处理")
    @ExcelProperty("状态")
    private String status;

    @Schema(description = "客户编号", example = "1024")
    private Long customerId;

    @Schema(description = "客户名称", example = "XX科技")
    @ExcelProperty("客户名称")
    private String customerName;

    @Schema(description = "处理人编号", example = "10001")
    private Long assigneeId;

    @Schema(description = "处理人名称", example = "张三")
    @ExcelProperty("处理人")
    private String assigneeName;

    @Schema(description = "SLA 截止时间")
    @ExcelProperty("SLA截止时间")
    private LocalDateTime slaDeadline;

    @Schema(description = "是否超SLA")
    @ExcelProperty("是否超SLA")
    private Boolean isSlaBreached;

    @Schema(description = "工单内容")
    private String content;

    @Schema(description = "解决方案")
    private String solution;

    @Schema(description = "完结时间")
    @ExcelProperty("完结时间")
    private LocalDateTime resolvedTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建人", example = "admin")
    @ExcelProperty("创建人")
    private String creator;

    // 23软件工程4班蔡磊202305566515
    @Schema(description = "满意度评分（1-5）", example = "5")
    @ExcelProperty("满意度评分")
    private Integer satisfactionScore;

    // 23软件工程4班蔡磊202305566515
    @Schema(description = "满意度评价内容", example = "服务很好")
    @ExcelProperty("满意度评价")
    private String satisfactionComment;

}
