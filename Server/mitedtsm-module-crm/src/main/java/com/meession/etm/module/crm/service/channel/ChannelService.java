package com.meession.etm.module.crm.service.channel;

import java.util.Map;

/**
 * 统一发送渠道接口
 * 封装 SMS/Email 底层 API，营销群发和自动关怀共用
 */
public interface ChannelService {

    /** 单条短信发送 */
    boolean sendSms(String mobile, String templateCode, Map<String, Object> params);

    /** 单条邮件发送 */
    boolean sendMail(String email, String templateCode, Map<String, Object> params);

    /** 给 HTML 邮件内容植入追踪像素（统计打开） */
    String injectTrackingPixel(String htmlContent, Long taskId, Long customerId);

    /** 给 HTML 邮件链接加上点击追踪参数 */
    String injectClickTracking(String htmlContent, Long taskId, Long customerId);

}
