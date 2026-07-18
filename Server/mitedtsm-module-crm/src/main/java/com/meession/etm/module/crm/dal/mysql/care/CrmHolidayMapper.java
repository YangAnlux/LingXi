package com.meession.etm.module.crm.dal.mysql.care;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.meession.etm.module.crm.controller.admin.care.vo.holiday.CrmHolidayPageReqVO;
import com.meession.etm.module.crm.dal.dataobject.care.CrmHolidayDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface CrmHolidayMapper extends BaseMapperX<CrmHolidayDO> {

    /**
     * 查询指定日期的启用节日列表
     */
    default List<CrmHolidayDO> selectListByDate(LocalDate date) {
        return selectList(new MPJLambdaWrapperX<CrmHolidayDO>()
                .eq(CrmHolidayDO::getHolidayDate, date)
                .eq(CrmHolidayDO::getStatus, 0));
    }

    /**
     * 节日配置分页查询
     */
    default PageResult<CrmHolidayDO> selectPage(CrmHolidayPageReqVO pageReqVO) {
        MPJLambdaWrapperX<CrmHolidayDO> query = new MPJLambdaWrapperX<>();
        query.selectAll(CrmHolidayDO.class)
                .likeIfPresent(CrmHolidayDO::getName, pageReqVO.getName())
                .eqIfPresent(CrmHolidayDO::getHolidayDate, pageReqVO.getHolidayDate())
                .eqIfPresent(CrmHolidayDO::getStatus, pageReqVO.getStatus())
                .orderByAsc(CrmHolidayDO::getHolidayDate);
        return selectJoinPage(pageReqVO, CrmHolidayDO.class, query);
    }

}
