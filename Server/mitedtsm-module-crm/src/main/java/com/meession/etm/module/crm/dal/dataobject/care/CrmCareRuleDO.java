package com.meession.etm.module.crm.dal.dataobject.care;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * CRM 自动关怀规则 DO
 */
@TableName(value = "crm_care_rule")
@KeySequence("crm_care_rule_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmCareRuleDO extends BaseDO {

    @TableId
    private Long id;

    /** 规则名称 */
    private String name;

    /** 触发类型: BIRTHDAY/HOLIDAY/MANUAL */
    private String triggerType;

    /** 提前N天触发 */
    private Integer triggerDaysBefore;

    /** 关联节日ID */
    private Long holidayId;

    /** 模板ID */
    private Long templateId;

    /** 模板编码 */
    private String templateCode;

    /** 发送渠道 1-短信 2-邮件 */
    private Integer channel;

    /** 发送窗口开始 */
    private LocalTime sendWindowStart;

    /** 发送窗口结束 */
    private LocalTime sendWindowEnd;

    /** 是否启用 0-停用 1-启用 */
    private Integer isEnabled;

    /** 最近一次执行时间 */
    private LocalDateTime lastExecTime;

    /** 备注 */
    private String remark;

}
