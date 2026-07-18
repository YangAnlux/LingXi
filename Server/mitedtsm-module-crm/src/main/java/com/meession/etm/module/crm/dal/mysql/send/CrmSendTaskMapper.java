package com.meession.etm.module.crm.dal.mysql.send;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.meession.etm.module.crm.controller.admin.send.vo.CrmSendTaskPageReqVO;
import com.meession.etm.module.crm.dal.dataobject.send.CrmSendTaskDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * CRM 发送任务 Mapper
 */
@Mapper
public interface CrmSendTaskMapper extends BaseMapperX<CrmSendTaskDO> {

    default PageResult<CrmSendTaskDO> selectPage(CrmSendTaskPageReqVO pageReqVO) {
        MPJLambdaWrapperX<CrmSendTaskDO> query = new MPJLambdaWrapperX<>();
        query.selectAll(CrmSendTaskDO.class)
                .eqIfPresent(CrmSendTaskDO::getTaskType, pageReqVO.getTaskType())
                .eqIfPresent(CrmSendTaskDO::getChannel, pageReqVO.getChannel())
                .eqIfPresent(CrmSendTaskDO::getStatus, pageReqVO.getStatus())
                .eqIfPresent(CrmSendTaskDO::getActivityId, pageReqVO.getActivityId())
                .orderByDesc(CrmSendTaskDO::getCreateTime);
        return selectJoinPage(pageReqVO, CrmSendTaskDO.class, query);
    }

}
