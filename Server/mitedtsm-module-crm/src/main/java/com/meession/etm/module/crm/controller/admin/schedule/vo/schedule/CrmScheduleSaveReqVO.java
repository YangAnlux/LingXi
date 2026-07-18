package com.meession.etm.module.crm.controller.admin.schedule.vo.schedule;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.meession.etm.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 日程创建/更新 Request VO")
@Data
public class CrmScheduleSaveReqVO {

    @Schema(description = "编号", example = "1")
    private Long id;

    @Schema(description = "日程标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "团队周会")
    @NotEmpty(message = "日程标题不能为空")
    private String title;

    @Schema(description = "日程描述", example = "讨论本周工作计划")
    private String description;

    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-01-01 09:00:00")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime startTime;

    @Schema(description = "结束时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-01-01 10:00:00")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime endTime;

    @Schema(description = "是否全天", example = "false")
    private Boolean allDay;

    @Schema(description = "颜色标记", example = "#409eff")
    private String color;

    @Schema(description = "日程类型", example = "1")
    private Integer scheduleType;

    @Schema(description = "所属用户编号", example = "1")
    private Long ownerUserId;

    @Schema(description = "关联任务编号", example = "1")
    private Long relatedTaskId;

    @Schema(description = "备注", example = "备注内容")
    private String remark;

}
