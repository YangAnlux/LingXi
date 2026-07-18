package com.meession.etm.module.crm.controller.admin.schedule.vo.schedule;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import com.meession.etm.framework.excel.core.annotations.DictFormat;
import com.meession.etm.framework.excel.core.convert.DictConvert;
import com.meession.etm.framework.excel.core.convert.LocalDateTimeConverter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 日程 Response VO")
@Data
@ToString(callSuper = true)
@ExcelIgnoreUnannotated
public class CrmScheduleRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "日程标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "团队周会")
    @ExcelProperty("日程标题")
    private String title;

    @Schema(description = "日程描述", example = "讨论本周工作计划")
    @ExcelProperty("日程描述")
    private String description;

    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "开始时间", converter = LocalDateTimeConverter.class)
    private LocalDateTime startTime;

    @Schema(description = "结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "结束时间", converter = LocalDateTimeConverter.class)
    private LocalDateTime endTime;

    @Schema(description = "是否全天")
    private Boolean allDay;

    @Schema(description = "颜色标记", example = "#409eff")
    private String color;

    @Schema(description = "日程类型", example = "1")
    @ExcelProperty(value = "日程类型", converter = DictConvert.class)
    @DictFormat(com.meession.etm.module.crm.enums.DictTypeConstants.CRM_SCHEDULE_TYPE)
    private Integer scheduleType;

    @Schema(description = "状态", example = "0")
    private Integer status;

    @Schema(description = "所属用户编号", example = "1")
    private Long ownerUserId;

    @Schema(description = "所属用户名", example = "张三")
    @ExcelProperty("所属用户")
    private String ownerUserName;

    @Schema(description = "关联任务编号", example = "1")
    private Long relatedTaskId;

    @Schema(description = "备注", example = "备注内容")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建人", example = "1")
    private String creator;

    @Schema(description = "创建人名字", example = "李四")
    @ExcelProperty("创建人名字")
    private String creatorName;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "创建时间", converter = LocalDateTimeConverter.class)
    private LocalDateTime createTime;

}
