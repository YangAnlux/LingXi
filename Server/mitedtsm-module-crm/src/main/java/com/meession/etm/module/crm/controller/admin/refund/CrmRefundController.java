// 23软4胡伟-202305566535-修改于2026.07.17
// [ADD START] 退款Controller - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
package com.meession.etm.module.crm.controller.admin.refund;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import com.meession.etm.framework.apilog.core.annotation.ApiAccessLog;
import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.framework.excel.core.util.ExcelUtils;
import com.meession.etm.module.crm.controller.admin.refund.vo.CrmRefundPageReqVO;
import com.meession.etm.module.crm.controller.admin.refund.vo.CrmRefundRespVO;
import com.meession.etm.module.crm.controller.admin.refund.vo.CrmRefundSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.contract.CrmContractDO;
import com.meession.etm.module.crm.dal.dataobject.customer.CrmCustomerDO;
import com.meession.etm.module.crm.dal.dataobject.refund.CrmRefundDO;
import com.meession.etm.module.crm.service.contract.CrmContractService;
import com.meession.etm.module.crm.service.customer.CrmCustomerService;
import com.meession.etm.module.crm.service.refund.CrmRefundService;
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
import java.util.Set;

import static com.meession.etm.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static com.meession.etm.framework.common.pojo.CommonResult.success;
import static com.meession.etm.framework.common.pojo.PageParam.PAGE_SIZE_NONE;
import static com.meession.etm.framework.common.util.collection.CollectionUtils.convertSet;
import static com.meession.etm.framework.common.util.collection.MapUtils.findAndThen;
import static com.meession.etm.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - CRM 退款")
@RestController
@RequestMapping("/crm/refund")
@Validated
public class CrmRefundController {

    @Resource
    private CrmRefundService refundService;
    @Resource
    private CrmContractService contractService;
    @Resource
    private CrmCustomerService customerService;
    @Resource
    private AdminUserApi adminUserApi;

    @PostMapping("/create")
    @Operation(summary = "创建退款")
    @PreAuthorize("@ss.hasPermission('crm:refund:create')")
    public CommonResult<Long> createRefund(@Valid @RequestBody CrmRefundSaveReqVO createReqVO) {
        return success(refundService.createRefund(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新退款")
    @PreAuthorize("@ss.hasPermission('crm:refund:update')")
    public CommonResult<Boolean> updateRefund(@Valid @RequestBody CrmRefundSaveReqVO updateReqVO) {
        refundService.updateRefund(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除退款")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:refund:delete')")
    public CommonResult<Boolean> deleteRefund(@RequestParam("id") Long id) {
        refundService.deleteRefund(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得退款")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('crm:refund:query')")
    public CommonResult<CrmRefundRespVO> getRefund(@RequestParam("id") Long id) {
        CrmRefundDO refund = refundService.getRefund(id);
        return success(buildRefundRespVO(refund));
    }

    @GetMapping("/page")
    @Operation(summary = "获得退款分页")
    @PreAuthorize("@ss.hasPermission('crm:refund:query')")
    public CommonResult<PageResult<CrmRefundRespVO>> getRefundPage(@Valid CrmRefundPageReqVO pageReqVO) {
        PageResult<CrmRefundDO> pageResult = refundService.getRefundPage(pageReqVO);
        return success(new PageResult<>(buildRefundDetailList(pageResult.getList()), pageResult.getTotal()));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出退款 Excel")
    @PreAuthorize("@ss.hasPermission('crm:refund:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportRefundExcel(@Valid CrmRefundPageReqVO pageReqVO,
                                   HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PAGE_SIZE_NONE);
        List<CrmRefundDO> list = refundService.getRefundPage(pageReqVO).getList();
        ExcelUtils.write(response, "退款.xls", "数据", CrmRefundRespVO.class,
                buildRefundDetailList(list));
    }

    @PutMapping("/submit")
    @Operation(summary = "提交退款审批")
    @PreAuthorize("@ss.hasPermission('crm:refund:submit')")
    public CommonResult<Boolean> submitRefund(@RequestParam("id") Long id) {
        refundService.submitRefund(id, getLoginUserId());
        return success(true);
    }

    private List<CrmRefundRespVO> buildRefundDetailList(List<CrmRefundDO> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        // 1.1 客户
        Map<Long, CrmCustomerDO> customerMap = customerService.getCustomerMap(
                convertSet(list, CrmRefundDO::getCustomerId));
        // 1.2 合同
        Map<Long, CrmContractDO> contractMap = contractService.getContractMap(
                convertSet(list, CrmRefundDO::getContractId));
        // 1.3 负责人 + 创建人（creator 可能为字符串用户名，使用 NumberUtil 安全解析）
        Set<Long> userIds = convertSet(list, CrmRefundDO::getOwnerUserId);
        for (CrmRefundDO refundDO : list) {
            // 仅当 creator 为纯数字时才加入查询集合，避免 NumberFormatException
            if (NumberUtil.isNumber(refundDO.getCreator())) {
                userIds.add(Long.parseLong(refundDO.getCreator()));
            }
        }
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(userIds);

        // 2. 拼接
        List<CrmRefundRespVO> results = BeanUtils.toBean(list, CrmRefundRespVO.class);
        for (int i = 0; i < results.size(); i++) {
            CrmRefundRespVO refund = results.get(i);
            CrmRefundDO refundDO = list.get(i);
            findAndThen(customerMap, refund.getCustomerId(), customer -> refund.setCustomerName(customer.getName()));
            findAndThen(contractMap, refund.getContractId(), contract -> refund.setContractName(contract.getName()));
            findAndThen(userMap, refund.getOwnerUserId(), user -> refund.setOwnerUserName(user.getNickname()));
            // 安全解析创建人 ID 并填充昵称
            if (NumberUtil.isNumber(refundDO.getCreator())) {
                Long creatorId = Long.parseLong(refundDO.getCreator());
                findAndThen(userMap, creatorId, user -> refund.setCreatorName(user.getNickname()));
            }
        }
        return results;
    }

    private CrmRefundRespVO buildRefundRespVO(CrmRefundDO refund) {
        CrmRefundRespVO respVO = BeanUtils.toBean(refund, CrmRefundRespVO.class);
        // 客户
        if (refund.getCustomerId() != null) {
            CrmCustomerDO customer = customerService.getCustomer(refund.getCustomerId());
            if (customer != null) {
                respVO.setCustomerName(customer.getName());
            }
        }
        // 合同
        if (refund.getContractId() != null) {
            CrmContractDO contract = contractService.getContract(refund.getContractId());
            if (contract != null) {
                respVO.setContractName(contract.getName());
            }
        }
        // 用户
        if (refund.getOwnerUserId() != null) {
            Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(
                    Collections.singleton(refund.getOwnerUserId()));
            findAndThen(userMap, refund.getOwnerUserId(),
                    user -> respVO.setOwnerUserName(user.getNickname()));
        }
        // 创建人（creator 字段可能为用户名，安全解析后再查询）
        if (NumberUtil.isNumber(refund.getCreator())) {
            Long creatorId = Long.parseLong(refund.getCreator());
            Map<Long, AdminUserRespDTO> creatorMap = adminUserApi.getUserMap(
                    Collections.singleton(creatorId));
            findAndThen(creatorMap, creatorId,
                    user -> respVO.setCreatorName(user.getNickname()));
        }
        return respVO;
    }

}
// [ADD END] 退款Controller - 2026-07-17 - 23软4胡伟-202305566535-修改于2026.07.17
