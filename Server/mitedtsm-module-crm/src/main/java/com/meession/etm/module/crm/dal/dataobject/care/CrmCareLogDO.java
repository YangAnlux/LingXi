package com.meession.etm.module.crm.dal.dataobject.care;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.time.LocalDateTime;

@TableName(value = "crm_care_log")
@KeySequence("crm_care_log_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmCareLogDO extends BaseDO {

    @TableId
    private Long id;

    private Long customerId;

    private Long campaignId;

    /** 关怀类型 1-生日关怀 2-节日关怀 3-手动关怀 */
    private Integer careType;

    /** 发送渠道 1-短信 2-邮件 */
    private Integer channel;

    private String templateCode;

    private String content;

    /** 状态 0-成功 1-失败 */
    private Integer status;

    private LocalDateTime sendTime;

    private String remark;

}
