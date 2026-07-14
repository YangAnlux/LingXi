// 2023级软4蔡磊202305566515,2026年7月14日
package com.meession.etm.module.crm.controller.admin.workorder.vo.workorder;

import com.meession.etm.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 工单分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmWorkOrderPageReqVO extends PageParam {

    @Schema(description = "工单标题", example = "服务请求")
    private String title;

    @Schema(description = "工单类型", example = "技术服务")
    private String type;

    @Schema(description = "优先级", example = "NORMAL")
    private String priority;

    @Schema(description = "状态", example = "待处理")
    private String status;

    @Schema(description = "客户编号", example = "1024")
    private Long customerId;

    @Schema(description = "处理人编号", example = "10001")
    private Long assigneeId;

}
