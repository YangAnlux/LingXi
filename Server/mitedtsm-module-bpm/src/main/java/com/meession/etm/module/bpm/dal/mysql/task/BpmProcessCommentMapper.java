package com.meession.etm.module.bpm.dal.mysql.task;

import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.meession.etm.module.bpm.dal.dataobject.task.BpmProcessCommentDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BpmProcessCommentMapper extends BaseMapperX<BpmProcessCommentDO> {

    default List<BpmProcessCommentDO> selectListByProcessInstanceId(String processInstanceId) {
        return selectList(new LambdaQueryWrapperX<BpmProcessCommentDO>()
                .eq(BpmProcessCommentDO::getProcessInstanceId, processInstanceId)
                .orderByDesc(BpmProcessCommentDO::getId));
    }

}