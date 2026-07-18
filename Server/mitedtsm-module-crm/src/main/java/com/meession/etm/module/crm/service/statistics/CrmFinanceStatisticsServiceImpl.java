// 23软4胡伟-202305566535-修改于2026.07.17
// [ADD START] 财务汇总Service实现 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
package com.meession.etm.module.crm.service.statistics;

import cn.hutool.core.collection.CollUtil;
import com.meession.etm.module.crm.controller.admin.statistics.vo.finance.CrmFinanceStatisticsRespVO;
import com.meession.etm.module.crm.dal.dataobject.expense.CrmExpenseDO;
import com.meession.etm.module.crm.dal.dataobject.invoice.CrmInvoiceDO;
import com.meession.etm.module.crm.dal.dataobject.receivable.CrmReceivableDO;
import com.meession.etm.module.crm.dal.dataobject.refund.CrmRefundDO;
import com.meession.etm.module.crm.dal.dataobject.reimbursement.CrmReimbursementDO;
import com.meession.etm.module.crm.dal.mysql.expense.CrmExpenseMapper;
import com.meession.etm.module.crm.dal.mysql.invoice.CrmInvoiceMapper;
import com.meession.etm.module.crm.dal.mysql.receivable.CrmReceivableMapper;
import com.meession.etm.module.crm.dal.mysql.refund.CrmRefundMapper;
import com.meession.etm.module.crm.dal.mysql.reimbursement.CrmReimbursementMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.List;

/**
 * CRM 财务统计 Service 实现类
 *
 * @author 23软4胡伟-202305566535-修改于2026.07.17
 */
@Service
@Validated
public class CrmFinanceStatisticsServiceImpl implements CrmFinanceStatisticsService {

    @Resource
    private CrmReceivableMapper receivableMapper;

    @Resource
    private CrmInvoiceMapper invoiceMapper;

    @Resource
    private CrmExpenseMapper expenseMapper;

    @Resource
    private CrmReimbursementMapper reimbursementMapper;

    @Resource
    private CrmRefundMapper refundMapper;

    @Override
    public CrmFinanceStatisticsRespVO getFinanceSummary() {
        CrmFinanceStatisticsRespVO vo = new CrmFinanceStatisticsRespVO();

        // 1. 回款统计
        List<CrmReceivableDO> receivableList = receivableMapper.selectList();
        if (CollUtil.isNotEmpty(receivableList)) {
            vo.setReceivableCount((long) receivableList.size());
            vo.setReceivableTotal(
                    receivableList.stream()
                            .map(CrmReceivableDO::getPrice)
                            .filter(java.util.Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add));
        } else {
            vo.setReceivableCount(0L);
            vo.setReceivableTotal(BigDecimal.ZERO);
        }

        // 2. 发票统计
        List<CrmInvoiceDO> invoiceList = invoiceMapper.selectList();
        if (CollUtil.isNotEmpty(invoiceList)) {
            vo.setInvoiceCount((long) invoiceList.size());
            vo.setInvoiceTotal(
                    invoiceList.stream()
                            .map(CrmInvoiceDO::getAmount)
                            .filter(java.util.Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add));
        } else {
            vo.setInvoiceCount(0L);
            vo.setInvoiceTotal(BigDecimal.ZERO);
        }

        // 3. 费用统计
        List<CrmExpenseDO> expenseList = expenseMapper.selectList();
        if (CollUtil.isNotEmpty(expenseList)) {
            vo.setExpenseCount((long) expenseList.size());
            vo.setExpenseTotal(
                    expenseList.stream()
                            .map(CrmExpenseDO::getAmount)
                            .filter(java.util.Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add));
        } else {
            vo.setExpenseCount(0L);
            vo.setExpenseTotal(BigDecimal.ZERO);
        }

        // 4. 报销统计
        List<CrmReimbursementDO> reimbursementList = reimbursementMapper.selectList();
        if (CollUtil.isNotEmpty(reimbursementList)) {
            vo.setReimbursementCount((long) reimbursementList.size());
            vo.setReimbursementTotal(
                    reimbursementList.stream()
                            .map(CrmReimbursementDO::getTotalAmount)
                            .filter(java.util.Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add));
        } else {
            vo.setReimbursementCount(0L);
            vo.setReimbursementTotal(BigDecimal.ZERO);
        }

        // 5. 退款统计
        List<CrmRefundDO> refundList = refundMapper.selectList();
        if (CollUtil.isNotEmpty(refundList)) {
            vo.setRefundCount((long) refundList.size());
            vo.setRefundTotal(
                    refundList.stream()
                            .map(CrmRefundDO::getRefundAmount)
                            .filter(java.util.Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add));
        } else {
            vo.setRefundCount(0L);
            vo.setRefundTotal(BigDecimal.ZERO);
        }

        return vo;
    }

}
// [ADD END] 财务汇总Service实现 - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
