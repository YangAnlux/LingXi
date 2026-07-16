// 2023级软4蔡磊202305566515,2026年7月14日
package com.meession.etm.module.crm.controller.admin.workorder;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderPageReqVO;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderRespVO;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.workorder.CrmWorkOrderDO;
import com.meession.etm.module.crm.service.workorder.CrmWorkOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.meession.etm.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - CRM 工单")
@RestController
@RequestMapping("/crm/work-order")
@Validated
public class CrmWorkOrderController {

    @Resource
    private CrmWorkOrderService workOrderService;

    @PostMapping("/create")
    @Operation(summary = "创建工单")
    @PreAuthorize("@ss.hasPermission('crm:work-order:create')")
    public CommonResult<Long> createWorkOrder(@Valid @RequestBody CrmWorkOrderSaveReqVO createReqVO) {
        return success(workOrderService.createWorkOrder(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新工单")
    @PreAuthorize("@ss.hasPermission('crm:work-order:update')")
    public CommonResult<Boolean> updateWorkOrder(@Valid @RequestBody CrmWorkOrderSaveReqVO updateReqVO) {
        workOrderService.updateWorkOrder(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除工单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:work-order:delete')")
    public CommonResult<Boolean> deleteWorkOrder(@RequestParam("id") Long id) {
        workOrderService.deleteWorkOrder(id);
        return success(true);
    }

    // 2023级软4蔡磊202305566515,2026年7月14日
    @PutMapping("/transition-status")
    @Operation(summary = "工单状态流转")
    @PreAuthorize("@ss.hasPermission('crm:work-order:update')")
    public CommonResult<Boolean> transitionStatusWorkOrder(@RequestParam("id") Long id,
                                                           @RequestParam("status") String status) {
        workOrderService.transitionStatus(id, status);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得工单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('crm:work-order:query')")
    public CommonResult<CrmWorkOrderRespVO> getWorkOrder(@RequestParam("id") Long id) {
        CrmWorkOrderDO workOrder = workOrderService.getWorkOrder(id);
        return success(BeanUtils.toBean(workOrder, CrmWorkOrderRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得工单分页")
    @PreAuthorize("@ss.hasPermission('crm:work-order:query')")
    public CommonResult<PageResult<CrmWorkOrderRespVO>> getWorkOrderPage(@Valid CrmWorkOrderPageReqVO pageReqVO) {
        PageResult<CrmWorkOrderDO> pageResult = workOrderService.getWorkOrderPage(pageReqVO);
        List<CrmWorkOrderRespVO> voList = BeanUtils.toBean(pageResult.getList(), CrmWorkOrderRespVO.class);
        return success(new PageResult<>(voList, pageResult.getTotal()));
    }

}
