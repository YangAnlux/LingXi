package com.meession.etm.module.bpm.dal.dataobject.task;

import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName(value = "bpm_process_comment", autoResultMap = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BpmProcessCommentDO extends BaseDO {

    @TableId
    private Long id;

    private String processInstanceId;

    private String taskId;

    private Long userId;

    private String content;

}