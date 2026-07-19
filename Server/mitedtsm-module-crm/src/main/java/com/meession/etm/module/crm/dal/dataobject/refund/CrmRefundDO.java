// 23软4胡伟-202305566535-修改于2026.07.17
// [ADD START] 退款DO字段定义 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
package com.meession.etm.module.crm.dal.dataobject.refund;

import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import com.meession.etm.module.crm.dal.dataobject.contract.CrmContractDO;
import com.meession.etm.module.crm.dal.dataobject.customer.CrmCustomerDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * CRM 退款 DO
 *
 * @author 23软4胡伟-202305566535-修改于2026.07.17
 */
@TableName("crm_refund")
@KeySequence("crm_refund_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmRefundDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 退款编号（自动生成）
     */
    private String no;
    /**
     * 客户编号
     *
     * 关联 {@link CrmCustomerDO#getId()}
     */
    private Long customerId;
    /**
     * 合同编号
     *
     * 关联 {@link CrmContractDO#getId()}
     */
    private Long contractId;
    /**
     * 关联订单ID（预留）
     */
    private Long orderId;
    /**
     * 负责人编号
     */
    private Long ownerUserId;
    /**
     * 退款金额
     */
    private BigDecimal refundAmount;
    /**
     * 退款日期
     */
    private LocalDate refundDate;
    /**
     * 退款原因
     */
    private String refundReason;
    /**
     * 退款类型
     *
     * 枚举 {@link com.meession.etm.module.crm.enums.refund.CrmRefundTypeEnum}
     */
    private Integer refundType;
    /**
     * 工作流编号（BPM 关联）
     */
    private String processInstanceId;
    /**
     * 退款状态（0=待提交 1=审批中 2=已通过 3=已驳回）
     */
    private Integer status;
    /**
     * 审批状态（BPM 关联）
     *
     * 枚举 {@link com.meession.etm.module.crm.enums.common.CrmAuditStatusEnum}
     */
    private Integer auditStatus;
    /**
     * 备注
     */
    private String remark;

}
// [ADD END] 退款DO字段定义 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
