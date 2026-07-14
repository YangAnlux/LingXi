// 23软4胡伟-202305566535-修改于2026.07.14
// [ADD START] 发票分页请求VO - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
package com.meession.etm.module.crm.controller.admin.invoice.vo;

import com.meession.etm.framework.common.pojo.PageParam;
import com.meession.etm.framework.common.validation.InEnum;
import com.meession.etm.module.crm.enums.invoice.CrmInvoiceStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - CRM 发票分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmInvoicePageReqVO extends PageParam {

    @Schema(description = "发票号码")
    private String invoiceNo;

    @Schema(description = "客户编号", example = "2")
    private Long customerId;

    @Schema(description = "合同编号", example = "1")
    private Long contractId;

    @Schema(description = "发票状态", example = "0")
    @InEnum(CrmInvoiceStatusEnum.class)
    private Integer status;

    @Schema(description = "发票类型", example = "1")
    private Integer type;

}
// [ADD END] 发票分页请求VO - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
