// 2023级软4蔡磊202305566515,2026年7月14日
package com.meession.etm.module.crm.service.workorder;

import com.meession.etm.module.crm.dal.dataobject.workorder.CrmWorkOrderRecordDO;

import java.util.List;

/**
 * 工单处理记录 Service 接口
 *
 * @author CRM Team
 */
public interface CrmWorkOrderRecordService {

    /**
     * 获得工单的处理记录列表
     *
     * @param workOrderId 工单编号
     * @return 处理记录列表
     */
    List<CrmWorkOrderRecordDO> getRecordList(Long workOrderId);

    /**
     * 创建处理记录
     *
     * @param record 处理记录
     */
    void createRecord(CrmWorkOrderRecordDO record);

}
