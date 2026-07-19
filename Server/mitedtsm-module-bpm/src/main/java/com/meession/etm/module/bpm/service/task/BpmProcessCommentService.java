package com.meession.etm.module.bpm.service.task;

import com.meession.etm.module.bpm.dal.dataobject.task.BpmProcessCommentDO;

import java.util.List;

public interface BpmProcessCommentService {

    BpmProcessCommentDO createProcessComment(String processInstanceId, String taskId, String content);

    List<BpmProcessCommentDO> getProcessCommentList(String processInstanceId);

    void deleteProcessComment(Long id);

}