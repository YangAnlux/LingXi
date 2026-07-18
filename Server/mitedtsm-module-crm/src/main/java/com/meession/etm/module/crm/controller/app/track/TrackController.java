package com.meession.etm.module.crm.controller.app.track;

import com.meession.etm.module.crm.dal.dataobject.campaign.CrmCampaignSendLogDO;
import com.meession.etm.module.crm.dal.mysql.campaign.CrmCampaignSendLogMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 邮件追踪端点（无需认证）
 * 用于统计邮件打开率和点击率
 */
@Tag(name = "App - 邮件追踪")
@RestController
@RequestMapping("/api/track")
@Slf4j
public class TrackController {

    @Resource
    private CrmCampaignSendLogMapper sendLogMapper;

    /** 1x1 透明 GIF（base64） */
    private static final byte[] PIXEL_GIF = {
        (byte) 0x47, (byte) 0x49, (byte) 0x46, (byte) 0x38, (byte) 0x39, (byte) 0x61,
        (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x80, (byte) 0x00,
        (byte) 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x21, (byte) 0xF9, (byte) 0x04, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x2C, (byte) 0x00, (byte) 0x00,
        (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x01, (byte) 0x00,
        (byte) 0x00, (byte) 0x02, (byte) 0x02, (byte) 0x44, (byte) 0x01, (byte) 0x00,
        (byte) 0x3B
    };

    @GetMapping("/open/{taskId}/{customerId}.gif")
    @Operation(summary = "邮件打开追踪（透明GIF）")
    public void trackOpen(@PathVariable Long taskId, @PathVariable Long customerId,
                          HttpServletResponse response) throws IOException {
        log.info("[Track][打开追踪 taskId={}, customerId={}]", taskId, customerId);
        // 更新最近一条匹配的发送日志的 open_time
        updateOpenTime(taskId, customerId);

        response.setContentType("image/gif");
        response.setHeader("Cache-Control", "no-store");
        response.getOutputStream().write(PIXEL_GIF);
    }

    @GetMapping("/click/{taskId}/{customerId}")
    @Operation(summary = "邮件点击追踪（重定向）")
    public void trackClick(@PathVariable Long taskId, @PathVariable Long customerId,
                           @RequestParam("url") String url,
                           HttpServletResponse response) throws IOException {
        log.info("[Track][点击追踪 taskId={}, customerId={}, url={}]", taskId, customerId, url);
        updateClickTime(taskId, customerId);

        response.sendRedirect(url);
    }

    private void updateOpenTime(Long taskId, Long customerId) {
        try {
            CrmCampaignSendLogDO log = sendLogMapper.selectOne(
                    new com.meession.etm.framework.mybatis.core.query.MPJLambdaWrapperX<CrmCampaignSendLogDO>()
                            .eq(CrmCampaignSendLogDO::getCampaignId, taskId)
                            .isNull(CrmCampaignSendLogDO::getOpenTime)
                            .orderByDesc(CrmCampaignSendLogDO::getId)
                            .last("LIMIT 1"));
            if (log != null) {
                log.setOpenTime(LocalDateTime.now());
                log.setOpenCount((log.getOpenCount() != null ? log.getOpenCount() : 0) + 1);
                sendLogMapper.updateById(log);
            }
        } catch (Exception e) {
            log.warn("[Track][更新open_time失败: {}]", e.getMessage());
        }
    }

    private void updateClickTime(Long taskId, Long customerId) {
        try {
            CrmCampaignSendLogDO log = sendLogMapper.selectOne(
                    new com.meession.etm.framework.mybatis.core.query.MPJLambdaWrapperX<CrmCampaignSendLogDO>()
                            .eq(CrmCampaignSendLogDO::getCampaignId, taskId)
                            .isNull(CrmCampaignSendLogDO::getClickTime)
                            .orderByDesc(CrmCampaignSendLogDO::getId)
                            .last("LIMIT 1"));
            if (log != null) {
                log.setClickTime(LocalDateTime.now());
                log.setClickCount((log.getClickCount() != null ? log.getClickCount() : 0) + 1);
                sendLogMapper.updateById(log);
            }
        } catch (Exception e) {
            log.warn("[Track][更新click_time失败: {}]", e.getMessage());
        }
    }

}
