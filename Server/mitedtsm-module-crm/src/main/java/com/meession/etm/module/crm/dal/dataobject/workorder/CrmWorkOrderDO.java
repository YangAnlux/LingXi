// 2023级软4蔡磊202305566515,2026年7月14日
package com.meession.etm.module.crm.dal.dataobject.workorder;

import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * CRM 工单 DO
 *
 * @author CRM Team
 */
@TableName("crm_work_order")
@KeySequence("crm_work_order_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmWorkOrderDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;

    /**
     * 工单标题
     */
    private String title;

    /**
     * 工单类型（字典）
     */
    private String type;

    /**
     * 优先级（LOW/NORMAL/HIGH/URGENT）
     */
    private String priority;

    /**
     * 状态（待处理/处理中/已完结/已退回）
     */
    private String status;

    /**
     * 关联客户编号
     */
    private Long customerId;

    /**
     * 处理人编号
     */
    private Long assigneeId;

    /**
     * SLA 截止时间
     */
    private LocalDateTime slaDeadline;

    /**
     * 是否超SLA
     */
    private Boolean isSlaBreached;

    /**
     * 工单内容
     */
    private String content;

    /**
     * 解决方案
     */
    private String solution;

    /**
     * 完结时间
     */
    private LocalDateTime resolvedTime;

    /**
     * 满意度评分（1-5）
     */
    // 23软件工程4班蔡磊202305566515
    private Integer satisfactionScore;

    /**
     * 满意度评价内容
     */
    // 23软件工程4班蔡磊202305566515
    private String satisfactionComment;

}
