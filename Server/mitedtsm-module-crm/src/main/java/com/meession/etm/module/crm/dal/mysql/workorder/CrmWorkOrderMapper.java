package com.meession.etm.module.crm.dal.mysql.workorder;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.meession.etm.module.crm.controller.admin.workorder.vo.workorder.CrmWorkOrderPageReqVO;
import com.meession.etm.module.crm.dal.dataobject.workorder.CrmWorkOrderDO;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

@Mapper
public interface CrmWorkOrderMapper extends BaseMapperX<CrmWorkOrderDO> {

    default PageResult<CrmWorkOrderDO> selectPage(CrmWorkOrderPageReqVO reqVO) {
        return selectPage(reqVO, new MPJLambdaWrapperX<CrmWorkOrderDO>()
                .likeIfPresent(CrmWorkOrderDO::getTitle, reqVO.getTitle())
                .eqIfPresent(CrmWorkOrderDO::getStatus, reqVO.getStatus())
                .eqIfPresent(CrmWorkOrderDO::getPriority, reqVO.getPriority())
                .eqIfPresent(CrmWorkOrderDO::getType, reqVO.getType())
                .eqIfPresent(CrmWorkOrderDO::getCustomerId, reqVO.getCustomerId())
                .eqIfPresent(CrmWorkOrderDO::getAssigneeId, reqVO.getAssigneeId())
                .orderByDesc(CrmWorkOrderDO::getId));
    }

    default int updateSlaBreachedStatus() {
        return update(null, new LambdaUpdateWrapper<CrmWorkOrderDO>()
                .set(CrmWorkOrderDO::getIsSlaBreached, true)
                .lt(CrmWorkOrderDO::getSlaDeadline, LocalDateTime.now())
                .eq(CrmWorkOrderDO::getIsSlaBreached, false)
                .notIn(CrmWorkOrderDO::getStatus, "已完结", "已退回"));
    }

}