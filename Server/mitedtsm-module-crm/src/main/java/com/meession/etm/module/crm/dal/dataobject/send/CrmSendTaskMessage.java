package com.meession.etm.module.crm.dal.dataobject.send;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * RabbitMQ 消息体
 * 投递到 sms.queue / email.queue 时序列化为 JSON
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmSendTaskMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 任务ID */
    private Long taskId;

    /** 关联活动ID */
    private Long activityId;

    /** 业务类型: MARKETING / AUTO_CARE */
    private String bizType;

    /** 渠道: 1-SMS 2-EMAIL */
    private Integer channel;

    /** 模板编码 */
    private String templateCode;

    /** 模板内容（已替换变量） */
    private String templateContent;

    /** 客户列表: [{customerId, mobile/email, name, ...}] */
    private List<Map<String, Object>> customers;

    /** 租户ID */
    private Long tenantId;

    /** 操作人ID */
    private Long userId;

}
