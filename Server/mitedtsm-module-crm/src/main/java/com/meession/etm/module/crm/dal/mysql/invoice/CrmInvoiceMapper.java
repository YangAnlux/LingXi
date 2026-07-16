// 23软4胡伟-202305566535-修改于2026.07.14
// [ADD START] 发票Mapper - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
package com.meession.etm.module.crm.dal.mysql.invoice;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.meession.etm.module.crm.controller.admin.invoice.vo.CrmInvoicePageReqVO;
import com.meession.etm.module.crm.dal.dataobject.invoice.CrmInvoiceDO;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * 发票 Mapper
 *
 * @author 23软4胡伟-202305566535-修改于2026.07.14
 */
@Mapper
public interface CrmInvoiceMapper extends BaseMapperX<CrmInvoiceDO> {

    default CrmInvoiceDO selectByInvoiceNo(String invoiceNo) {
        return selectOne(CrmInvoiceDO::getInvoiceNo, invoiceNo);
    }

    default CrmInvoiceDO selectByNo(String no) {
        return selectOne(CrmInvoiceDO::getNo, no);
    }

    default PageResult<CrmInvoiceDO> selectPage(CrmInvoicePageReqVO pageReqVO) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<CrmInvoiceDO>()
                .eqIfPresent(CrmInvoiceDO::getNo, pageReqVO.getNo())
                .eqIfPresent(CrmInvoiceDO::getInvoiceNo, pageReqVO.getInvoiceNo())
                .eqIfPresent(CrmInvoiceDO::getCustomerId, pageReqVO.getCustomerId())
                .eqIfPresent(CrmInvoiceDO::getContractId, pageReqVO.getContractId())
                .eqIfPresent(CrmInvoiceDO::getStatus, pageReqVO.getStatus())
                .eqIfPresent(CrmInvoiceDO::getType, pageReqVO.getType())
                .orderByDesc(CrmInvoiceDO::getId));
    }

    default List<CrmInvoiceDO> selectListByContractIdAndStatus(Long contractId, List<Integer> statusList) {
        return selectList(new LambdaQueryWrapperX<CrmInvoiceDO>()
                .eq(CrmInvoiceDO::getContractId, contractId)
                .inIfPresent(CrmInvoiceDO::getStatus, statusList));
    }

    default List<CrmInvoiceDO> selectListByContractId(Long contractId) {
        return selectList(CrmInvoiceDO::getContractId, contractId);
    }

    default BigDecimal selectSumAmountByContractId(Long contractId, Collection<Long> excludeIds) {
        List<CrmInvoiceDO> invoices = selectList(new LambdaQueryWrapperX<CrmInvoiceDO>()
                .eq(CrmInvoiceDO::getContractId, contractId)
                .ne(CrmInvoiceDO::getStatus, 2));  // 排除已作废
        if (excludeIds != null && !excludeIds.isEmpty()) {
            invoices = invoices.stream()
                    .filter(i -> !excludeIds.contains(i.getId()))
                    .collect(java.util.stream.Collectors.toList());
        }
        return invoices.stream()
                .map(CrmInvoiceDO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
// [ADD END] 发票Mapper - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
