package com.meession.etm.module.crm.dal.mysql.care;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.meession.etm.module.crm.controller.admin.campaign.vo.send.CrmCareLogPageReqVO;
import com.meession.etm.module.crm.dal.dataobject.care.CrmCareLogDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CrmCareLogMapper extends BaseMapperX<CrmCareLogDO> {

    default PageResult<CrmCareLogDO> selectPage(CrmCareLogPageReqVO pageReqVO) {
        MPJLambdaWrapperX<CrmCareLogDO> query = new MPJLambdaWrapperX<>();
        query.selectAll(CrmCareLogDO.class)
                .eqIfPresent(CrmCareLogDO::getCareType, pageReqVO.getCareType())
                .eqIfPresent(CrmCareLogDO::getChannel, pageReqVO.getChannel())
                .eqIfPresent(CrmCareLogDO::getStatus, pageReqVO.getStatus())
                .eqIfPresent(CrmCareLogDO::getCustomerId, pageReqVO.getCustomerId())
                .geIfPresent(CrmCareLogDO::getSendTime, pageReqVO.getSendTimeStart())
                .leIfPresent(CrmCareLogDO::getSendTime, pageReqVO.getSendTimeEnd())
                .orderByDesc(CrmCareLogDO::getId);
        return selectJoinPage(pageReqVO, CrmCareLogDO.class, query);
    }

}
