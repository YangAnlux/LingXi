package com.meession.etm.module.crm.controller.admin.care.vo.holiday;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 节日配置 Response VO")
@Data
public class CrmHolidayRespVO {

    @Schema(description = "编号", example = "1")
    private Long id;

    @Schema(description = "节日名称", example = "春节")
    private String name;

    @Schema(description = "节日日期", example = "2026-02-17")
    private LocalDate holidayDate;

    @Schema(description = "模板编码", example = "crm_holiday_care")
    private String templateCode;

    @Schema(description = "状态 0-启用 1-禁用", example = "0")
    private Integer status;

    @Schema(description = "备注", example = "春节")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}
