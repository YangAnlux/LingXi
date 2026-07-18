package com.meession.etm.module.crm.dal.mysql.schedule;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.meession.etm.module.crm.controller.admin.schedule.vo.schedule.CrmSchedulePageReqVO;
import com.meession.etm.module.crm.dal.dataobject.schedule.CrmScheduleDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CrmScheduleMapper extends BaseMapperX<CrmScheduleDO> {

    default PageResult<CrmScheduleDO> selectPage(CrmSchedulePageReqVO pageReqVO) {
        MPJLambdaWrapperX<CrmScheduleDO> query = new MPJLambdaWrapperX<>();
        query.selectAll(CrmScheduleDO.class)
                .likeIfPresent(CrmScheduleDO::getTitle, pageReqVO.getTitle())
                .eqIfPresent(CrmScheduleDO::getScheduleType, pageReqVO.getScheduleType())
                .eqIfPresent(CrmScheduleDO::getOwnerUserId, pageReqVO.getOwnerUserId())
                .eqIfPresent(CrmScheduleDO::getStatus, pageReqVO.getStatus())
                .geIfPresent(CrmScheduleDO::getStartTime, pageReqVO.getStartTime())
                .leIfPresent(CrmScheduleDO::getEndTime, pageReqVO.getEndTime())
                .orderByAsc(CrmScheduleDO::getStartTime);
        return selectJoinPage(pageReqVO, CrmScheduleDO.class, query);
    }

    default List<CrmScheduleDO> selectListByTimeRange(LocalDateTime start, LocalDateTime end) {
        MPJLambdaWrapperX<CrmScheduleDO> query = new MPJLambdaWrapperX<>();
        query.selectAll(CrmScheduleDO.class)
                .ge(CrmScheduleDO::getStartTime, start)
                .le(CrmScheduleDO::getEndTime, end)
                .eq(CrmScheduleDO::getStatus, 0)
                .orderByAsc(CrmScheduleDO::getStartTime);
        return selectList(query);
    }

}
