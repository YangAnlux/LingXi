package com.meession.etm.module.crm.controller.admin.workorder.vo.workorder;

import com.meession.etm.framework.common.validation.InEnum;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class CrmWorkOrderSaveReqVO {

    private Long id;

    @NotBlank(message = "工单标题不能为空")
    private String title;

    private String type;

    private String priority;

    private Long customerId;

    private Long assigneeId;

    private LocalDateTime slaDeadline;

    @NotBlank(message = "工单内容不能为空")
    private String content;

    private String solution;

}