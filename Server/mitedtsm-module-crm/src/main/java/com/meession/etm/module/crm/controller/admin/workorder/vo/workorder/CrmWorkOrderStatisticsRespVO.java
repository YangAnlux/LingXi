// 23软件工程4班蔡磊202305566515
package com.meession.etm.module.crm.controller.admin.workorder.vo.workorder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 工单统计 Response VO")
@Data
public class CrmWorkOrderStatisticsRespVO {

    @Schema(description = "维度名称（类型名/状态名/处理人名）", requiredMode = Schema.RequiredMode.REQUIRED, example = "技术服务")
    private String name;

    @Schema(description = "工单数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "15")
    private Long count;

}
