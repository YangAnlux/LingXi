package com.meession.etm.module.crm.dal.mysql.campaign;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.meession.etm.module.crm.controller.admin.campaign.vo.send.CrmCampaignSendLogPageReqVO;
import com.meession.etm.module.crm.dal.dataobject.campaign.CrmCampaignSendLogDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CrmCampaignSendLogMapper extends BaseMapperX<CrmCampaignSendLogDO> {

    default List<CrmCampaignSendLogDO> selectListByCampaignId(Long campaignId) {
        return selectList(new MPJLambdaWrapperX<CrmCampaignSendLogDO>()
                .eq(CrmCampaignSendLogDO::getCampaignId, campaignId)
                .orderByDesc(CrmCampaignSendLogDO::getId));
    }

    default PageResult<CrmCampaignSendLogDO> selectPage(CrmCampaignSendLogPageReqVO pageReqVO) {
        MPJLambdaWrapperX<CrmCampaignSendLogDO> query = new MPJLambdaWrapperX<>();
        query.selectAll(CrmCampaignSendLogDO.class)
                .eqIfPresent(CrmCampaignSendLogDO::getCampaignId, pageReqVO.getCampaignId())
                .eqIfPresent(CrmCampaignSendLogDO::getChannel, pageReqVO.getChannel())
                .geIfPresent(CrmCampaignSendLogDO::getSendTime, pageReqVO.getSendTimeStart())
                .leIfPresent(CrmCampaignSendLogDO::getSendTime, pageReqVO.getSendTimeEnd())
                .orderByDesc(CrmCampaignSendLogDO::getId);
        return selectJoinPage(pageReqVO, CrmCampaignSendLogDO.class, query);
    }

}
