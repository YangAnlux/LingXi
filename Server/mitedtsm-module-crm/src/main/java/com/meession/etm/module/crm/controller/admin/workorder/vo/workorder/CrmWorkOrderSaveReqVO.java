// 2023级软4蔡磊202305566515,2026年7月14日
package com.meession.etm.module.crm.controller.admin.workorder.vo.workorder;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.meession.etm.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 工单创建/更新 Request VO")
@Data
public class CrmWorkOrderSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "工单标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "服务请求")
    @NotBlank(message = "工单标题不能为空")
    private String title;

    @Schema(description = "工单类型", example = "技术服务")
    private String type;

    @Schema(description = "优先级", example = "NORMAL")
    private String priority;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "待处理")
    @NotBlank(message = "工单状态不能为空")
    private String status;

    @Schema(description = "客户编号", example = "1024")
    private Long customerId;

    @Schema(description = "处理人编号", example = "10001")
    private Long assigneeId;

    @Schema(description = "SLA 截止时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime slaDeadline;

    @Schema(description = "工单内容")
    private String content;

    @Schema(description = "解决方案")
    private String solution;

}
