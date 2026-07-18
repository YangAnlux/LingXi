package com.meession.etm.module.crm.service.channel;

import com.meession.etm.module.system.api.mail.MailSendApi;
import com.meession.etm.module.system.api.mail.dto.MailSendSingleToUserReqDTO;
import com.meession.etm.module.system.api.sms.SmsSendApi;
import com.meession.etm.module.system.api.sms.dto.send.SmsSendSingleToUserReqDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class ChannelServiceImpl implements ChannelService {

    /** 链接匹配正则 */
    private static final Pattern HREF_PATTERN = Pattern.compile(
            "<a\\s+[^>]*href\\s*=\\s*[\"']([^\"']+)[\"']", Pattern.CASE_INSENSITIVE);

    @Resource
    private SmsSendApi smsSendApi;
    @Resource
    private MailSendApi mailSendApi;

    @Override
    public boolean sendSms(String mobile, String templateCode, Map<String, Object> params) {
        try {
            smsSendApi.sendSingleSmsToAdmin(new SmsSendSingleToUserReqDTO()
                    .setMobile(mobile)
                    .setTemplateCode(templateCode)
                    .setTemplateParams(params));
            return true;
        } catch (Exception e) {
            log.error("[ChannelService][短信发送失败 mobile={}]", mobile, e);
            return false;
        }
    }

    @Override
    public boolean sendMail(String email, String templateCode, Map<String, Object> params) {
        try {
            MailSendSingleToUserReqDTO req = new MailSendSingleToUserReqDTO();
            req.setToMails(List.of(email));
            req.setTemplateCode(templateCode);
            req.setTemplateParams(params);
            mailSendApi.sendSingleMailToAdmin(req);
            return true;
        } catch (Exception e) {
            log.error("[ChannelService][邮件发送失败 email={}]", email, e);
            return false;
        }
    }

    @Override
    public String injectTrackingPixel(String htmlContent, Long taskId, Long customerId) {
        if (htmlContent == null) return null;
        String pixel = "<img src=\"/api/track/open/" + taskId + "/" + customerId
                + ".gif\" width=\"1\" height=\"1\" style=\"display:none;\" />";
        // 在 </body> 前插入，若无则追加到末尾
        if (htmlContent.contains("</body>")) {
            return htmlContent.replace("</body>", pixel + "</body>");
        }
        return htmlContent + pixel;
    }

    @Override
    public String injectClickTracking(String htmlContent, Long taskId, Long customerId) {
        if (htmlContent == null) return null;
        Matcher m = HREF_PATTERN.matcher(htmlContent);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String originalUrl = m.group(1);
            String trackedUrl = "/api/track/click/" + taskId + "/" + customerId
                    + "?url=" + java.net.URLEncoder.encode(originalUrl, java.nio.charset.StandardCharsets.UTF_8);
            m.appendReplacement(sb, "<a href=\"" + trackedUrl + "\"");
        }
        m.appendTail(sb);
        return sb.toString();
    }

}
