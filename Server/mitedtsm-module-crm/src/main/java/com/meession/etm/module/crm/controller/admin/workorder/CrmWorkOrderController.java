package com.meession.etm.module.crm.controller.admin.workorder;

import cn.hutool.core.collection.CollUtil;
import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.collection.MapUtils;
import com.meession.etm.framework.common.util.number.NumberUtils;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderPageReqVO;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderRespVO;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.customer.CrmCustomerDO;
import com.meession.etm.module.crm.dal.dataobject.workorder.CrmWorkOrderDO;
import com.meession.etm.module.crm.service.customer.CrmCustomerService;
import com.meession.etm.module.crm.service.workorder.CrmWorkOrderService;
import com.meession.etm.module.system.api.user.AdminUserApi;
import com.meession.etm.module.system.api.user.dto.AdminUserRespDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.meession.etm.framework.common.pojo.CommonResult.success;
import static com.meession.etm.framework.common.util.collection.CollectionUtils.*;
import static com.meession.etm.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - CRM 工单")
@RestController
@RequestMapping("/crm/work-order")
@Validated
public class CrmWorkOrderController {

    @Resource
    private CrmWorkOrderService workOrderService;
    @Resource
    private CrmCustomerService customerService;
    @Resource
    private AdminUserApi adminUserApi;

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

    @GetMapping("/get")
    @Operation(summary = "获得工单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('crm:work-order:query')")
    public CommonResult<CrmWorkOrderRespVO> getWorkOrder(@RequestParam("id") Long id) {
        CrmWorkOrderDO workOrder = workOrderService.getWorkOrder(id);
        return success(buildWorkOrderRespVO(workOrder));
    }

    @GetMapping("/page")
    @Operation(summary = "获得工单分页")
    @PreAuthorize("@ss.hasPermission('crm:work-order:query')")
    public CommonResult<PageResult<CrmWorkOrderRespVO>> getWorkOrderPage(@Valid CrmWorkOrderPageReqVO pageVO) {
        PageResult<CrmWorkOrderDO> pageResult = workOrderService.getWorkOrderPage(pageVO);
        return success(new PageResult<>(buildWorkOrderRespVOList(pageResult.getList()), pageResult.getTotal()));
    }

    @PutMapping("/update-status")
    @Operation(summary = "更新工单状态")
    @PreAuthorize("@ss.hasPermission('crm:work-order:update')")
    public CommonResult<Boolean> updateWorkOrderStatus(@RequestParam("id") Long id, @RequestParam("status") String status) {
        workOrderService.updateWorkOrderStatus(id, status);
        return success(true);
    }

    @PutMapping("/assign")
    @Operation(summary = "分配工单")
    @PreAuthorize("@ss.hasPermission('crm:work-order:update')")
    public CommonResult<Boolean> assignWorkOrder(@RequestParam("id") Long id, @RequestParam("assigneeId") Long assigneeId) {
        workOrderService.assignWorkOrder(id, assigneeId);
        return success(true);
    }

    private CrmWorkOrderRespVO buildWorkOrderRespVO(CrmWorkOrderDO workOrder) {
        if (workOrder == null) {
            return null;
        }
        return buildWorkOrderRespVOList(Collections.singletonList(workOrder)).get(0);
    }

    private List<CrmWorkOrderRespVO> buildWorkOrderRespVOList(List<CrmWorkOrderDO> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        Map<Long, CrmCustomerDO> customerMap = customerService.getCustomerMap(
                convertSet(list, CrmWorkOrderDO::getCustomerId));
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(convertListByFlatMap(list,
                workOrder -> Stream.of(NumberUtils.parseLong(workOrder.getCreator()), workOrder.getAssigneeId())));
        return BeanUtils.toBean(list, CrmWorkOrderRespVO.class, respVO -> {
            MapUtils.findAndThen(customerMap, respVO.getCustomerId(), customer -> respVO.setCustomerName(customer.getName()));
            MapUtils.findAndThen(userMap, respVO.getAssigneeId(), user -> respVO.setAssigneeName(user.getNickname()));
        });
    }

}