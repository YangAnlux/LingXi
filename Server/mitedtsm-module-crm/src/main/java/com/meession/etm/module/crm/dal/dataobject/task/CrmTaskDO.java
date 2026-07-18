package com.meession.etm.module.crm.dal.dataobject.task;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.time.LocalDateTime;

@TableName(value = "crm_task")
@KeySequence("crm_task_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmTaskDO extends BaseDO {

    @TableId
    private Long id;

    private String name;

    private String description;

    private Integer status;

    private Integer priority;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime completedTime;

    private Long ownerUserId;

    private Long assignerUserId;

    private Long relatedCampaignId;

    private Long relatedCustomerId;

    private Long relatedBusinessId;

    private String remark;

}
