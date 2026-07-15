package com.meession.etm.module.promotion.dal.dataobject.campaign;

import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 营销活动 DO
 *
 * @author 密讯
 */
@TableName(value = "promotion_campaign", autoResultMap = true)
@KeySequence("promotion_campaign_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
public class CampaignDO extends BaseDO {

    /**
     * 活动编号，主键自增
     */
    @TableId
    private Long id;
    /**
     * 活动名称
     */
    private String name;
    /**
     * 活动状态
     *
     * 枚举 {@link com.meession.etm.module.promotion.enums.common.PromotionActivityStatusEnum}
     *
     * 10: 未开始, 20: 进行中, 30: 已结束, 40: 已关闭
     */
    private Integer status;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 排序
     */
    private Integer sort;

}
