package com.meession.etm.module.crm.controller.admin.campaign.vo.campaign;

import com.meession.etm.framework.excel.core.annotations.DictFormat;
import com.meession.etm.framework.excel.core.convert.DictConvert;
import com.meession.etm.framework.excel.core.convert.LocalDateTimeConverter;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 营销活动 Response VO")
@Data
@ToString(callSuper = true)
@ExcelIgnoreUnannotated
public class CrmCampaignRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "活动名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "夏季促销活动")
    @ExcelProperty("活动名称")
    private String name;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat(com.meession.etm.module.crm.enums.DictTypeConstants.CRM_CAMPAIGN_STATUS)
    private Integer status;

    @Schema(description = "开始时间", example = "2023-01-01 00:00:00")
    @ExcelProperty(value = "开始时间", converter = LocalDateTimeConverter.class)
    private LocalDateTime startTime;

    @Schema(description = "结束时间", example = "2023-01-31 23:59:59")
    @ExcelProperty(value = "结束时间", converter = LocalDateTimeConverter.class)
    private LocalDateTime endTime;

    @Schema(description = "活动预算(元)", example = "10000.00")
    @ExcelProperty("活动预算(元)")
    private BigDecimal budget;

    @Schema(description = "实际花费(元)", example = "8000.00")
    @ExcelProperty("实际花费(元)")
    private BigDecimal actualCost;

    @Schema(description = "渠道类型", example = "1")
    @ExcelProperty(value = "渠道类型", converter = DictConvert.class)
    @DictFormat(com.meession.etm.module.crm.enums.DictTypeConstants.CRM_CAMPAIGN_CHANNEL)
    private Integer channel;

    @Schema(description = "活动描述", example = "活动描述内容")
    @ExcelProperty("活动描述")
    private String description;

    @Schema(description = "负责人用户编号", example = "1")
    private Long ownerUserId;

    @Schema(description = "负责人名字", example = "张三")
    @ExcelProperty("负责人名字")
    private String ownerUserName;

    @Schema(description = "备注", example = "备注内容")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建人", example = "1")
    @ExcelProperty("创建人")
    private String creator;

    @Schema(description = "创建人名字", example = "李四")
    @ExcelProperty("创建人名字")
    private String creatorName;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "创建时间", converter = LocalDateTimeConverter.class)
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "更新时间", converter = LocalDateTimeConverter.class)
    private LocalDateTime updateTime;

    @Schema(description = "关联发送任务数")
    private Integer sendTaskCount;

}
