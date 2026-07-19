// 23软4胡伟-202305566535-修改于2026.07.16
// [ADD START] 报销分页请求VO - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
package com.meession.etm.module.crm.controller.admin.reimbursement.vo;

import com.meession.etm.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - CRM 报销分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmReimbursementPageReqVO extends PageParam {

    @Schema(description = "报销编号")
    private String no;

    @Schema(description = "客户编号", example = "2")
    private Long customerId;

    @Schema(description = "合同编号", example = "1")
    private Long contractId;

    @Schema(description = "报销状态（0=待提交 1=审批中 2=已通过 3=已驳回）", example = "0")
    private Integer status;

}
// [ADD END] 报销分页请求VO - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
