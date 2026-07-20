// 23软4胡伟-202305566535-修改于2026.07.16
// [ADD START] 费用Service接口 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
package com.meession.etm.module.crm.service.expense;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.expense.vo.CrmExpensePageReqVO;
import com.meession.etm.module.crm.controller.admin.expense.vo.CrmExpenseSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.expense.CrmExpenseDO;
import jakarta.validation.Valid;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.meession.etm.framework.common.util.collection.CollectionUtils.convertMap;

/**
 * CRM 费用 Service 接口
 *
 * @author 23软4胡伟-202305566535-修改于2026.07.16
 */
public interface CrmExpenseService {

    /**
     * 创建费用
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createExpense(@Valid CrmExpenseSaveReqVO createReqVO);

    /**
     * 更新费用
     *
     * @param updateReqVO 更新信息
     */
    void updateExpense(@Valid CrmExpenseSaveReqVO updateReqVO);

    /**
     * 删除费用
     *
     * @param id 编号
     */
    void deleteExpense(Long id);

    /**
     * 获得费用
     *
     * @param id 编号
     * @return 费用
     */
    CrmExpenseDO getExpense(Long id);

    /**
     * 获得费用列表
     *
     * @param ids 编号
     * @return 费用列表
     */
    List<CrmExpenseDO> getExpenseList(Collection<Long> ids);

    /**
     * 获得费用 Map
     *
     * @param ids 编号
     * @return 费用 Map
     */
    default Map<Long, CrmExpenseDO> getExpenseMap(Collection<Long> ids) {
        return convertMap(getExpenseList(ids), CrmExpenseDO::getId);
    }

    /**
     * 获得费用分页
     *
     * @param pageReqVO 分页查询
     * @return 费用分页
     */
    PageResult<CrmExpenseDO> getExpensePage(CrmExpensePageReqVO pageReqVO);

}
// [ADD END] 费用Service接口 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
