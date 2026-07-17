// 23软件工程4班蔡磊202305566515
package com.meession.etm.module.crm.controller.admin.workorder.vo.workorder;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 工单满意度回访 Request VO")
@Data
public class CrmWorkOrderSatisfactionReqVO {

    @Schema(description = "工单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "工单编号不能为空")
    private Long id;

    @Schema(description = "满意度评分（1-5）", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    @NotNull(message = "满意度评分不能为空")
    @Min(value = 1, message = "评分最小为1")
    @Max(value = 5, message = "评分最大为5")
    private Integer satisfactionScore;

    @Schema(description = "满意度评价内容", example = "服务很好，响应及时")
    private String satisfactionComment;

}
