package com.meession.etm.module.crm.service.schedule;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.crm.controller.admin.schedule.vo.schedule.CrmSchedulePageReqVO;
import com.meession.etm.module.crm.controller.admin.schedule.vo.schedule.CrmScheduleSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.schedule.CrmScheduleDO;
import com.meession.etm.module.crm.dal.mysql.schedule.CrmScheduleMapper;
import com.meession.etm.module.crm.service.notify.CrmNotifyService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.meession.etm.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.meession.etm.module.crm.enums.ErrorCodeConstants.SCHEDULE_NOT_EXISTS;

@Service
@Slf4j
public class CrmScheduleServiceImpl implements CrmScheduleService {

    @Resource
    private CrmScheduleMapper scheduleMapper;

    @Resource
    private CrmNotifyService crmNotifyService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createSchedule(CrmScheduleSaveReqVO createReqVO, Long userId) {
        CrmScheduleDO schedule = BeanUtils.toBean(createReqVO, CrmScheduleDO.class);
        schedule.setOwnerUserId(userId);
        schedule.setStatus(0);
        scheduleMapper.insert(schedule);
        // 发送日程创建通知
        crmNotifyService.sendScheduleChangeNotify(schedule, "创建");
        return schedule.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSchedule(CrmScheduleSaveReqVO updateReqVO) {
        CrmScheduleDO schedule = validateScheduleExists(updateReqVO.getId());
        BeanUtils.copyProperties(updateReqVO, schedule);
        scheduleMapper.updateById(schedule);
        // 发送日程变更通知
        crmNotifyService.sendScheduleChangeNotify(schedule, "更新");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSchedule(Long id) {
        validateScheduleExists(id);
        scheduleMapper.deleteById(id);
    }

    @Override
    public CrmScheduleDO getSchedule(Long id) {
        return validateScheduleExists(id);
    }

    @Override
    public PageResult<CrmScheduleDO> getSchedulePage(CrmSchedulePageReqVO pageReqVO) {
        return scheduleMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CrmScheduleDO> getScheduleListByTimeRange(LocalDateTime start, LocalDateTime end) {
        return scheduleMapper.selectListByTimeRange(start, end);
    }

    private CrmScheduleDO validateScheduleExists(Long id) {
        CrmScheduleDO schedule = scheduleMapper.selectById(id);
        if (schedule == null) {
            throw exception(SCHEDULE_NOT_EXISTS);
        }
        return schedule;
    }

}
