package com.meession.etm.module.crm.service.campaign;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.campaign.vo.campaign.CrmCampaignPageReqVO;
import com.meession.etm.module.crm.controller.admin.campaign.vo.campaign.CrmCampaignSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.campaign.CrmCampaignDO;

import jakarta.validation.Valid;

public interface CrmCampaignService {

    Long createCampaign(@Valid CrmCampaignSaveReqVO createReqVO, Long userId);

    void updateCampaign(@Valid CrmCampaignSaveReqVO updateReqVO);

    void deleteCampaign(Long id);

    CrmCampaignDO getCampaign(Long id);

    PageResult<CrmCampaignDO> getCampaignPage(CrmCampaignPageReqVO pageReqVO, Long ownerUserId);

    void updateCampaignStatus(Long id, Integer status);

}
