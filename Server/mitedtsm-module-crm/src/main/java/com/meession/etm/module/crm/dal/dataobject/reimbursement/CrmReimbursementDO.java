// 23软4胡伟-202305566535-修改于2026.07.16
// [ADD START] 报销DO字段定义 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
package com.meession.etm.module.crm.dal.dataobject.reimbursement;

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
 * CRM 报销 DO
 *
 * @author 23软4胡伟-202305566535-修改于2026.07.16
 */
@TableName("crm_reimbursement")
@KeySequence("crm_reimbursement_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmReimbursementDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 报销编号（自动生成）
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
     * 负责人编号
     */
    private Long ownerUserId;
    /**
     * 报销日期
     */
    private LocalDate reimbursementDate;
    /**
     * 报销总金额
     */
    private BigDecimal totalAmount;
    /**
     * 报销类型
     *
     * 枚举 {@link com.meession.etm.module.crm.enums.expense.CrmExpenseTypeEnum}
     */
    private Integer type;
    /**
     * 报销状态（0=待提交 1=审批中 2=已通过 3=已驳回）
     */
    private Integer status;
    /**
     * 工作流编号（BPM 关联）
     */
    private String processInstanceId;
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
// [ADD END] 报销DO字段定义 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
