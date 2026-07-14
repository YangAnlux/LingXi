// 2023级软4蔡磊202305566515,2026年7月14日
package com.meession.etm.module.crm.dal.mysql.workorder;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderPageReqVO;
import com.meession.etm.module.crm.dal.dataobject.workorder.CrmWorkOrderDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 工单 Mapper
 *
 * @author CRM Team
 */
@Mapper
public interface CrmWorkOrderMapper extends BaseMapperX<CrmWorkOrderDO> {

    default PageResult<CrmWorkOrderDO> selectPage(CrmWorkOrderPageReqVO pageReqVO) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<CrmWorkOrderDO>()
                .likeIfPresent(CrmWorkOrderDO::getTitle, pageReqVO.getTitle())
                .eqIfPresent(CrmWorkOrderDO::getType, pageReqVO.getType())
                .eqIfPresent(CrmWorkOrderDO::getPriority, pageReqVO.getPriority())
                .eqIfPresent(CrmWorkOrderDO::getStatus, pageReqVO.getStatus())
                .eqIfPresent(CrmWorkOrderDO::getCustomerId, pageReqVO.getCustomerId())
                .eqIfPresent(CrmWorkOrderDO::getAssigneeId, pageReqVO.getAssigneeId())
                .orderByDesc(CrmWorkOrderDO::getId));
    }

}
