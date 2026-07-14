package com.meession.etm.module.bpm.controller.admin.task;

import cn.hutool.core.collection.CollUtil;
import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.bpm.controller.admin.base.user.UserSimpleBaseVO;
import com.meession.etm.module.bpm.controller.admin.task.vo.comment.BpmProcessCommentCreateReqVO;
import com.meession.etm.module.bpm.controller.admin.task.vo.comment.BpmProcessCommentRespVO;
import com.meession.etm.module.bpm.dal.dataobject.task.BpmProcessCommentDO;
import com.meession.etm.module.bpm.service.task.BpmProcessCommentService;
import com.meession.etm.module.system.api.user.AdminUserApi;
import com.meession.etm.module.system.api.user.dto.AdminUserRespDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.meession.etm.framework.common.pojo.CommonResult.success;
import static com.meession.etm.framework.common.util.collection.CollectionUtils.convertList;

@Tag(name = "管理后台 - 流程评论")
@RestController
@RequestMapping("/bpm/process-comment")
@Validated
public class BpmProcessCommentController {

    @Resource
    private BpmProcessCommentService processCommentService;

    @Resource
    private AdminUserApi adminUserApi;

    @PostMapping("/create")
    @Operation(summary = "添加流程评论")
    @PreAuthorize("@ss.hasPermission('bpm:process-instance:query')")
    public CommonResult<BpmProcessCommentRespVO> createProcessComment(
            @Valid @RequestBody BpmProcessCommentCreateReqVO reqVO) {
        BpmProcessCommentDO comment = processCommentService.createProcessComment(
                reqVO.getProcessInstanceId(), reqVO.getTaskId(), reqVO.getContent());
        return success(convertComment(comment));
    }

    @GetMapping("/list")
    @Operation(summary = "获取流程评论列表")
    @PreAuthorize("@ss.hasPermission('bpm:process-instance:query')")
    public CommonResult<List<BpmProcessCommentRespVO>> getProcessCommentList(
            @RequestParam("processInstanceId") String processInstanceId) {
        List<BpmProcessCommentDO> list = processCommentService.getProcessCommentList(processInstanceId);
        if (CollUtil.isEmpty(list)) {
            return success(List.of());
        }
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(convertList(list, BpmProcessCommentDO::getUserId));
        return success(convertList(list, comment -> {
            BpmProcessCommentRespVO respVO = BeanUtils.toBean(comment, BpmProcessCommentRespVO.class);
            AdminUserRespDTO user = userMap.get(comment.getUserId());
            if (user != null) {
                respVO.setUser(BeanUtils.toBean(user, UserSimpleBaseVO.class));
            }
            return respVO;
        }));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除流程评论")
    @PreAuthorize("@ss.hasPermission('bpm:process-instance:query')")
    public CommonResult<Boolean> deleteProcessComment(@RequestParam("id") Long id) {
        processCommentService.deleteProcessComment(id);
        return success(true);
    }

    private BpmProcessCommentRespVO convertComment(BpmProcessCommentDO comment) {
        BpmProcessCommentRespVO respVO = BeanUtils.toBean(comment, BpmProcessCommentRespVO.class);
        AdminUserRespDTO user = adminUserApi.getUser(comment.getUserId());
        if (user != null) {
            respVO.setUser(BeanUtils.toBean(user, UserSimpleBaseVO.class));
        }
        return respVO;
    }

}
