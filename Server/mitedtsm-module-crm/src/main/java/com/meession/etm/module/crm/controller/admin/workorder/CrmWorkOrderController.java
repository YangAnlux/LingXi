// 2023级软4蔡磊202305566515,2026年7月14日
package com.meession.etm.module.crm.controller.admin.workorder;

import cn.hutool.core.collection.CollUtil;
import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.collection.MapUtils;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderAssignReqVO;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderPageReqVO;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderRecordRespVO;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderRespVO;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderSatisfactionReqVO;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderSaveReqVO;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderStatisticsRespVO;
import com.meession.etm.module.crm.dal.dataobject.workorder.CrmWorkOrderDO;
import com.meession.etm.module.crm.dal.dataobject.workorder.CrmWorkOrderRecordDO;
import com.meession.etm.module.crm.service.workorder.CrmWorkOrderRecordService;
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

import static com.meession.etm.framework.common.pojo.CommonResult.success;
import static com.meession.etm.framework.common.util.collection.CollectionUtils.convertSet;

@Tag(name = "管理后台 - CRM 工单")
@RestController
@RequestMapping("/crm/work-order")
@Validated
public class CrmWorkOrderController {

    @Resource
    private CrmWorkOrderService workOrderService;

    @Resource(name = "crmWorkOrderRecordService")
    private CrmWorkOrderRecordService recordService;

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
        // [MODIFY] 填充处理人名称 - 2026-07-17 - Cailei
        return success(buildWorkOrderDetail(workOrder));
    }

    @GetMapping("/page")
    @Operation(summary = "获得工单分页")
    @PreAuthorize("@ss.hasPermission('crm:work-order:query')")
    public CommonResult<PageResult<CrmWorkOrderRespVO>> getWorkOrderPage(@Valid CrmWorkOrderPageReqVO pageReqVO) {
        PageResult<CrmWorkOrderDO> pageResult = workOrderService.getWorkOrderPage(pageReqVO);
        // [MODIFY] 填充处理人名称 - 2026-07-17 - Cailei
        List<CrmWorkOrderRespVO> voList = buildWorkOrderDetailList(pageResult.getList());
        return success(new PageResult<>(voList, pageResult.getTotal()));
    }

    @PutMapping("/process")
    @Operation(summary = "开始处理工单（待处理/已退回 → 处理中）")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:work-order:update')")
    public CommonResult<Boolean> processWorkOrder(@RequestParam("id") Long id) {
        workOrderService.processWorkOrder(id);
        return success(true);
    }

    @PutMapping("/resolve")
    @Operation(summary = "完结工单（处理中 → 已完结）")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:work-order:update')")
    public CommonResult<Boolean> resolveWorkOrder(@RequestParam("id") Long id) {
        workOrderService.resolveWorkOrder(id);
        return success(true);
    }

    @PutMapping("/return")
    @Operation(summary = "退回工单（处理中 → 已退回）")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:work-order:update')")
    public CommonResult<Boolean> returnWorkOrder(@RequestParam("id") Long id) {
        workOrderService.returnWorkOrder(id);
        return success(true);
    }

    // 23软件工程4班蔡磊202305566515
    @PutMapping("/assign")
    @Operation(summary = "分配工单（指派处理人）")
    @PreAuthorize("@ss.hasPermission('crm:work-order:assign')")
    public CommonResult<Boolean> assignWorkOrder(@Valid @RequestBody CrmWorkOrderAssignReqVO assignReqVO) {
        workOrderService.assignWorkOrder(assignReqVO.getId(), assignReqVO.getAssigneeId());
        return success(true);
    }

    // 23软件工程4班蔡磊202305566515
    @PutMapping("/satisfaction")
    @Operation(summary = "满意度回访评分")
    @PreAuthorize("@ss.hasPermission('crm:work-order:update')")
    public CommonResult<Boolean> submitSatisfaction(@Valid @RequestBody CrmWorkOrderSatisfactionReqVO reqVO) {
        workOrderService.submitSatisfaction(reqVO.getId(), reqVO.getSatisfactionScore(), reqVO.getSatisfactionComment());
        return success(true);
    }

    @GetMapping("/record-list")
    @Operation(summary = "获得工单处理记录列表")
    @Parameter(name = "workOrderId", description = "工单编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('crm:work-order:query')")
    public CommonResult<List<CrmWorkOrderRecordRespVO>> getRecordList(@RequestParam("workOrderId") Long workOrderId) {
        List<CrmWorkOrderRecordDO> list = recordService.getRecordList(workOrderId);
        return success(BeanUtils.toBean(list, CrmWorkOrderRecordRespVO.class));
    }

    // 23软件工程4班蔡磊202305566515
    @GetMapping("/statistics")
    @Operation(summary = "工单统计报表（按类型/状态/处理人）")
    @PreAuthorize("@ss.hasPermission('crm:work-order:query')")
    public CommonResult<Map<String, List<CrmWorkOrderStatisticsRespVO>>> getStatistics() {
        return success(Map.of(
                "byType", workOrderService.countByType(),
                "byStatus", workOrderService.countByStatus(),
                "byAssignee", workOrderService.countByAssignee()));
    }

    // [ADD START] 填充处理人名称 - 2026-07-17 - Cailei
    private CrmWorkOrderRespVO buildWorkOrderDetail(CrmWorkOrderDO workOrder) {
        if (workOrder == null) {
            return null;
        }
        return buildWorkOrderDetailList(Collections.singletonList(workOrder)).get(0);
    }

    private List<CrmWorkOrderRespVO> buildWorkOrderDetailList(List<CrmWorkOrderDO> workOrderList) {
        if (CollUtil.isEmpty(workOrderList)) {
            return Collections.emptyList();
        }
        // 获取处理人列表
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(
                convertSet(workOrderList, CrmWorkOrderDO::getAssigneeId));
        // 拼接数据：填充 assigneeName
        return BeanUtils.toBean(workOrderList, CrmWorkOrderRespVO.class, workOrderVO -> {
            MapUtils.findAndThen(userMap, workOrderVO.getAssigneeId(),
                    user -> workOrderVO.setAssigneeName(user.getNickname()));
        });
    }
    // [ADD END] 填充处理人名称 - 2026-07-17 - Cailei

}
