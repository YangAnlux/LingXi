// 2023级软4蔡磊202305566515,2026年7月14日
package com.meession.etm.module.crm.service.workorder;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderPageReqVO;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.workorder.CrmWorkOrderDO;
import jakarta.validation.Valid;

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

}
