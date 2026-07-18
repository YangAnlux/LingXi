package com.meession.etm.module.crm.controller.admin.task.vo.task;

import com.meession.etm.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.meession.etm.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 任务分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmTaskPageReqVO extends PageParam {

    @Schema(description = "任务名称", example = "跟进客户")
    private String name;

    @Schema(description = "状态", example = "0")
    private Integer status;

    @Schema(description = "优先级", example = "1")
    private Integer priority;

    @Schema(description = "负责人用户编号", example = "1")
    private Long ownerUserId;

    @Schema(description = "分配人用户编号", example = "2")
    private Long assignerUserId;

    @Schema(description = "截止时间开始", example = "2024-01-01 00:00:00")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime endTimeStart;

    @Schema(description = "截止时间结束", example = "2024-01-31 23:59:59")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime endTimeEnd;

}
