package com.meession.etm.module.bpm.controller.admin.task.vo.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BpmProcessCommentCreateReqVO {

    @NotBlank(message = "流程实例编号不能为空")
    private String processInstanceId;

    private String taskId;

    @NotBlank(message = "评论内容不能为空")
    private String content;

}