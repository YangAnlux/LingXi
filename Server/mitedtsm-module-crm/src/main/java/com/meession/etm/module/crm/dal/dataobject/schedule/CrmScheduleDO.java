package com.meession.etm.module.crm.dal.dataobject.schedule;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.time.LocalDateTime;

@TableName(value = "crm_schedule")
@KeySequence("crm_schedule_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmScheduleDO extends BaseDO {

    @TableId
    private Long id;

    private String title;

    private String description;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Boolean allDay;

    private String color;

    private Integer scheduleType;

    private Integer status;

    private Long ownerUserId;

    private Long relatedTaskId;

    private String remark;

}
