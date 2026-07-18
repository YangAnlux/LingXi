package com.meession.etm.module.crm.service.task;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.task.vo.task.CrmTaskPageReqVO;
import com.meession.etm.module.crm.controller.admin.task.vo.task.CrmTaskSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.task.CrmTaskDO;

import jakarta.validation.Valid;

public interface CrmTaskService {

    Long createTask(@Valid CrmTaskSaveReqVO createReqVO, Long userId);

    void updateTask(@Valid CrmTaskSaveReqVO updateReqVO);

    void deleteTask(Long id);

    CrmTaskDO getTask(Long id);

    PageResult<CrmTaskDO> getTaskPage(CrmTaskPageReqVO pageReqVO);

    void updateTaskStatus(Long id, Integer status);

}
