package com.meession.etm.module.crm.dal.dataobject.send;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.time.LocalDateTime;

/**
 * CRM 发送任务 DO
 * 记录每次群发/关怀任务的元信息
 */
@TableName(value = "crm_send_task")
@KeySequence("crm_send_task_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmSendTaskDO extends BaseDO {

    @TableId
    private Long id;

    /** 关联营销活动编号 */
    private Long activityId;

    /** 任务类型: MARKETING / AUTO_CARE */
    private String taskType;

    /** 模板ID (system_notify_template.id) */
    private Long templateId;

    /** 模板编码 */
    private String templateCode;

    /** 渠道: 1-短信 2-邮件 */
    private Integer channel;

    /** 客户选择模式: ALL_ACTIVE / CSV / BY_TAG */
    private String customerSelectMode;

    /** 目标客户数 */
    private Integer customerCount;

    /** 发送模式: IMMEDIATE / SCHEDULED */
    private String sendMode;

    /** 定时发送时间（sendMode=SCHEDULED 时必填） */
    private LocalDateTime scheduledTime;

    /** 状态: PENDING / RUNNING / COMPLETED / FAILED */
    private String status;

    /** 缺失变量的客户数 */
    private Integer missingVarCount;

}
