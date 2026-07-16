// 23软4胡伟-202305566535-修改于2026.07.16
// [ADD START] 费用Service实现 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
package com.meession.etm.module.crm.service.expense;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.controller.admin.expense.vo.CrmExpensePageReqVO;
import com.meession.etm.module.crm.controller.admin.expense.vo.CrmExpenseSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.expense.CrmExpenseDO;
import com.meession.etm.module.crm.dal.mysql.expense.CrmExpenseMapper;
import com.meession.etm.module.crm.dal.redis.no.CrmNoRedisDAO;
import com.meession.etm.module.crm.enums.common.CrmAuditStatusEnum;
import com.meession.etm.module.crm.framework.permission.core.annotations.CrmPermission;
import com.meession.etm.module.crm.enums.common.CrmBizTypeEnum;
import com.meession.etm.module.crm.enums.permission.CrmPermissionLevelEnum;
import com.meession.etm.module.crm.service.permission.CrmPermissionService;
import com.meession.etm.module.crm.service.permission.bo.CrmPermissionCreateReqBO;
import com.meession.etm.module.system.api.user.AdminUserApi;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;

import static com.meession.etm.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.meession.etm.module.crm.enums.ErrorCodeConstants.*;
import static com.meession.etm.module.crm.enums.LogRecordConstants.*;

/**
 * CRM 费用 Service 实现类
 *
 * @author 23软4胡伟-202305566535-修改于2026.07.16
 */
@Service
@Validated
@Slf4j
public class CrmExpenseServiceImpl implements CrmExpenseService {

    @Resource
    private CrmExpenseMapper expenseMapper;

    @Resource
    private CrmNoRedisDAO noRedisDAO;

    @Resource
    private CrmPermissionService permissionService;
    @Resource
    private AdminUserApi adminUserApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_EXPENSE_TYPE, subType = CRM_EXPENSE_CREATE_SUB_TYPE, bizNo = "{{#expense.id}}",
            success = CRM_EXPENSE_CREATE_SUCCESS)
    public Long createExpense(CrmExpenseSaveReqVO createReqVO) {
        // 1.1 生成费用编号
        String no = noRedisDAO.generate(CrmNoRedisDAO.EXPENSE_PREFIX);
        if (expenseMapper.selectByNo(no) != null) {
            throw exception(EXPENSE_NO_EXISTS);
        }
        // 1.2 插入费用
        CrmExpenseDO expense = BeanUtils.toBean(createReqVO, CrmExpenseDO.class);
        expense.setNo(no);
        expense.setAuditStatus(CrmAuditStatusEnum.DRAFT.getStatus());
        expenseMapper.insert(expense);
        // 1.3 创建数据权限
        permissionService.createPermission(
                new CrmPermissionCreateReqBO().setBizType(CrmBizTypeEnum.CRM_EXPENSE.getType())
                        .setBizId(expense.getId()).setUserId(createReqVO.getOwnerUserId())
                        .setLevel(CrmPermissionLevelEnum.OWNER.getLevel()));

        LogRecordContext.putVariable("expense", expense);
        return expense.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_EXPENSE_TYPE, subType = CRM_EXPENSE_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = CRM_EXPENSE_UPDATE_SUCCESS)
    public void updateExpense(CrmExpenseSaveReqVO updateReqVO) {
        // 1.1 校验存在
        CrmExpenseDO oldExpense = validateExpenseExists(updateReqVO.getId());
        // 1.2 更新费用
        CrmExpenseDO updateObj = BeanUtils.toBean(updateReqVO, CrmExpenseDO.class);
        expenseMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = CRM_EXPENSE_TYPE, subType = CRM_EXPENSE_DELETE_SUB_TYPE, bizNo = "{{#id}}",
            success = CRM_EXPENSE_DELETE_SUCCESS)
    public void deleteExpense(Long id) {
        // 校验存在
        validateExpenseExists(id);
        // 删除
        expenseMapper.deleteById(id);
    }

    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_EXPENSE, bizId = "#id", level = CrmPermissionLevelEnum.READ)
    public CrmExpenseDO getExpense(Long id) {
        return expenseMapper.selectById(id);
    }

    @Override
    public List<CrmExpenseDO> getExpenseList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return expenseMapper.selectBatchIds(ids);
    }

    @Override
    @CrmPermission(bizType = CrmBizTypeEnum.CRM_EXPENSE, bizId = "#pageReqVO.id", level = CrmPermissionLevelEnum.READ)
    public PageResult<CrmExpenseDO> getExpensePage(CrmExpensePageReqVO pageReqVO) {
        return expenseMapper.selectPage(pageReqVO);
    }

    /**
     * 校验费用是否存在
     *
     * @param id 费用编号
     * @return 费用
     */
    private CrmExpenseDO validateExpenseExists(Long id) {
        CrmExpenseDO expense = expenseMapper.selectById(id);
        if (expense == null) {
            throw exception(EXPENSE_NOT_EXISTS);
        }
        return expense;
    }

}
// [ADD END] 费用Service实现 - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
