package com.meession.etm.module.crm.controller.admin.care.vo;

import com.meession.etm.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 关怀规则分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmCareRulePageReqVO extends PageParam {

    @Schema(description = "触发类型", example = "BIRTHDAY")
    private String triggerType;

    @Schema(description = "是否启用 0-停用 1-启用", example = "1")
    private Integer isEnabled;

}
