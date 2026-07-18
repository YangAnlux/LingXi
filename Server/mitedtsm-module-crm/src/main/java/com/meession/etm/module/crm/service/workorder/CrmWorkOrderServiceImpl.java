// 2023级软4蔡磊202305566515,2026年7月14日
package com.meession.etm.module.crm.service.workorder;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderPageReqVO;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderSaveReqVO;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderStatisticsRespVO;
import com.meession.etm.module.crm.dal.dataobject.workorder.CrmWorkOrderDO;
import com.meession.etm.module.crm.dal.dataobject.workorder.CrmWorkOrderRecordDO;
import com.meession.etm.module.crm.dal.mysql.workorder.CrmWorkOrderMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import static com.meession.etm.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.meession.etm.module.crm.enums.ErrorCodeConstants.WORK_ORDER_NOT_EXISTS;
import static com.meession.etm.module.crm.enums.ErrorCodeConstants.WORK_ORDER_STATUS_INVALID;

import java.time.LocalDateTime;
import java.util.List;

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

    @Resource(name = "crmWorkOrderRecordService")
    private CrmWorkOrderRecordServiceImpl recordService;

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
                // 23软件工程4班蔡磊202305566515
                .setSlaDeadline(calcSlaDeadline(createReqVO.getPriority()))
                .setIsSlaBreached(false)
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processWorkOrder(Long id) {
        CrmWorkOrderDO workOrder = workOrderMapper.selectById(id);
        if (workOrder == null) {
            throw exception(WORK_ORDER_NOT_EXISTS);
        }
        // 待处理/已退回 → 处理中
        if (!"待处理".equals(workOrder.getStatus()) && !"已退回".equals(workOrder.getStatus())) {
            throw exception(WORK_ORDER_STATUS_INVALID);
        }
        String fromStatus = workOrder.getStatus();
        workOrderMapper.updateById(new CrmWorkOrderDO().setId(id).setStatus("处理中"));
        // 记录处理日志
        recordService.createRecord(new CrmWorkOrderRecordDO()
                .setWorkOrderId(id).setFromStatus(fromStatus).setToStatus("处理中"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resolveWorkOrder(Long id) {
        CrmWorkOrderDO workOrder = workOrderMapper.selectById(id);
        if (workOrder == null) {
            throw exception(WORK_ORDER_NOT_EXISTS);
        }
        // 处理中 → 已完结
        if (!"处理中".equals(workOrder.getStatus())) {
            throw exception(WORK_ORDER_STATUS_INVALID);
        }
        String fromStatus = workOrder.getStatus();
        workOrderMapper.updateById(new CrmWorkOrderDO().setId(id)
                .setStatus("已完结").setResolvedTime(LocalDateTime.now()));
        // 记录处理日志
        recordService.createRecord(new CrmWorkOrderRecordDO()
                .setWorkOrderId(id).setFromStatus(fromStatus).setToStatus("已完结"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void returnWorkOrder(Long id) {
        CrmWorkOrderDO workOrder = workOrderMapper.selectById(id);
        if (workOrder == null) {
            throw exception(WORK_ORDER_NOT_EXISTS);
        }
        // 处理中 → 已退回
        if (!"处理中".equals(workOrder.getStatus())) {
            throw exception(WORK_ORDER_STATUS_INVALID);
        }
        String fromStatus = workOrder.getStatus();
        workOrderMapper.updateById(new CrmWorkOrderDO().setId(id).setStatus("已退回"));
        // 记录处理日志
        recordService.createRecord(new CrmWorkOrderRecordDO()
                .setWorkOrderId(id).setFromStatus(fromStatus).setToStatus("已退回"));
    }

    // 23软件工程4班蔡磊202305566515
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignWorkOrder(Long id, Long assigneeId) {
        CrmWorkOrderDO workOrder = workOrderMapper.selectById(id);
        if (workOrder == null) {
            throw exception(WORK_ORDER_NOT_EXISTS);
        }
        workOrderMapper.updateById(new CrmWorkOrderDO().setId(id).setAssigneeId(assigneeId));
    }

    // 23软件工程4班蔡磊202305566515
    /**
     * 根据优先级计算 SLA 截止时间
     */
    private LocalDateTime calcSlaDeadline(String priority) {
        LocalDateTime now = LocalDateTime.now();
        if ("URGENT".equals(priority)) {
            return now.plusHours(2);
        } else if ("HIGH".equals(priority)) {
            return now.plusHours(4);
        } else if ("NORMAL".equals(priority)) {
            return now.plusHours(8);
        } else {
            return now.plusHours(24);
        }
    }

    // 23软件工程4班蔡磊202305566515
    @Override
    public int updateSlaBreached() {
        return workOrderMapper.updateSlaBreached();
    }

    // 23软件工程4班蔡磊202305566515
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitSatisfaction(Long id, Integer satisfactionScore, String satisfactionComment) {
        CrmWorkOrderDO workOrder = workOrderMapper.selectById(id);
        if (workOrder == null) {
            throw exception(WORK_ORDER_NOT_EXISTS);
        }
        // 仅已完结工单可回访评分
        if (!"已完结".equals(workOrder.getStatus())) {
            throw exception(WORK_ORDER_STATUS_INVALID);
        }
        // 不允许重复评分
        if (workOrder.getSatisfactionScore() != null) {
            throw exception(WORK_ORDER_STATUS_INVALID);
        }
        workOrderMapper.updateById(new CrmWorkOrderDO()
                .setId(id)
                .setSatisfactionScore(satisfactionScore)
                .setSatisfactionComment(satisfactionComment));
    }

    // 23软件工程4班蔡磊202305566515
    @Override
    public List<CrmWorkOrderStatisticsRespVO> countByType() {
        return workOrderMapper.countByType();
    }

    // 23软件工程4班蔡磊202305566515
    @Override
    public List<CrmWorkOrderStatisticsRespVO> countByStatus() {
        return workOrderMapper.countByStatus();
    }

    // 23软件工程4班蔡磊202305566515
    @Override
    public List<CrmWorkOrderStatisticsRespVO> countByAssignee() {
        return workOrderMapper.countByAssignee();
    }

}
