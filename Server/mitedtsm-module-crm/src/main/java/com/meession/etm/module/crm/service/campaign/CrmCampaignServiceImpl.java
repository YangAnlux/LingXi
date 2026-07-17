package com.meession.etm.module.crm.service.campaign;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.controller.admin.campaign.vo.campaign.CrmCampaignPageReqVO;
import com.meession.etm.module.crm.controller.admin.campaign.vo.campaign.CrmCampaignSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.campaign.CrmCampaignDO;
import com.meession.etm.module.crm.dal.mysql.campaign.CrmCampaignMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.meession.etm.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.meession.etm.module.crm.enums.ErrorCodeConstants.CAMPAIGN_NOT_EXISTS;

@Service
@Slf4j
public class CrmCampaignServiceImpl implements CrmCampaignService {

    @Resource
    private CrmCampaignMapper crmCampaignMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createCampaign(CrmCampaignSaveReqVO createReqVO, Long userId) {
        CrmCampaignDO campaign = BeanUtils.toBean(createReqVO, CrmCampaignDO.class);
        campaign.setOwnerUserId(userId);
        crmCampaignMapper.insert(campaign);
        return campaign.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCampaign(CrmCampaignSaveReqVO updateReqVO) {
        CrmCampaignDO campaign = validateCampaignExists(updateReqVO.getId());
        BeanUtils.copyProperties(updateReqVO, campaign);
        crmCampaignMapper.updateById(campaign);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCampaign(Long id) {
        validateCampaignExists(id);
        crmCampaignMapper.deleteById(id);
    }

    @Override
    public CrmCampaignDO getCampaign(Long id) {
        return validateCampaignExists(id);
    }

    @Override
    public PageResult<CrmCampaignDO> getCampaignPage(CrmCampaignPageReqVO pageReqVO, Long ownerUserId) {
        return crmCampaignMapper.selectPage(pageReqVO, ownerUserId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCampaignStatus(Long id, Integer status) {
        CrmCampaignDO campaign = validateCampaignExists(id);
        campaign.setStatus(status);
        crmCampaignMapper.updateById(campaign);
    }

    private CrmCampaignDO validateCampaignExists(Long id) {
        CrmCampaignDO campaign = crmCampaignMapper.selectById(id);
        if (campaign == null) {
            throw exception(CAMPAIGN_NOT_EXISTS);
        }
        return campaign;
    }

}
