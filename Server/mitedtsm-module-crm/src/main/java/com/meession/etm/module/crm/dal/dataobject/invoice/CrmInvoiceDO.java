// 23软4胡伟-202305566535-修改于2026.07.14
// [ADD START] 发票DO字段定义 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
package com.meession.etm.module.crm.dal.dataobject.invoice;

import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import com.meession.etm.module.crm.dal.dataobject.contract.CrmContractDO;
import com.meession.etm.module.crm.dal.dataobject.customer.CrmCustomerDO;
import com.meession.etm.module.crm.enums.invoice.CrmInvoiceStatusEnum;
import com.meession.etm.module.crm.enums.invoice.CrmInvoiceTypeEnum;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * CRM 发票 DO
 *
 * @author 23软4胡伟-202305566535-修改于2026.07.14
 */
@TableName("crm_invoice")
@KeySequence("crm_invoice_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmInvoiceDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 发票编号（自动生成）
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
     * 发票号码
     */
    private String invoiceNo;
    /**
     * 发票类型
     *
     * 枚举 {@link CrmInvoiceTypeEnum}
     */
    private Integer type;
    /**
     * 开票金额
     */
    private BigDecimal amount;
    /**
     * 发票抬头
     */
    private String title;
    /**
     * 税号
     */
    private String taxNo;
    /**
     * 发票状态
     *
     * 枚举 {@link CrmInvoiceStatusEnum}
     */
    private Integer status;
    /**
     * 开票日期
     */
    private LocalDate issueDate;
    /**
     * 工作流编号（预留BPM）
     */
    private String processInstanceId;
    /**
     * 审批状态（预留BPM）
     */
    private Integer auditStatus;
    /**
     * 备注
     */
    private String remark;

}
// [ADD END] 发票DO字段定义 - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
