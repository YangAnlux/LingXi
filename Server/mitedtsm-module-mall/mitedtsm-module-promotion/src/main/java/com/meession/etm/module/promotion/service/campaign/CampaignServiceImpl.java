package com.meession.etm.module.promotion.service.campaign;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.promotion.controller.admin.campaign.vo.CampaignCreateReqVO;
import com.meession.etm.module.promotion.controller.admin.campaign.vo.CampaignPageReqVO;
import com.meession.etm.module.promotion.controller.admin.campaign.vo.CampaignUpdateReqVO;
import com.meession.etm.module.promotion.convert.campaign.CampaignConvert;
import com.meession.etm.module.promotion.dal.dataobject.campaign.CampaignDO;
import com.meession.etm.module.promotion.dal.mysql.campaign.CampaignMapper;
import com.meession.etm.module.promotion.enums.common.PromotionActivityStatusEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static com.meession.etm.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.meession.etm.module.promotion.enums.ErrorCodeConstants.*;

/**
 * 营销活动 Service 实现类
 *
 * @author 密讯
 */
@Service
@Validated
public class CampaignServiceImpl implements CampaignService {

    @Resource
    private CampaignMapper campaignMapper;

    @Override
    public Long createCampaign(CampaignCreateReqVO createReqVO) {
        // 插入活动，初始状态为未开始
        CampaignDO campaign = CampaignConvert.INSTANCE.convert(createReqVO)
                .setStatus(PromotionActivityStatusEnum.WAIT.getStatus());
        campaignMapper.insert(campaign);
        return campaign.getId();
    }

    @Override
    public void updateCampaign(CampaignUpdateReqVO updateReqVO) {
        // 校验存在
        CampaignDO campaign = validateCampaignExists(updateReqVO.getId());
        // 已关闭的活动，不能修改
        if (campaign.getStatus().equals(PromotionActivityStatusEnum.CLOSE.getStatus())) {
            throw exception(CAMPAIGN_CLOSED_CANNOT_UPDATE);
        }
        // 更新活动
        CampaignDO updateObj = CampaignConvert.INSTANCE.convert(updateReqVO);
        campaignMapper.updateById(updateObj);
    }

    @Override
    public void closeCampaign(Long id) {
        // 校验存在
        CampaignDO campaign = validateCampaignExists(id);
        // 已关闭的活动，不能重复关闭
        if (campaign.getStatus().equals(PromotionActivityStatusEnum.CLOSE.getStatus())) {
            throw exception(CAMPAIGN_CLOSE_FAIL_STATUS_CLOSED);
        }
        // 更新状态为已关闭
        campaignMapper.updateById(new CampaignDO().setId(id).setStatus(PromotionActivityStatusEnum.CLOSE.getStatus()));
    }

    @Override
    public void deleteCampaign(Long id) {
        // 校验存在
        CampaignDO campaign = validateCampaignExists(id);
        // 未关闭的活动，不能删除
        if (!campaign.getStatus().equals(PromotionActivityStatusEnum.CLOSE.getStatus())) {
            throw exception(CAMPAIGN_DELETE_FAIL_STATUS_NOT_CLOSED);
        }
        // 删除活动（MyBatis-Plus 软删除）
        campaignMapper.deleteById(id);
    }

    private CampaignDO validateCampaignExists(Long id) {
        CampaignDO campaign = campaignMapper.selectById(id);
        if (campaign == null) {
            throw exception(CAMPAIGN_NOT_EXISTS);
        }
        return campaign;
    }

    @Override
    public CampaignDO getCampaign(Long id) {
        return campaignMapper.selectById(id);
    }

    @Override
    public PageResult<CampaignDO> getCampaignPage(CampaignPageReqVO pageReqVO) {
        return campaignMapper.selectPage(pageReqVO);
    }

}
