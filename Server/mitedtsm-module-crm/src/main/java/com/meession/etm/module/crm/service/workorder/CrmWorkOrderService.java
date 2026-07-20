package com.meession.etm.module.crm.service.workorder;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderPageReqVO;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.workorder.CrmWorkOrderDO;

import jakarta.validation.Valid;

import java.util.Map;

public interface CrmWorkOrderService {

    Long createWorkOrder(@Valid CrmWorkOrderSaveReqVO reqVO);

    void updateWorkOrder(@Valid CrmWorkOrderSaveReqVO reqVO);

    void deleteWorkOrder(Long id);

    CrmWorkOrderDO getWorkOrder(Long id);

    PageResult<CrmWorkOrderDO> getWorkOrderPage(CrmWorkOrderPageReqVO pageReqVO);

    void updateWorkOrderStatus(Long id, String status);

    void assignWorkOrder(Long id, Long assigneeId);

    int updateSlaBreached();

    Map<String, Long> getStatisticsByType();

    Map<String, Long> getStatisticsByStatus();

    Map<String, Long> getStatisticsByAssignee();
}