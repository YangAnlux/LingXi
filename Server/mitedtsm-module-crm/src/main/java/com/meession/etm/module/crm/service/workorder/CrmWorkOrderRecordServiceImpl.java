// 2023级软4蔡磊202305566515,2026年7月14日
package com.meession.etm.module.crm.service.workorder;

import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.meession.etm.module.crm.dal.dataobject.workorder.CrmWorkOrderRecordDO;
import com.meession.etm.module.crm.dal.mysql.workorder.CrmWorkOrderRecordMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * 工单处理记录 Service 实现类
 *
 * @author CRM Team
 */
@Service("crmWorkOrderRecordService")
@Validated
public class CrmWorkOrderRecordServiceImpl implements CrmWorkOrderRecordService {

    @Resource
    private CrmWorkOrderRecordMapper recordMapper;

    @Override
    public List<CrmWorkOrderRecordDO> getRecordList(Long workOrderId) {
        return recordMapper.selectList(new LambdaQueryWrapperX<CrmWorkOrderRecordDO>()
                .eq(CrmWorkOrderRecordDO::getWorkOrderId, workOrderId)
                .orderByDesc(CrmWorkOrderRecordDO::getId));
    }

    @Override
    public void createRecord(CrmWorkOrderRecordDO record) {
        recordMapper.insert(record);
    }

}
