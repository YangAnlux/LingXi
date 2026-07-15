package com.meession.etm.module.promotion.convert.campaign;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.promotion.controller.admin.campaign.vo.CampaignCreateReqVO;
import com.meession.etm.module.promotion.controller.admin.campaign.vo.CampaignRespVO;
import com.meession.etm.module.promotion.controller.admin.campaign.vo.CampaignUpdateReqVO;
import com.meession.etm.module.promotion.dal.dataobject.campaign.CampaignDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 营销活动 Convert
 *
 * @author 密讯
 */
@Mapper
public interface CampaignConvert {

    CampaignConvert INSTANCE = Mappers.getMapper(CampaignConvert.class);

    CampaignDO convert(CampaignCreateReqVO bean);

    CampaignDO convert(CampaignUpdateReqVO bean);

    CampaignRespVO convert(CampaignDO bean);

    List<CampaignRespVO> convertList(List<CampaignDO> list);

    PageResult<CampaignRespVO> convertPage(PageResult<CampaignDO> page);

}
