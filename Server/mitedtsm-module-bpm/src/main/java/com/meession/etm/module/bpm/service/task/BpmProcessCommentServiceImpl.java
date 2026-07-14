package com.meession.etm.module.bpm.service.task;

import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.bpm.dal.dataobject.task.BpmProcessCommentDO;
import com.meession.etm.module.bpm.dal.mysql.task.BpmProcessCommentMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static com.meession.etm.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Service
@Validated
@Slf4j
public class BpmProcessCommentServiceImpl implements BpmProcessCommentService {

    @Resource
    private BpmProcessCommentMapper processCommentMapper;

    @Override
    public BpmProcessCommentDO createProcessComment(String processInstanceId, String taskId, String content) {
        BpmProcessCommentDO comment = new BpmProcessCommentDO();
        comment.setProcessInstanceId(processInstanceId);
        comment.setTaskId(taskId);
        comment.setUserId(getLoginUserId());
        comment.setContent(content);
        processCommentMapper.insert(comment);
        return comment;
    }

    @Override
    public List<BpmProcessCommentDO> getProcessCommentList(String processInstanceId) {
        return processCommentMapper.selectListByProcessInstanceId(processInstanceId);
    }

    @Override
    public void deleteProcessComment(Long id) {
        processCommentMapper.deleteById(id);
    }

}