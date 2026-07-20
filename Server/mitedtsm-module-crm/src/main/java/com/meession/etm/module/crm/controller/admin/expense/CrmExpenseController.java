// 23软4胡伟-202305566535-修改于2026.07.16
// [ADD START] 费用Controller - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
package com.meession.etm.module.crm.controller.admin.expense;

import cn.hutool.core.collection.CollUtil;
import com.meession.etm.framework.apilog.core.annotation.ApiAccessLog;
import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.framework.excel.core.util.ExcelUtils;
import com.meession.etm.module.crm.controller.admin.expense.vo.CrmExpensePageReqVO;
import com.meession.etm.module.crm.controller.admin.expense.vo.CrmExpenseRespVO;
import com.meession.etm.module.crm.controller.admin.expense.vo.CrmExpenseSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.contract.CrmContractDO;
import com.meession.etm.module.crm.dal.dataobject.customer.CrmCustomerDO;
import com.meession.etm.module.crm.dal.dataobject.expense.CrmExpenseDO;
import com.meession.etm.module.crm.service.contract.CrmContractService;
import com.meession.etm.module.crm.service.customer.CrmCustomerService;
import com.meession.etm.module.crm.service.expense.CrmExpenseService;
import com.meession.etm.module.system.api.user.AdminUserApi;
import com.meession.etm.module.system.api.user.dto.AdminUserRespDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.meession.etm.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static com.meession.etm.framework.common.pojo.CommonResult.success;
import static com.meession.etm.framework.common.pojo.PageParam.PAGE_SIZE_NONE;
import static com.meession.etm.framework.common.util.collection.CollectionUtils.convertListByFlatMap;
import static com.meession.etm.framework.common.util.collection.CollectionUtils.convertSet;
import static com.meession.etm.framework.common.util.collection.MapUtils.findAndThen;

@Tag(name = "管理后台 - CRM 费用")
@RestController
@RequestMapping("/crm/expense")
@Validated
public class CrmExpenseController {

    @Resource
    private CrmExpenseService expenseService;
    @Resource
    private CrmContractService contractService;
    @Resource
    private CrmCustomerService customerService;
    @Resource
    private AdminUserApi adminUserApi;

    @PostMapping("/create")
    @Operation(summary = "创建费用")
    @PreAuthorize("@ss.hasPermission('crm:expense:create')")
    public CommonResult<Long> createExpense(@Valid @RequestBody CrmExpenseSaveReqVO createReqVO) {
        return success(expenseService.createExpense(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新费用")
    @PreAuthorize("@ss.hasPermission('crm:expense:update')")
    public CommonResult<Boolean> updateExpense(@Valid @RequestBody CrmExpenseSaveReqVO updateReqVO) {
        expenseService.updateExpense(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除费用")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:expense:delete')")
    public CommonResult<Boolean> deleteExpense(@RequestParam("id") Long id) {
        expenseService.deleteExpense(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得费用")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('crm:expense:query')")
    public CommonResult<CrmExpenseRespVO> getExpense(@RequestParam("id") Long id) {
        CrmExpenseDO expense = expenseService.getExpense(id);
        return success(buildExpenseRespVO(expense));
    }

    @GetMapping("/page")
    @Operation(summary = "获得费用分页")
    @PreAuthorize("@ss.hasPermission('crm:expense:query')")
    public CommonResult<PageResult<CrmExpenseRespVO>> getExpensePage(@Valid CrmExpensePageReqVO pageReqVO) {
        PageResult<CrmExpenseDO> pageResult = expenseService.getExpensePage(pageReqVO);
        return success(new PageResult<>(buildExpenseDetailList(pageResult.getList()), pageResult.getTotal()));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出费用 Excel")
    @PreAuthorize("@ss.hasPermission('crm:expense:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportExpenseExcel(@Valid CrmExpensePageReqVO pageReqVO,
                                    HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PAGE_SIZE_NONE);
        List<CrmExpenseDO> list = expenseService.getExpensePage(pageReqVO).getList();
        List<CrmExpenseRespVO> respList = buildExpenseDetailList(list);
        ExcelUtils.write(response, "费用.xls", "数据", CrmExpenseRespVO.class, respList);
    }

    private List<CrmExpenseRespVO> buildExpenseDetailList(List<CrmExpenseDO> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<CrmExpenseRespVO> resultList = BeanUtils.toBean(list, CrmExpenseRespVO.class);
        // 1. 获取客户信息
        Map<Long, CrmCustomerDO> customerMap = customerService.getCustomerMap(
                convertSet(list, CrmExpenseDO::getCustomerId, id -> id != null));
        resultList.forEach(expense ->
                findAndThen(customerMap, expense.getCustomerId(),
                        customer -> expense.setCustomerName(customer.getName())));
        // 2. 获取合同信息
        Map<Long, CrmContractDO> contractMap = contractService.getContractMap(
                convertSet(list, CrmExpenseDO::getContractId, id -> id != null));
        resultList.forEach(expense ->
                findAndThen(contractMap, expense.getContractId(),
                        contract -> expense.setContractName(contract.getName())));
        // 3. 获取负责人信息
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(
                convertSet(list, CrmExpenseDO::getOwnerUserId, id -> id != null));
        resultList.forEach(expense ->
                findAndThen(userMap, expense.getOwnerUserId(),
                        user -> expense.setOwnerUserName(user.getNickname())));
        return resultList;
    }

    private CrmExpenseRespVO buildExpenseRespVO(CrmExpenseDO expense) {
        CrmExpenseRespVO respVO = BeanUtils.toBean(expense, CrmExpenseRespVO.class);
        // 客户名称
        if (expense.getCustomerId() != null) {
            CrmCustomerDO customer = customerService.getCustomer(expense.getCustomerId());
            if (customer != null) {
                respVO.setCustomerName(customer.getName());
            }
        }
        // 合同名称
        if (expense.getContractId() != null) {
            CrmContractDO contract = contractService.getContract(expense.getContractId());
            if (contract != null) {
                respVO.setContractName(contract.getName());
            }
        }
        // 负责人名称
        if (expense.getOwnerUserId() != null) {
            AdminUserRespDTO user = adminUserApi.getUser(expense.getOwnerUserId());
            if (user != null) {
                respVO.setOwnerUserName(user.getNickname());
            }
        }
        return respVO;
    }

}
// [ADD END] 费用Controller - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
