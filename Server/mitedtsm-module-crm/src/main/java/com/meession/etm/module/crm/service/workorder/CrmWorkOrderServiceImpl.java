// 2023级软4蔡磊202305566515,2026年7月14日
package com.meession.etm.module.crm.service.workorder;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderPageReqVO;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.workorder.CrmWorkOrderDO;
import com.meession.etm.module.crm.dal.mysql.workorder.CrmWorkOrderMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import static com.meession.etm.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.meession.etm.module.crm.enums.ErrorCodeConstants.WORK_ORDER_NOT_EXISTS;

/**
 * 工单 Service 实现类
 *
 * @author CRM Team
 */
@Service
@Validated
public class CrmWorkOrderServiceImpl implements CrmWorkOrderService {

    @Resource
    private CrmWorkOrderMapper workOrderMapper;

    @Override
    public PageResult<CrmWorkOrderDO> getWorkOrderPage(CrmWorkOrderPageReqVO pageReqVO) {
        return workOrderMapper.selectPage(pageReqVO);
    }

    @Override
    public CrmWorkOrderDO getWorkOrder(Long id) {
        CrmWorkOrderDO workOrder = workOrderMapper.selectById(id);
        if (workOrder == null) {
            throw exception(WORK_ORDER_NOT_EXISTS);
        }
        return workOrder;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createWorkOrder(CrmWorkOrderSaveReqVO createReqVO) {
        CrmWorkOrderDO workOrder = new CrmWorkOrderDO()
                .setTitle(createReqVO.getTitle())
                .setType(createReqVO.getType())
                .setPriority(createReqVO.getPriority())
                .setStatus(createReqVO.getStatus())
                .setCustomerId(createReqVO.getCustomerId())
                .setAssigneeId(createReqVO.getAssigneeId())
                .setSlaDeadline(createReqVO.getSlaDeadline())
                .setContent(createReqVO.getContent())
                .setSolution(createReqVO.getSolution());
        workOrderMapper.insert(workOrder);
        return workOrder.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWorkOrder(CrmWorkOrderSaveReqVO updateReqVO) {
        CrmWorkOrderDO existing = workOrderMapper.selectById(updateReqVO.getId());
        if (existing == null) {
            throw exception(WORK_ORDER_NOT_EXISTS);
        }
        CrmWorkOrderDO workOrder = new CrmWorkOrderDO()
                .setId(updateReqVO.getId())
                .setTitle(updateReqVO.getTitle())
                .setType(updateReqVO.getType())
                .setPriority(updateReqVO.getPriority())
                .setStatus(updateReqVO.getStatus())
                .setCustomerId(updateReqVO.getCustomerId())
                .setAssigneeId(updateReqVO.getAssigneeId())
                .setSlaDeadline(updateReqVO.getSlaDeadline())
                .setContent(updateReqVO.getContent())
                .setSolution(updateReqVO.getSolution());
        workOrderMapper.updateById(workOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteWorkOrder(Long id) {
        CrmWorkOrderDO existing = workOrderMapper.selectById(id);
        if (existing == null) {
            throw exception(WORK_ORDER_NOT_EXISTS);
        }
        workOrderMapper.deleteById(id);
    }

}
