package com.meession.etm.module.crm.controller.admin.care.vo.holiday;

import com.meession.etm.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Schema(description = "管理后台 - 节日配置分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmHolidayPageReqVO extends PageParam {

    @Schema(description = "节日名称", example = "春节")
    private String name;

    @Schema(description = "节日日期", example = "2026-02-17")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate holidayDate;

    @Schema(description = "状态 0-启用 1-禁用", example = "0")
    private Integer status;

}
