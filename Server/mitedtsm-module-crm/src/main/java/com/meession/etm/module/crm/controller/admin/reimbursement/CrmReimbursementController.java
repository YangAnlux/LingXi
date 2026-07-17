// 23软4胡伟-202305566535-修改于2026.07.16
// [ADD START] 报销Controller - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
package com.meession.etm.module.crm.controller.admin.reimbursement;

import cn.hutool.core.collection.CollUtil;
import com.meession.etm.framework.apilog.core.annotation.ApiAccessLog;
import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.framework.excel.core.util.ExcelUtils;
import com.meession.etm.module.crm.controller.admin.reimbursement.vo.CrmReimbursementPageReqVO;
import com.meession.etm.module.crm.controller.admin.reimbursement.vo.CrmReimbursementRespVO;
import com.meession.etm.module.crm.controller.admin.reimbursement.vo.CrmReimbursementSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.contract.CrmContractDO;
import com.meession.etm.module.crm.dal.dataobject.customer.CrmCustomerDO;
import com.meession.etm.module.crm.dal.dataobject.reimbursement.CrmReimbursementDO;
import com.meession.etm.module.crm.service.contract.CrmContractService;
import com.meession.etm.module.crm.service.customer.CrmCustomerService;
import com.meession.etm.module.crm.service.reimbursement.CrmReimbursementService;
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

import static com.meession.etm.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static com.meession.etm.framework.common.pojo.CommonResult.success;
import static com.meession.etm.framework.common.pojo.PageParam.PAGE_SIZE_NONE;
import static com.meession.etm.framework.common.util.collection.CollectionUtils.convertSet;
import static com.meession.etm.framework.common.util.collection.MapUtils.findAndThen;
import static com.meession.etm.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - CRM 报销")
@RestController
@RequestMapping("/crm/reimbursement")
@Validated
public class CrmReimbursementController {

    @Resource
    private CrmReimbursementService reimbursementService;
    @Resource
    private CrmContractService contractService;
    @Resource
    private CrmCustomerService customerService;
    @Resource
    private AdminUserApi adminUserApi;

    @PostMapping("/create")
    @Operation(summary = "创建报销")
    @PreAuthorize("@ss.hasPermission('crm:reimbursement:create')")
    public CommonResult<Long> createReimbursement(@Valid @RequestBody CrmReimbursementSaveReqVO createReqVO) {
        return success(reimbursementService.createReimbursement(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新报销")
    @PreAuthorize("@ss.hasPermission('crm:reimbursement:update')")
    public CommonResult<Boolean> updateReimbursement(@Valid @RequestBody CrmReimbursementSaveReqVO updateReqVO) {
        reimbursementService.updateReimbursement(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除报销")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:reimbursement:delete')")
    public CommonResult<Boolean> deleteReimbursement(@RequestParam("id") Long id) {
        reimbursementService.deleteReimbursement(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得报销")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('crm:reimbursement:query')")
    public CommonResult<CrmReimbursementRespVO> getReimbursement(@RequestParam("id") Long id) {
        CrmReimbursementDO reimbursement = reimbursementService.getReimbursement(id);
        return success(buildReimbursementRespVO(reimbursement));
    }

    @GetMapping("/page")
    @Operation(summary = "获得报销分页")
    @PreAuthorize("@ss.hasPermission('crm:reimbursement:query')")
    public CommonResult<PageResult<CrmReimbursementRespVO>> getReimbursementPage(@Valid CrmReimbursementPageReqVO pageReqVO) {
        PageResult<CrmReimbursementDO> pageResult = reimbursementService.getReimbursementPage(pageReqVO);
        return success(new PageResult<>(buildReimbursementDetailList(pageResult.getList()), pageResult.getTotal()));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出报销 Excel")
    @PreAuthorize("@ss.hasPermission('crm:reimbursement:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportReimbursementExcel(@Valid CrmReimbursementPageReqVO pageReqVO,
                                        HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PAGE_SIZE_NONE);
        List<CrmReimbursementDO> list = reimbursementService.getReimbursementPage(pageReqVO).getList();
        ExcelUtils.write(response, "报销.xls", "数据", CrmReimbursementRespVO.class,
                buildReimbursementDetailList(list));
    }

    @PutMapping("/submit")
    @Operation(summary = "提交报销审批")
    @PreAuthorize("@ss.hasPermission('crm:reimbursement:submit')")
    public CommonResult<Boolean> submitReimbursement(@RequestParam("id") Long id) {
        reimbursementService.submitReimbursement(id, getLoginUserId());
        return success(true);
    }

    private List<CrmReimbursementRespVO> buildReimbursementDetailList(List<CrmReimbursementDO> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<CrmReimbursementRespVO> resultList = BeanUtils.toBean(list, CrmReimbursementRespVO.class);
        // 1. 客户
        Map<Long, CrmCustomerDO> customerMap = customerService.getCustomerMap(
                convertSet(list, CrmReimbursementDO::getCustomerId, id -> id != null));
        resultList.forEach(r -> findAndThen(customerMap, r.getCustomerId(),
                customer -> r.setCustomerName(customer.getName())));
        // 2. 合同
        Map<Long, CrmContractDO> contractMap = contractService.getContractMap(
                convertSet(list, CrmReimbursementDO::getContractId, id -> id != null));
        resultList.forEach(r -> findAndThen(contractMap, r.getContractId(),
                contract -> r.setContractName(contract.getName())));
        // 3. 负责人
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(
                convertSet(list, CrmReimbursementDO::getOwnerUserId, id -> id != null));
        resultList.forEach(r -> findAndThen(userMap, r.getOwnerUserId(),
                user -> r.setOwnerUserName(user.getNickname())));
        return resultList;
    }

    private CrmReimbursementRespVO buildReimbursementRespVO(CrmReimbursementDO reimbursement) {
        CrmReimbursementRespVO respVO = BeanUtils.toBean(reimbursement, CrmReimbursementRespVO.class);
        if (reimbursement.getCustomerId() != null) {
            CrmCustomerDO customer = customerService.getCustomer(reimbursement.getCustomerId());
            if (customer != null) respVO.setCustomerName(customer.getName());
        }
        if (reimbursement.getContractId() != null) {
            CrmContractDO contract = contractService.getContract(reimbursement.getContractId());
            if (contract != null) respVO.setContractName(contract.getName());
        }
        if (reimbursement.getOwnerUserId() != null) {
            AdminUserRespDTO user = adminUserApi.getUser(reimbursement.getOwnerUserId());
            if (user != null) respVO.setOwnerUserName(user.getNickname());
        }
        return respVO;
    }

}
// [ADD END] 报销Controller - 2026-07-16 - 23软4胡伟-202305566535-修改于2026.07.16
