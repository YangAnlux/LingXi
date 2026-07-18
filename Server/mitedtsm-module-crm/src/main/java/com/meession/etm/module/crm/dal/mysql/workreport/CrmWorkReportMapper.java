package com.meession.etm.module.crm.dal.mysql.workreport;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.meession.etm.module.crm.controller.admin.workreport.vo.workreport.CrmWorkReportPageReqVO;
import com.meession.etm.module.crm.dal.dataobject.workreport.CrmWorkReportDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CrmWorkReportMapper extends BaseMapperX<CrmWorkReportDO> {

    default PageResult<CrmWorkReportDO> selectPage(CrmWorkReportPageReqVO pageReqVO) {
        MPJLambdaWrapperX<CrmWorkReportDO> query = new MPJLambdaWrapperX<>();
        query.selectAll(CrmWorkReportDO.class)
                .likeIfPresent(CrmWorkReportDO::getTitle, pageReqVO.getTitle())
                .eqIfPresent(CrmWorkReportDO::getStatus, pageReqVO.getStatus())
                .eqIfPresent(CrmWorkReportDO::getReportType, pageReqVO.getReportType())
                .eqIfPresent(CrmWorkReportDO::getOwnerUserId, pageReqVO.getOwnerUserId())
                .geIfPresent(CrmWorkReportDO::getReportDate, pageReqVO.getReportDateStart())
                .leIfPresent(CrmWorkReportDO::getReportDate, pageReqVO.getReportDateEnd())
                .orderByDesc(CrmWorkReportDO::getCreateTime);
        return selectJoinPage(pageReqVO, CrmWorkReportDO.class, query);
    }

}
