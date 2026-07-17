package com.meession.etm.module.crm.dal.dataobject.campaign;

import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import com.meession.etm.module.crm.enums.DictTypeConstants;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName(value = "crm_campaign")
@KeySequence("crm_campaign_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmCampaignDO extends BaseDO {

    @TableId
    private Long id;

    private String name;

    private Integer status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private BigDecimal budget;

    private BigDecimal actualCost;

    private Integer channel;

    private String description;

    private Long ownerUserId;

    private String remark;

}
