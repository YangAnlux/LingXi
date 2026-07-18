package com.meession.etm.module.crm.dal.dataobject.care;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.time.LocalDate;

/**
 * CRM 节日配置 DO
 *
 * 用于配置需要发送关怀的节日日期
 */
@TableName(value = "crm_holiday")
@KeySequence("crm_holiday_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmHolidayDO extends BaseDO {

    @TableId
    private Long id;

    /** 节日名称 */
    private String name;

    /** 节日日期 */
    private LocalDate holidayDate;

    /** 模板编码 */
    private String templateCode;

    /** 状态 0-启用 1-禁用 */
    private Integer status;

    /** 备注 */
    private String remark;

}
