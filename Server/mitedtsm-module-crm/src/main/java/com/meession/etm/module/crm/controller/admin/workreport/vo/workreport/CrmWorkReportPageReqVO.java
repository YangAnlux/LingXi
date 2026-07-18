package com.meession.etm.module.crm.controller.admin.workreport.vo.workreport;

import com.meession.etm.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Schema(description = "管理后台 - 工作报告分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmWorkReportPageReqVO extends PageParam {

    @Schema(description = "报告标题", example = "工作总结")
    private String title;

    @Schema(description = "审批状态", example = "0")
    private Integer status;

    @Schema(description = "报告类型", example = "2")
    private Integer reportType;

    @Schema(description = "提交人用户编号", example = "1")
    private Long ownerUserId;

    @Schema(description = "报告日期开始", example = "2024-01-01")
    private LocalDate reportDateStart;

    @Schema(description = "报告日期结束", example = "2024-12-31")
    private LocalDate reportDateEnd;

}
