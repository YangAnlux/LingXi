package com.meession.etm.module.crm.controller.admin.statistics.vo.funnel;

import com.meession.etm.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "管理后台 - CRM 销售漏斗分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class CrmStatisticsFunnelReqVO extends PageParam {

    @Schema(description = "部门 id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long deptId;

    @Schema(description = "负责人用户 id", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1")
    private Long userId;

    @Schema(description = "负责人用户 id 集合", hidden = true, example = "2")
    private java.util.List<Long> userIds;

    @Schema(description = "时间间隔类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer interval;

    @Schema(description = "时间范围", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private java.time.LocalDateTime[] times;

}
