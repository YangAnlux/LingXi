package com.meession.etm.module.crm.dal.mysql.task;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.meession.etm.module.crm.controller.admin.task.vo.task.CrmTaskPageReqVO;
import com.meession.etm.module.crm.dal.dataobject.task.CrmTaskDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CrmTaskMapper extends BaseMapperX<CrmTaskDO> {

    default PageResult<CrmTaskDO> selectPage(CrmTaskPageReqVO pageReqVO) {
        MPJLambdaWrapperX<CrmTaskDO> query = new MPJLambdaWrapperX<>();
        query.selectAll(CrmTaskDO.class)
                .likeIfPresent(CrmTaskDO::getName, pageReqVO.getName())
                .eqIfPresent(CrmTaskDO::getStatus, pageReqVO.getStatus())
                .eqIfPresent(CrmTaskDO::getPriority, pageReqVO.getPriority())
                .eqIfPresent(CrmTaskDO::getOwnerUserId, pageReqVO.getOwnerUserId())
                .eqIfPresent(CrmTaskDO::getAssignerUserId, pageReqVO.getAssignerUserId())
                .geIfPresent(CrmTaskDO::getEndTime, pageReqVO.getEndTimeStart())
                .leIfPresent(CrmTaskDO::getEndTime, pageReqVO.getEndTimeEnd())
                .orderByDesc(CrmTaskDO::getCreateTime);
        return selectJoinPage(pageReqVO, CrmTaskDO.class, query);
    }

    default List<CrmTaskDO> selectListByDueTimeRange(LocalDateTime start, LocalDateTime end) {
        MPJLambdaWrapperX<CrmTaskDO> query = new MPJLambdaWrapperX<>();
        query.selectAll(CrmTaskDO.class)
                .ge(CrmTaskDO::getEndTime, start)
                .le(CrmTaskDO::getEndTime, end)
                .in(CrmTaskDO::getStatus, 0, 1)
                .isNotNull(CrmTaskDO::getOwnerUserId);
        return selectList(query);
    }

}
