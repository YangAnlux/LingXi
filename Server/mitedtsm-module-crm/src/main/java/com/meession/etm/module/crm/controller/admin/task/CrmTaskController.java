package com.meession.etm.module.crm.controller.admin.task;

import cn.hutool.core.collection.CollUtil;
import com.meession.etm.framework.apilog.core.annotation.ApiAccessLog;
import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.collection.MapUtils;
import com.meession.etm.framework.common.util.number.NumberUtils;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.framework.excel.core.util.ExcelUtils;
import com.meession.etm.module.crm.controller.admin.task.vo.task.CrmTaskPageReqVO;
import com.meession.etm.module.crm.controller.admin.task.vo.task.CrmTaskRespVO;
import com.meession.etm.module.crm.controller.admin.task.vo.task.CrmTaskSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.task.CrmTaskDO;
import com.meession.etm.module.crm.service.task.CrmTaskService;
import com.meession.etm.module.system.api.user.AdminUserApi;
import com.meession.etm.module.system.api.user.dto.AdminUserRespDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.meession.etm.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static com.meession.etm.framework.common.pojo.CommonResult.success;
import static com.meession.etm.framework.common.pojo.PageParam.PAGE_SIZE_NONE;
import static com.meession.etm.framework.common.util.collection.CollectionUtils.convertListByFlatMap;
import static com.meession.etm.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static java.util.Collections.singletonList;

@Tag(name = "管理后台 - 任务管理")
@RestController
@RequestMapping("/crm/task")
@Validated
public class CrmTaskController {

    @Resource
    private CrmTaskService taskService;

    @Resource
    private AdminUserApi adminUserApi;

    @PostMapping("/create")
    @Operation(summary = "创建任务")
    @PreAuthorize("@ss.hasPermission('crm:task:create')")
    public CommonResult<Long> createTask(@Valid @RequestBody CrmTaskSaveReqVO createReqVO) {
        return success(taskService.createTask(createReqVO, getLoginUserId()));
    }

    @PutMapping("/update")
    @Operation(summary = "更新任务")
    @PreAuthorize("@ss.hasPermission('crm:task:update')")
    public CommonResult<Boolean> updateTask(@Valid @RequestBody CrmTaskSaveReqVO updateReqVO) {
        taskService.updateTask(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除任务")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('crm:task:delete')")
    public CommonResult<Boolean> deleteTask(@RequestParam("id") Long id) {
        taskService.deleteTask(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得任务")
    @Parameter(name = "id", description = "编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('crm:task:query')")
    public CommonResult<CrmTaskRespVO> getTask(@RequestParam("id") Long id) {
        CrmTaskDO task = taskService.getTask(id);
        return success(buildTaskDetail(task));
    }

    @GetMapping("/page")
    @Operation(summary = "获得任务分页")
    @PreAuthorize("@ss.hasPermission('crm:task:query')")
    public CommonResult<PageResult<CrmTaskRespVO>> getTaskPage(@Valid CrmTaskPageReqVO pageVO) {
        PageResult<CrmTaskDO> pageResult = taskService.getTaskPage(pageVO);
        return success(new PageResult<>(buildTaskDetailList(pageResult.getList()), pageResult.getTotal()));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出任务 Excel")
    @PreAuthorize("@ss.hasPermission('crm:task:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportTaskExcel(@Valid CrmTaskPageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PAGE_SIZE_NONE);
        List<CrmTaskDO> list = taskService.getTaskPage(pageReqVO).getList();
        ExcelUtils.write(response, "任务.xls", "数据", CrmTaskRespVO.class, buildTaskDetailList(list));
    }

    @PutMapping("/status")
    @Operation(summary = "更新任务状态")
    @Parameter(name = "id", description = "编号", required = true)
    @Parameter(name = "status", description = "状态 0-待办 1-进行中 2-已完成 3-已取消", required = true)
    @PreAuthorize("@ss.hasPermission('crm:task:update')")
    public CommonResult<Boolean> updateTaskStatus(@RequestParam("id") Long id, @RequestParam("status") Integer status) {
        taskService.updateTaskStatus(id, status);
        return success(true);
    }

    private CrmTaskRespVO buildTaskDetail(CrmTaskDO task) {
        if (task == null) {
            return null;
        }
        return buildTaskDetailList(singletonList(task)).get(0);
    }

    private List<CrmTaskRespVO> buildTaskDetailList(List<CrmTaskDO> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(convertListByFlatMap(list,
                task -> Stream.of(task.getOwnerUserId(), task.getAssignerUserId(),
                        NumberUtils.parseLong(task.getCreator()))));
        return BeanUtils.toBean(list, CrmTaskRespVO.class, taskVO -> {
            // 将 epoch 0 时间转为 null
            if (taskVO.getStartTime() != null
                    && taskVO.getStartTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() < 86400000L) {
                taskVO.setStartTime(null);
            }
            if (taskVO.getEndTime() != null
                    && taskVO.getEndTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() < 86400000L) {
                taskVO.setEndTime(null);
            }
            if (taskVO.getCompletedTime() != null
                    && taskVO.getCompletedTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() < 86400000L) {
                taskVO.setCompletedTime(null);
            }
            MapUtils.findAndThen(userMap, taskVO.getOwnerUserId(),
                    user -> taskVO.setOwnerUserName(user.getNickname()));
            MapUtils.findAndThen(userMap, taskVO.getAssignerUserId(),
                    user -> taskVO.setAssignerUserName(user.getNickname()));
            MapUtils.findAndThen(userMap, NumberUtils.parseLong(taskVO.getCreator()),
                    user -> taskVO.setCreatorName(user.getNickname()));
        });
    }

}
