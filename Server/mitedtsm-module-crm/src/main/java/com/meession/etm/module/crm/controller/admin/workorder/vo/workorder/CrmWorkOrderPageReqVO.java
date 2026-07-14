package com.meession.etm.module.crm.controller.admin.workorder.vo.workorder;

import com.meession.etm.framework.common.pojo.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CrmWorkOrderPageReqVO extends PageParam {

    private String title;

    private String type;

    private String priority;

    private String status;

    private Long customerId;

    private Long assigneeId;

    private LocalDateTime createTime;

}