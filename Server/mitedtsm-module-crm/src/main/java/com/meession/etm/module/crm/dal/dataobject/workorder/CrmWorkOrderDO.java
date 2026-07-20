package com.meession.etm.module.crm.dal.dataobject.workorder;

import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName(value = "crm_work_order", autoResultMap = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmWorkOrderDO extends BaseDO {

    @TableId
    private Long id;

    private Long tenantId;

    private String title;

    private String type;

    private String priority;

    private String status;

    private Long customerId;

    private Long assigneeId;

    private LocalDateTime slaDeadline;

    private Integer isSlaBreached;

    private String content;

    private String solution;

    private LocalDateTime resolvedTime;

}
