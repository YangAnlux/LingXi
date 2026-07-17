// 23软4胡伟-202305566535-修改于2026.07.16
// [ADD START] 费用分页请求VO - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
package com.meession.etm.module.crm.controller.admin.expense.vo;

import com.meession.etm.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static com.meession.etm.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;

@Schema(description = "管理后台 - CRM 费用分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmExpensePageReqVO extends PageParam {

    @Schema(description = "费用编号", example = "FY20260716")
    private String no;

    @Schema(description = "客户编号", example = "2")
    private Long customerId;

    @Schema(description = "合同编号", example = "1")
    private Long contractId;

    @Schema(description = "费用类型", example = "1")
    private Integer type;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    private LocalDate[] createTime;

}
// [ADD END] 费用分页请求VO - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
