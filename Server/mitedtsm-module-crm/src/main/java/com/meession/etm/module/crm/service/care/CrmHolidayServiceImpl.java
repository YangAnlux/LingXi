package com.meession.etm.module.crm.service.care;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.meession.etm.module.crm.controller.admin.care.vo.holiday.CrmHolidayPageReqVO;
import com.meession.etm.module.crm.controller.admin.care.vo.holiday.CrmHolidaySaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.care.CrmHolidayDO;
import com.meession.etm.module.crm.dal.mysql.care.CrmHolidayMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.meession.etm.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.meession.etm.module.crm.enums.ErrorCodeConstants.HOLIDAY_NOT_EXISTS;

@Service
@Slf4j
public class CrmHolidayServiceImpl implements CrmHolidayService {

    @Resource
    private CrmHolidayMapper holidayMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createHoliday(CrmHolidaySaveReqVO createReqVO) {
        CrmHolidayDO holiday = BeanUtils.toBean(createReqVO, CrmHolidayDO.class);
        holidayMapper.insert(holiday);
        return holiday.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateHoliday(CrmHolidaySaveReqVO updateReqVO) {
        CrmHolidayDO holiday = validateHolidayExists(updateReqVO.getId());
        BeanUtils.copyProperties(updateReqVO, holiday);
        holidayMapper.updateById(holiday);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteHoliday(Long id) {
        validateHolidayExists(id);
        holidayMapper.deleteById(id);
    }

    @Override
    public CrmHolidayDO getHoliday(Long id) {
        return validateHolidayExists(id);
    }

    @Override
    public PageResult<CrmHolidayDO> getHolidayPage(CrmHolidayPageReqVO pageVO) {
        return holidayMapper.selectPage(pageVO);
    }

    @Override
    public List<CrmHolidayDO> listAllEnabled() {
        return holidayMapper.selectList(
                new MPJLambdaWrapperX<CrmHolidayDO>()
                        .eq(CrmHolidayDO::getStatus, 0)
                        .orderByAsc(CrmHolidayDO::getHolidayDate));
    }

    private CrmHolidayDO validateHolidayExists(Long id) {
        CrmHolidayDO holiday = holidayMapper.selectById(id);
        if (holiday == null) {
            throw exception(HOLIDAY_NOT_EXISTS);
        }
        return holiday;
    }

}
