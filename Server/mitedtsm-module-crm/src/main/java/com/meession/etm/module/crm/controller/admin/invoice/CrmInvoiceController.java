// 23软4胡伟-202305566535-修改于2026.07.14
// [ADD START] 发票Controller - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
package com.meession.etm.module.crm.controller.admin.invoice;

import cn.hutool.core.collection.CollUtil;
import com.meession.etm.framework.apilog.core.annotation.ApiAccessLog;
import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.collection.CollectionUtils;
import com.meession.etm.framework.common.util.number.NumberUtils;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.framework.excel.core.util.ExcelUtils;
import com.meession.etm.module.crm.controller.admin.invoice.vo.CrmInvoicePageReqVO;
import com.meession.etm.module.crm.controller.admin.invoice.vo.CrmInvoiceRespVO;
import com.meession.etm.module.crm.controller.admin.invoice.vo.CrmInvoiceSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.contract.CrmContractDO;
import com.meession.etm.module.crm.dal.dataobject.customer.CrmCustomerDO;
import com.meession.etm.module.crm.dal.dataobject.invoice.CrmInvoiceDO;
import com.meession.etm.module.crm.service.contract.CrmContractService;
import com.meession.etm.module.crm.service.customer.CrmCustomerService;
import com.meession.etm.module.crm.service.invoice.CrmInvoiceService;
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

@Tag(name = "管理后台 - CRM 发票")
@RestController
@RequestMapping("/crm/invoice")
@Validated
public class CrmInvoiceController {

    @Resource
    private CrmInvoiceService invoiceService;
    @Resource
    private CrmContractService contractService;
    @Resource
    private CrmCustomerService customerService;
    @Resource
    private AdminUserApi adminUserApi;

    @PostMapping("/create")
    @Operation(summary = "创建发票")
    @PreAuthorize("@ss.hasPermission('crm:invoice:create')")
    public CommonResult<Long> createInvoice(@Valid @RequestBody CrmInvoiceSaveReqVO createReqVO) {
        return success(invoiceService.createInvoice(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新发票")
    @PreAuthorize("@ss.hasPermission('crm:invoice:update')")
    public CommonResult<Boolean> updateInvoice(@Valid @RequestBody CrmInvoiceSaveReqVO updateReqVO) {
        invoiceService.updateInvoice(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除发票")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:invoice:delete')")
    public CommonResult<Boolean> deleteInvoice(@RequestParam("id") Long id) {
        invoiceService.deleteInvoice(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得发票")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('crm:invoice:query')")
    public CommonResult<CrmInvoiceRespVO> getInvoice(@RequestParam("id") Long id) {
        CrmInvoiceDO invoice = invoiceService.getInvoice(id);
        return success(buildInvoiceDetail(invoice));
    }

    @GetMapping("/page")
    @Operation(summary = "获得发票分页")
    @PreAuthorize("@ss.hasPermission('crm:invoice:query')")
    public CommonResult<PageResult<CrmInvoiceRespVO>> getInvoicePage(@Valid CrmInvoicePageReqVO pageReqVO) {
        PageResult<CrmInvoiceDO> pageResult = invoiceService.getInvoicePage(pageReqVO);
        return success(new PageResult<>(buildInvoiceDetailList(pageResult.getList()), pageResult.getTotal()));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出发票 Excel")
    @PreAuthorize("@ss.hasPermission('crm:invoice:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportInvoiceExcel(@Valid CrmInvoicePageReqVO exportReqVO,
                                   HttpServletResponse response) throws IOException {
        exportReqVO.setPageSize(PAGE_SIZE_NONE);
        List<CrmInvoiceDO> list = invoiceService.getInvoicePage(exportReqVO).getList();
        ExcelUtils.write(response, "发票.xls", "数据", CrmInvoiceRespVO.class,
                buildInvoiceDetailList(list));
    }

    private List<CrmInvoiceRespVO> buildInvoiceDetailList(List<CrmInvoiceDO> invoiceList) {
        if (CollUtil.isEmpty(invoiceList)) {
            return Collections.emptyList();
        }
        // 1.1 获取客户列表
        Map<Long, CrmCustomerDO> customerMap = customerService.getCustomerMap(
                convertSet(invoiceList, CrmInvoiceDO::getCustomerId));
        // 1.2 获取创建人、负责人列表
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(convertListByFlatMap(invoiceList,
                contact -> Stream.of(NumberUtils.parseLong(contact.getCreator()), contact.getOwnerUserId())));
        // 1.3 获得合同列表
        Map<Long, CrmContractDO> contractMap = contractService.getContractMap(
                convertSet(invoiceList, CrmInvoiceDO::getContractId));
        // 2. 拼接结果
        return BeanUtils.toBean(invoiceList, CrmInvoiceRespVO.class, (invoiceVO) -> {
            // 2.1 拼接客户名称
            findAndThen(customerMap, invoiceVO.getCustomerId(), customer -> invoiceVO.setCustomerName(customer.getName()));
            // 2.2 拼接负责人、创建人名称
            com.meession.etm.framework.common.util.collection.MapUtils.findAndThen(userMap,
                    NumberUtils.parseLong(invoiceVO.getCreator()),
                    user -> invoiceVO.setCreatorName(user.getNickname()));
            com.meession.etm.framework.common.util.collection.MapUtils.findAndThen(userMap,
                    invoiceVO.getOwnerUserId(),
                    user -> invoiceVO.setOwnerUserName(user.getNickname()));
            // 2.3 拼接合同信息
            findAndThen(contractMap, invoiceVO.getContractId(), contract ->
                    invoiceVO.setContractName(contract.getName()));
        });
    }

    private CrmInvoiceRespVO buildInvoiceDetail(CrmInvoiceDO invoice) {
        if (invoice == null) {
            return null;
        }
        return buildInvoiceDetailList(Collections.singletonList(invoice)).get(0);
    }

}
// [ADD END] 发票Controller - 2026-07-14 - 23软4胡伟-202305566535-修改于2026.07.14
