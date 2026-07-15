package com.meession.etm.module.promotion.service.campaign;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.promotion.controller.admin.campaign.vo.CampaignCreateReqVO;
import com.meession.etm.module.promotion.controller.admin.campaign.vo.CampaignPageReqVO;
import com.meession.etm.module.promotion.controller.admin.campaign.vo.CampaignUpdateReqVO;
import com.meession.etm.module.promotion.dal.dataobject.campaign.CampaignDO;
import jakarta.validation.Valid;

/**
 * 营销活动 Service 接口
 *
 * @author 密讯
 */
public interface CampaignService {

    /**
     * 创建营销活动
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCampaign(@Valid CampaignCreateReqVO createReqVO);

    /**
     * 更新营销活动
     *
     * @param updateReqVO 更新信息
     */
    void updateCampaign(@Valid CampaignUpdateReqVO updateReqVO);

    /**
     * 关闭营销活动
     *
     * @param id 编号
     */
    void closeCampaign(Long id);

    /**
     * 删除营销活动
     *
     * @param id 编号
     */
    void deleteCampaign(Long id);

    /**
     * 获得营销活动
     *
     * @param id 编号
     * @return 营销活动
     */
    CampaignDO getCampaign(Long id);

    /**
     * 获得营销活动分页
     *
     * @param pageReqVO 分页查询
     * @return 营销活动分页
     */
    PageResult<CampaignDO> getCampaignPage(CampaignPageReqVO pageReqVO);

}
