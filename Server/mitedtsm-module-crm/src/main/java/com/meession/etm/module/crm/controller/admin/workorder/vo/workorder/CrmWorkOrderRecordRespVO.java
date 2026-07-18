// 2023级软4蔡磊202305566515,2026年7月14日
package com.meession.etm.module.crm.controller.admin.workorder.vo.workorder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 工单处理记录 Response VO")
@Data
public class CrmWorkOrderRecordRespVO {

    @Schema(description = "编号", example = "1")
    private Long id;

    @Schema(description = "工单编号", example = "1")
    private Long workOrderId;

    @Schema(description = "变更前状态", example = "待处理")
    private String fromStatus;

    @Schema(description = "变更后状态", example = "处理中")
    private String toStatus;

    @Schema(description = "创建人", example = "admin")
    private String creator;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
