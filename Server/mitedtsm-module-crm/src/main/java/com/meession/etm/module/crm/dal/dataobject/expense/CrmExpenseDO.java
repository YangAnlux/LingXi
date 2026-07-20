// 23软4胡伟-202305566535-修改于2026.07.16
// [ADD START] 费用DO字段定义 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
package com.meession.etm.module.crm.dal.dataobject.expense;

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
 * CRM 费用 DO
 *
 * @author 23软4胡伟-202305566535-修改于2026.07.16
 */
@TableName("crm_expense")
@KeySequence("crm_expense_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmExpenseDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 费用编号（自动生成）
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
     * 费用类型
     *
     * 枚举 {@link com.meession.etm.module.crm.enums.expense.CrmExpenseTypeEnum}
     */
    private Integer type;
    /**
     * 费用金额
     */
    private BigDecimal amount;
    /**
     * 费用日期
     */
    private LocalDate expenseDate;
    /**
     * 审批状态（预留BPM）
     *
     * 枚举 {@link com.meession.etm.module.crm.enums.common.CrmAuditStatusEnum}
     */
    private Integer auditStatus;
    /**
     * 工作流编号（预留BPM）
     */
    private String processInstanceId;
    /**
     * 备注
     */
    private String remark;

}
// [ADD END] 费用DO字段定义 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
