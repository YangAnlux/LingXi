package com.meession.etm.module.crm.dal.mysql.campaign;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.meession.etm.module.crm.controller.admin.campaign.vo.campaign.CrmCampaignPageReqVO;
import com.meession.etm.module.crm.dal.dataobject.campaign.CrmCampaignDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CrmCampaignMapper extends BaseMapperX<CrmCampaignDO> {

    default PageResult<CrmCampaignDO> selectPage(CrmCampaignPageReqVO pageReqVO, Long ownerUserId) {
        MPJLambdaWrapperX<CrmCampaignDO> query = new MPJLambdaWrapperX<>();
        query.selectAll(CrmCampaignDO.class)
                .likeIfPresent(CrmCampaignDO::getName, pageReqVO.getName())
                .eqIfPresent(CrmCampaignDO::getStatus, pageReqVO.getStatus())
                .eqIfPresent(CrmCampaignDO::getChannel, pageReqVO.getChannel())
                .eqIfPresent(CrmCampaignDO::getOwnerUserId, pageReqVO.getOwnerUserId())
                .geIfPresent(CrmCampaignDO::getStartTime, pageReqVO.getStartTime())
                .leIfPresent(CrmCampaignDO::getEndTime, pageReqVO.getEndTime())
                .orderByDesc(CrmCampaignDO::getCreateTime);
        return selectJoinPage(pageReqVO, CrmCampaignDO.class, query);
    }

}
