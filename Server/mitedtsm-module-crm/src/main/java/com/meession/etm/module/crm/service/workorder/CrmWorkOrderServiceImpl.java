package com.meession.etm.module.crm.service.workorder;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderPageReqVO;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.workorder.CrmWorkOrderDO;
import com.meession.etm.module.crm.dal.mysql.workorder.CrmWorkOrderMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.meession.etm.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.meession.etm.module.crm.enums.ErrorCodeConstants.WORK_ORDER_NOT_EXISTS;

@Service
@Validated
@Slf4j
public class CrmWorkOrderServiceImpl implements CrmWorkOrderService {

    @Resource
    private CrmWorkOrderMapper workOrderMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createWorkOrder(CrmWorkOrderSaveReqVO reqVO) {
        CrmWorkOrderDO workOrder = BeanUtils.toBean(reqVO, CrmWorkOrderDO.class);
        workOrder.setStatus("PENDING");
        workOrder.setIsSlaBreached(0);
        workOrderMapper.insert(workOrder);
        return workOrder.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWorkOrder(CrmWorkOrderSaveReqVO reqVO) {
        validateWorkOrderExists(reqVO.getId());
        CrmWorkOrderDO updateObj = BeanUtils.toBean(reqVO, CrmWorkOrderDO.class);
        workOrderMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteWorkOrder(Long id) {
        validateWorkOrderExists(id);
        workOrderMapper.deleteById(id);
    }

    @Override
    public CrmWorkOrderDO getWorkOrder(Long id) {
        return workOrderMapper.selectById(id);
    }

    @Override
    public PageResult<CrmWorkOrderDO> getWorkOrderPage(CrmWorkOrderPageReqVO pageReqVO) {
        return workOrderMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWorkOrderStatus(Long id, String status) {
        CrmWorkOrderDO workOrder = validateWorkOrderExists(id);
        workOrder.setStatus(status);
        if (Objects.equals(status, "RESOLVED")) {
            workOrder.setResolvedTime(LocalDateTime.now());
        }
        workOrderMapper.updateById(workOrder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignWorkOrder(Long id, Long assigneeId) {
        CrmWorkOrderDO workOrder = validateWorkOrderExists(id);
        workOrder.setAssigneeId(assigneeId);
        workOrder.setStatus("PROCESSING");
        workOrderMapper.updateById(workOrder);
    }

    @Override
    public int updateSlaBreached() {
        return workOrderMapper.updateSlaBreachedStatus();
    }

    @Override
    public Map<String, Long> getStatisticsByType() {
        return workOrderMapper.selectList().stream()
                .filter(w -> w.getType() != null)
                .collect(Collectors.groupingBy(CrmWorkOrderDO::getType, Collectors.counting()));
    }

    @Override
    public Map<String, Long> getStatisticsByStatus() {
        return workOrderMapper.selectList().stream()
                .filter(w -> w.getStatus() != null)
                .collect(Collectors.groupingBy(CrmWorkOrderDO::getStatus, Collectors.counting()));
    }

    @Override
    public Map<String, Long> getStatisticsByAssignee() {
        return workOrderMapper.selectList().stream()
                .filter(w -> w.getAssigneeId() != null)
                .collect(Collectors.groupingBy(w -> String.valueOf(w.getAssigneeId()), Collectors.counting()));
    }

    private CrmWorkOrderDO validateWorkOrderExists(Long id) {
        CrmWorkOrderDO workOrder = workOrderMapper.selectById(id);
        if (workOrder == null) {
            throw exception(WORK_ORDER_NOT_EXISTS);
        }
        return workOrder;
    }

}