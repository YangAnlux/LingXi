package com.meession.etm.module.crm.dal.dataobject.workreport;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName(value = "crm_work_report")
@KeySequence("crm_work_report_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmWorkReportDO extends BaseDO {

    @TableId
    private Long id;

    private String title;

    private String content;

    private LocalDate reportDate;

    private Integer reportType;

    private Integer status;

    private Long ownerUserId;

    private Long auditUserId;

    private LocalDateTime auditTime;

    private String auditRemark;

    private Long relatedCampaignId;

    private Long relatedCustomerId;

    private String remark;

}
