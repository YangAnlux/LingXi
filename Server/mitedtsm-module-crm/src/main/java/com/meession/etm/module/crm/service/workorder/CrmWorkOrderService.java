// 2023级软4蔡磊202305566515,2026年7月14日
package com.meession.etm.module.crm.service.workorder;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderPageReqVO;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderSaveReqVO;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderStatisticsRespVO;
import com.meession.etm.module.crm.dal.dataobject.workorder.CrmWorkOrderDO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 工单 Service 接口
 *
 * @author CRM Team
 */
public interface CrmWorkOrderService {

    /**
     * 分页查询工单
     *
     * @param pageReqVO 分页请求
     * @return 分页结果
     */
    PageResult<CrmWorkOrderDO> getWorkOrderPage(CrmWorkOrderPageReqVO pageReqVO);

    /**
     * 查询工单详情
     *
     * @param id 编号
     * @return 工单
     */
    CrmWorkOrderDO getWorkOrder(Long id);

    /**
     * 创建工单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createWorkOrder(@Valid CrmWorkOrderSaveReqVO createReqVO);

    /**
     * 更新工单
     *
     * @param updateReqVO 更新信息
     */
    void updateWorkOrder(@Valid CrmWorkOrderSaveReqVO updateReqVO);

    /**
     * 删除工单
     *
     * @param id 编号
     */
    void deleteWorkOrder(Long id);

    /**
     * 开始处理工单（待处理/已退回 → 处理中）
     *
     * @param id 编号
     */
    void processWorkOrder(Long id);

    /**
     * 完结工单（处理中 → 已完结）
     *
     * @param id 编号
     */
    void resolveWorkOrder(Long id);

    /**
     * 退回工单（处理中 → 已退回）
     *
     * @param id 编号
     */
    void returnWorkOrder(Long id);

    // 23软件工程4班蔡磊202305566515
    /**
     * 分配工单（指派处理人）
     *
     * @param id         工单编号
     * @param assigneeId 处理人编号
     */
    void assignWorkOrder(Long id, Long assigneeId);

    // 23软件工程4班蔡磊202305566515
    /**
     * 扫描超时工单，标记 isSlaBreached
     *
     * @return 标记数量
     */
    int updateSlaBreached();

    // 23软件工程4班蔡磊202305566515
    /**
     * 满意度回访评分
     *
     * @param id                 工单编号
     * @param satisfactionScore  满意度评分（1-5）
     * @param satisfactionComment 满意度评价内容
     */
    void submitSatisfaction(Long id, Integer satisfactionScore, String satisfactionComment);

    // 23软件工程4班蔡磊202305566515
    /**
     * 工单统计报表（按类型/状态/处理人三个维度）
     *
     * @return 三个维度的统计数据
     */
    List<CrmWorkOrderStatisticsRespVO> countByType();

    // 23软件工程4班蔡磊202305566515
    List<CrmWorkOrderStatisticsRespVO> countByStatus();

    // 23软件工程4班蔡磊202305566515
    List<CrmWorkOrderStatisticsRespVO> countByAssignee();

}
