package com.meession.etm.module.promotion.dal.mysql.campaign;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.meession.etm.module.promotion.controller.admin.campaign.vo.CampaignPageReqVO;
import com.meession.etm.module.promotion.dal.dataobject.campaign.CampaignDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 营销活动 Mapper
 *
 * @author 密讯
 */
@Mapper
public interface CampaignMapper extends BaseMapperX<CampaignDO> {

    default PageResult<CampaignDO> selectPage(CampaignPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CampaignDO>()
                .likeIfPresent(CampaignDO::getName, reqVO.getName())
                .eqIfPresent(CampaignDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(CampaignDO::getCreateTime, reqVO.getCreateTime())
                .betweenIfPresent(CampaignDO::getStartTime, reqVO.getStartTime())
                .betweenIfPresent(CampaignDO::getEndTime, reqVO.getEndTime())
                .orderByDesc(CampaignDO::getSort)
                .orderByDesc(CampaignDO::getId));
    }

}
