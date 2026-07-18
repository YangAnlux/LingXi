package com.meession.etm.module.crm.dal.dataobject.campaign;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.time.LocalDateTime;

@TableName(value = "crm_campaign_send_log")
@KeySequence("crm_campaign_send_log_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmCampaignSendLogDO extends BaseDO {

    @TableId
    private Long id;

    private Long campaignId;

    /** 发送渠道 1-短信 2-邮件 */
    private Integer channel;

    private String templateCode;

    private Integer totalCount;

    private Integer successCount;

    private Integer failCount;

    /** 邮件打开次数（邮件跟踪回写） */
    private Integer openCount;

    /** 邮件点击次数（邮件跟踪回写） */
    private Integer clickCount;

    private LocalDateTime sendTime;

    /** 邮件打开时间（追踪像素回调） */
    private LocalDateTime openTime;

    /** 邮件点击时间（追踪链接回调） */
    private LocalDateTime clickTime;

    private String remark;

}
