package com.meession.etm.module.bpm.controller.admin.task.vo.comment;

import com.meession.etm.module.bpm.controller.admin.base.user.UserSimpleBaseVO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BpmProcessCommentRespVO {

    private Long id;

    private String processInstanceId;

    private String taskId;

    private Long userId;

    private String content;

    private LocalDateTime createTime;

    private UserSimpleBaseVO user;

}