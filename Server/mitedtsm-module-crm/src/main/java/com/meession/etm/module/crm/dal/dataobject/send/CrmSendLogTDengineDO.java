package com.meession.etm.module.crm.dal.dataobject.send;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * TDengine send_logs 超级表对应的 Java Bean
 * 不映射 MySQL 表，仅用于 TDengine 数据传递
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmSendLogTDengineDO {

    /** 时间戳 */
    private LocalDateTime ts;

    /** 发送状态: SUCCESS/FAIL/OPEN/CLICK */
    private String sendStatus;

    /** 渠道: SMS/EMAIL */
    private String channel;

    /** 失败原因 */
    private String failReason;

    /** 模板编码 */
    private String templateCode;

    // ===== TAGS =====

    /** 客户编号 */
    private Long customerId;

    /** 任务编号 */
    private Long taskId;

    /** 业务类型: MARKETING/AUTO_CARE */
    private String bizType;

}
