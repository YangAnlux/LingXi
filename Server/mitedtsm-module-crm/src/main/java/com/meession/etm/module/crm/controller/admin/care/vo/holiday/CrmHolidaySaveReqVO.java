package com.meession.etm.module.crm.controller.admin.care.vo.holiday;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Schema(description = "管理后台 - 节日配置创建/更新 Request VO")
@Data
public class CrmHolidaySaveReqVO {

    @Schema(description = "编号", example = "1")
    private Long id;

    @Schema(description = "节日名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "春节")
    @NotEmpty(message = "节日名称不能为空")
    private String name;

    @Schema(description = "节日日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2026-02-17")
    @NotNull(message = "节日日期不能为空")
    private LocalDate holidayDate;

    @Schema(description = "模板编码", example = "crm_holiday_care")
    private String templateCode;

    @Schema(description = "状态 0-启用 1-禁用", example = "0")
    private Integer status;

    @Schema(description = "备注", example = "春节")
    private String remark;

}
