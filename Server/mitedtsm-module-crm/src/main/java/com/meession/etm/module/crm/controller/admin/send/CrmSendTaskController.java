package com.meession.etm.module.crm.controller.admin.send;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.send.vo.CrmSendTaskCreateReqVO;
import com.meession.etm.module.crm.controller.admin.send.vo.CrmSendTaskPageReqVO;
import com.meession.etm.module.crm.controller.admin.send.vo.CrmSendTaskRespVO;
import com.meession.etm.module.crm.service.send.CrmSendTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.meession.etm.framework.common.pojo.CommonResult.success;
import static com.meession.etm.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - 发送任务管理")
@RestController
@RequestMapping("/crm/send-task")
@Validated
public class CrmSendTaskController {

    @Resource
    private CrmSendTaskService sendTaskService;

    @PostMapping("/create")
    @Operation(summary = "创建发送任务（群发/关怀4步向导提交）")
    @PreAuthorize("@ss.hasPermission('crm:send-task:create')")
    public CommonResult<CrmSendTaskRespVO> createSendTask(@Valid @RequestBody CrmSendTaskCreateReqVO reqVO) {
        CrmSendTaskRespVO resp = sendTaskService.createTask(reqVO, getLoginUserId());
        // hasWarning 通过响应体传递，前端判断后弹出确认框
        return success(resp);
    }

    @GetMapping("/page")
    @Operation(summary = "发送任务分页查询")
    @PreAuthorize("@ss.hasPermission('crm:send-task:query')")
    public CommonResult<PageResult<CrmSendTaskRespVO>> getSendTaskPage(@Valid CrmSendTaskPageReqVO pageVO) {
        return success(sendTaskService.getTaskPage(pageVO));
    }

    @GetMapping("/get")
    @Operation(summary = "获取发送任务详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('crm:send-task:query')")
    public CommonResult<CrmSendTaskRespVO> getSendTask(@RequestParam("id") Long id) {
        return success(sendTaskService.getTask(id));
    }

}
