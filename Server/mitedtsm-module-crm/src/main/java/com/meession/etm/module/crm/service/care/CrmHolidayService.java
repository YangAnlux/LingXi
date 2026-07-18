package com.meession.etm.module.crm.service.care;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.care.vo.holiday.CrmHolidayPageReqVO;
import com.meession.etm.module.crm.controller.admin.care.vo.holiday.CrmHolidaySaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.care.CrmHolidayDO;

import jakarta.validation.Valid;
import java.util.List;

public interface CrmHolidayService {

    /** 创建节日配置 */
    Long createHoliday(@Valid CrmHolidaySaveReqVO createReqVO);

    /** 更新节日配置 */
    void updateHoliday(@Valid CrmHolidaySaveReqVO updateReqVO);

    /** 删除节日配置 */
    void deleteHoliday(Long id);

    /** 获得节日配置 */
    CrmHolidayDO getHoliday(Long id);

    /** 获得节日配置分页 */
    PageResult<CrmHolidayDO> getHolidayPage(CrmHolidayPageReqVO pageVO);

    /** 获取全部启用的节日列表 */
    List<CrmHolidayDO> listAllEnabled();

}
