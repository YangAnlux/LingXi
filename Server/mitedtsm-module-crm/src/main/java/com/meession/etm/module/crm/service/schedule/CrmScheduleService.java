package com.meession.etm.module.crm.service.schedule;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.schedule.vo.schedule.CrmSchedulePageReqVO;
import com.meession.etm.module.crm.controller.admin.schedule.vo.schedule.CrmScheduleSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.schedule.CrmScheduleDO;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

public interface CrmScheduleService {

    Long createSchedule(@Valid CrmScheduleSaveReqVO createReqVO, Long userId);

    void updateSchedule(@Valid CrmScheduleSaveReqVO updateReqVO);

    void deleteSchedule(Long id);

    CrmScheduleDO getSchedule(Long id);

    PageResult<CrmScheduleDO> getSchedulePage(CrmSchedulePageReqVO pageReqVO);

    List<CrmScheduleDO> getScheduleListByTimeRange(LocalDateTime start, LocalDateTime end);

}
