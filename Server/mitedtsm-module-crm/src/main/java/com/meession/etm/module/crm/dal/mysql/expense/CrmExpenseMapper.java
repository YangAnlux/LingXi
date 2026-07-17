// 23软4胡伟-202305566535-修改于2026.07.16
// [ADD START] 费用Mapper - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
package com.meession.etm.module.crm.dal.mysql.expense;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.meession.etm.module.crm.controller.admin.expense.vo.CrmExpensePageReqVO;
import com.meession.etm.module.crm.dal.dataobject.expense.CrmExpenseDO;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

/**
 * 费用 Mapper
 *
 * @author 23软4胡伟-202305566535-修改于2026.07.16
 */
@Mapper
public interface CrmExpenseMapper extends BaseMapperX<CrmExpenseDO> {

    default CrmExpenseDO selectByNo(String no) {
        return selectOne(CrmExpenseDO::getNo, no);
    }

    default PageResult<CrmExpenseDO> selectPage(CrmExpensePageReqVO pageReqVO) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<CrmExpenseDO>()
                .eqIfPresent(CrmExpenseDO::getNo, pageReqVO.getNo())
                .eqIfPresent(CrmExpenseDO::getCustomerId, pageReqVO.getCustomerId())
                .eqIfPresent(CrmExpenseDO::getContractId, pageReqVO.getContractId())
                .eqIfPresent(CrmExpenseDO::getType, pageReqVO.getType())
                .orderByDesc(CrmExpenseDO::getId));
    }

    default List<CrmExpenseDO> selectListByContractId(Long contractId) {
        return selectList(CrmExpenseDO::getContractId, contractId);
    }

    default BigDecimal selectSumAmountByContractId(Long contractId, Collection<Long> excludeIds) {
        List<CrmExpenseDO> expenses = selectList(new LambdaQueryWrapperX<CrmExpenseDO>()
                .eq(CrmExpenseDO::getContractId, contractId));
        if (excludeIds != null && !excludeIds.isEmpty()) {
            expenses = expenses.stream()
                    .filter(e -> !excludeIds.contains(e.getId()))
                    .collect(java.util.stream.Collectors.toList());
        }
        return expenses.stream()
                .map(CrmExpenseDO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
// [ADD END] 费用Mapper - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
