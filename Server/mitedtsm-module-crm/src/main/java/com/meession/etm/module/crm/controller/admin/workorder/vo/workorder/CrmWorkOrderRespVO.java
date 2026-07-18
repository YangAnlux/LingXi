package com.meession.etm.module.crm.controller.admin.workorder.vo.workorder;

import com.meession.etm.framework.common.pojo.CommonResult;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CrmWorkOrderRespVO {

    private Long id;

    private String title;

    private String type;

    private String priority;

    private String status;

    private Long customerId;

    private String customerName;

    private Long assigneeId;

    private String assigneeName;

    private LocalDateTime slaDeadline;

    private Integer isSlaBreached;

    private String content;

    private String solution;

    private LocalDateTime resolvedTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}