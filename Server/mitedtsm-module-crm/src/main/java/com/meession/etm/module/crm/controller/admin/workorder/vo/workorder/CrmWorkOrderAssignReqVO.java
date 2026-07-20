// 23软件工程4班蔡磊202305566515
package com.meession.etm.module.crm.controller.admin.workorder.vo.workorder;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 工单分配 Request VO")
@Data
public class CrmWorkOrderAssignReqVO {

    @Schema(description = "工单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "工单编号不能为空")
    private Long id;

    @Schema(description = "处理人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "10001")
    @NotNull(message = "处理人不能为空")
    private Long assigneeId;

}
