package com.meession.etm.module.crm.dal.mysql.care;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.meession.etm.module.crm.controller.admin.care.vo.CrmCareRulePageReqVO;
import com.meession.etm.module.crm.dal.dataobject.care.CrmCareRuleDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CrmCareRuleMapper extends BaseMapperX<CrmCareRuleDO> {

    default PageResult<CrmCareRuleDO> selectPage(CrmCareRulePageReqVO pageReqVO) {
        MPJLambdaWrapperX<CrmCareRuleDO> query = new MPJLambdaWrapperX<>();
        query.selectAll(CrmCareRuleDO.class)
                .eqIfPresent(CrmCareRuleDO::getTriggerType, pageReqVO.getTriggerType())
                .eqIfPresent(CrmCareRuleDO::getIsEnabled, pageReqVO.getIsEnabled())
                .orderByDesc(CrmCareRuleDO::getCreateTime);
        return selectJoinPage(pageReqVO, CrmCareRuleDO.class, query);
    }

    /** 查询所有启用的规则 */
    default List<CrmCareRuleDO> selectEnabledRules() {
        return selectList(new MPJLambdaWrapperX<CrmCareRuleDO>()
                .eq(CrmCareRuleDO::getIsEnabled, 1));
    }

}
