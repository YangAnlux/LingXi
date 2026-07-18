package com.meession.etm.module.crm.service.send;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.send.vo.CrmSendTaskCreateReqVO;
import com.meession.etm.module.crm.controller.admin.send.vo.CrmSendTaskPageReqVO;
import com.meession.etm.module.crm.controller.admin.send.vo.CrmSendTaskRespVO;

import jakarta.validation.Valid;

/**
 * CRM 发送任务 Service 接口
 */
public interface CrmSendTaskService {

    /**
     * 创建发送任务
     * @return 任务响应 VO（含 hasWarning 标记表示有变量缺失）
     */
    CrmSendTaskRespVO createTask(@Valid CrmSendTaskCreateReqVO reqVO, Long userId);

    /**
     * 发送任务分页查询
     */
    PageResult<CrmSendTaskRespVO> getTaskPage(CrmSendTaskPageReqVO pageReqVO);

    /**
     * 获取任务详情
     */
    CrmSendTaskRespVO getTask(Long id);

}
