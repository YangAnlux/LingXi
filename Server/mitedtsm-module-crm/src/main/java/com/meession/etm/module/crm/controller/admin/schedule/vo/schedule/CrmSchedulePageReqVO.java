package com.meession.etm.module.crm.controller.admin.schedule.vo.schedule;

import com.meession.etm.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.meession.etm.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 日程分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmSchedulePageReqVO extends PageParam {

    @Schema(description = "日程标题", example = "周会")
    private String title;

    @Schema(description = "日程类型", example = "1")
    private Integer scheduleType;

    @Schema(description = "所属用户编号", example = "1")
    private Long ownerUserId;

    @Schema(description = "状态", example = "0")
    private Integer status;

    @Schema(description = "开始时间", example = "2024-01-01 00:00:00")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime startTime;

    @Schema(description = "结束时间", example = "2024-01-31 23:59:59")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime endTime;

}
