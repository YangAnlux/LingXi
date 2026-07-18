package com.meession.etm.module.crm.service.statistics;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.meession.etm.module.crm.controller.admin.send.vo.CrmSendTaskPageReqVO;
import com.meession.etm.module.crm.dal.dataobject.campaign.CrmCampaignDO;
import com.meession.etm.module.crm.dal.dataobject.campaign.CrmCampaignSendLogDO;
import com.meession.etm.module.crm.dal.dataobject.send.CrmSendTaskDO;
import com.meession.etm.module.crm.dal.mysql.campaign.CrmCampaignMapper;
import com.meession.etm.module.crm.dal.mysql.campaign.CrmCampaignSendLogMapper;
import com.meession.etm.module.crm.dal.mysql.send.CrmSendTaskMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CrmSendStatisticsServiceImpl implements CrmSendStatisticsService {

    @Resource
    private CrmCampaignSendLogMapper sendLogMapper;
    @Resource
    private CrmCampaignMapper crmCampaignMapper;
    @Resource
    private CrmSendTaskMapper sendTaskMapper;

    @Override
    public Map<String, Object> getSendStatistics(Long campaignId) {
        List<CrmCampaignSendLogDO> logs = campaignId != null
                ? sendLogMapper.selectListByCampaignId(campaignId)
                : sendLogMapper.selectList(new MPJLambdaWrapperX<>());

        int totalSms = 0, smsSuccess = 0, smsFail = 0;
        int totalMail = 0, mailSuccess = 0, mailFail = 0;

        for (CrmCampaignSendLogDO log : logs) {
            if (log.getChannel() == 1) {
                totalSms += log.getTotalCount() != null ? log.getTotalCount() : 0;
                smsSuccess += log.getSuccessCount() != null ? log.getSuccessCount() : 0;
                smsFail += log.getFailCount() != null ? log.getFailCount() : 0;
            } else if (log.getChannel() == 2) {
                totalMail += log.getTotalCount() != null ? log.getTotalCount() : 0;
                mailSuccess += log.getSuccessCount() != null ? log.getSuccessCount() : 0;
                mailFail += log.getFailCount() != null ? log.getFailCount() : 0;
            }
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("totalSends", totalSms + totalMail);
        result.put("smsTotal", totalSms);
        result.put("smsSuccess", smsSuccess);
        result.put("smsFail", smsFail);
        result.put("smsRate", totalSms > 0 ? Math.round(smsSuccess * 10000.0 / totalSms) / 100.0 : 0);
        result.put("mailTotal", totalMail);
        result.put("mailSuccess", mailSuccess);
        result.put("mailFail", mailFail);
        result.put("mailRate", totalMail > 0 ? Math.round(mailSuccess * 10000.0 / totalMail) / 100.0 : 0);
        // 邮件打开率 — 由邮件系统跟踪后回写 open_count 字段，此处聚合真实数据
        int totalOpen = 0, totalClick = 0;
        for (CrmCampaignSendLogDO log : logs) {
            if (log.getChannel() != null && log.getChannel() == 2) {
                totalOpen += log.getOpenCount() != null ? log.getOpenCount() : 0;
                totalClick += log.getClickCount() != null ? log.getClickCount() : 0;
            }
        }
        double mailOpenRate = totalMail > 0 ? Math.round(totalOpen * 10000.0 / totalMail) / 100.0 : 0;
        result.put("mailOpenRate", mailOpenRate);
        result.put("mailOpenCount", totalOpen);
        result.put("mailClickCount", totalClick);
        result.put("logs", logs);
        return result;
    }

    @Override
    public List<Map<String, Object>> getCampaignBreakdown() {
        List<CrmCampaignSendLogDO> logs = sendLogMapper.selectList(
                new MPJLambdaWrapperX<CrmCampaignSendLogDO>().orderByDesc(CrmCampaignSendLogDO::getId));

        // 按 campaignId 分组聚合
        Map<Long, List<CrmCampaignSendLogDO>> grouped = logs.stream()
                .filter(l -> l.getCampaignId() != null)
                .collect(Collectors.groupingBy(CrmCampaignSendLogDO::getCampaignId));

        // 批量加载营销活动名称
        List<Long> campaignIds = new ArrayList<>(grouped.keySet());
        Map<Long, CrmCampaignDO> campaignMap = new HashMap<>();
        if (!campaignIds.isEmpty()) {
            crmCampaignMapper.selectBatchIds(campaignIds)
                    .forEach(c -> campaignMap.put(c.getId(), c));
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<Long, List<CrmCampaignSendLogDO>> entry : grouped.entrySet()) {
            Long cid = entry.getKey();
            List<CrmCampaignSendLogDO> campaignLogs = entry.getValue();

            int totalSms = 0, smsSuccess = 0, smsFail = 0;
            int totalMail = 0, mailSuccess = 0, mailFail = 0;
            for (CrmCampaignSendLogDO log : campaignLogs) {
                if (log.getChannel() == 1) {
                    totalSms += log.getTotalCount() != null ? log.getTotalCount() : 0;
                    smsSuccess += log.getSuccessCount() != null ? log.getSuccessCount() : 0;
                    smsFail += log.getFailCount() != null ? log.getFailCount() : 0;
                } else if (log.getChannel() == 2) {
                    totalMail += log.getTotalCount() != null ? log.getTotalCount() : 0;
                    mailSuccess += log.getSuccessCount() != null ? log.getSuccessCount() : 0;
                    mailFail += log.getFailCount() != null ? log.getFailCount() : 0;
                }
            }

            CrmCampaignDO campaign = campaignMap.get(cid);
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("campaignId", cid);
            item.put("campaignName", campaign != null ? campaign.getName() : "未知活动");
            item.put("totalSends", totalSms + totalMail);
            item.put("smsTotal", totalSms);
            item.put("smsSuccess", smsSuccess);
            item.put("smsFail", smsFail);
            item.put("smsRate", totalSms > 0 ? Math.round(smsSuccess * 10000.0 / totalSms) / 100.0 : 0);
            item.put("mailTotal", totalMail);
            item.put("mailSuccess", mailSuccess);
            item.put("mailFail", mailFail);
            item.put("mailRate", totalMail > 0 ? Math.round(mailSuccess * 10000.0 / totalMail) / 100.0 : 0);
            result.add(item);
        }
        return result;
    }

    @Override
    public Map<String, Object> getDashboard(String bizType, String channel, String startDate, String endDate) {
        // 获取 MySQL 发送日志作为基础数据源（TDengine 聚合作为增强）
        List<CrmCampaignSendLogDO> allLogs = sendLogMapper.selectList(
                new MPJLambdaWrapperX<CrmCampaignSendLogDO>().orderByDesc(CrmCampaignSendLogDO::getId));

        // 今日统计
        LocalDate today = LocalDate.now();
        int todaySent = 0, todaySuccess = 0, todayFail = 0;
        for (CrmCampaignSendLogDO log : allLogs) {
            if (log.getSendTime() != null && log.getSendTime().toLocalDate().equals(today)) {
                todaySent += log.getTotalCount() != null ? log.getTotalCount() : 0;
                todaySuccess += log.getSuccessCount() != null ? log.getSuccessCount() : 0;
                todayFail += log.getFailCount() != null ? log.getFailCount() : 0;
            }
        }
        double todayRate = todaySent > 0 ? Math.round(todaySuccess * 10000.0 / todaySent) / 100.0 : 0;

        // 7天趋势
        List<Map<String, Object>> trend = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate day = today.minusDays(i);
            int dSent = 0, dSuccess = 0, dFail = 0, dOpen = 0;
            for (CrmCampaignSendLogDO log : allLogs) {
                if (log.getSendTime() != null && log.getSendTime().toLocalDate().equals(day)) {
                    dSent += log.getTotalCount() != null ? log.getTotalCount() : 0;
                    dSuccess += log.getSuccessCount() != null ? log.getSuccessCount() : 0;
                    dFail += log.getFailCount() != null ? log.getFailCount() : 0;
                    dOpen += log.getOpenCount() != null ? log.getOpenCount() : 0;
                }
            }
            Map<String, Object> point = new LinkedHashMap<>();
            point.put("date", day.toString());
            point.put("totalCount", dSent);
            point.put("successCount", dSuccess);
            point.put("failCount", dFail);
            point.put("openCount", dOpen);
            trend.add(point);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("todaySent", todaySent);
        result.put("todaySuccess", todaySuccess);
        result.put("todayFail", todayFail);
        result.put("deliverRate", todayRate);
        result.put("trend", trend);
        return result;
    }

    @Override
    public PageResult<Map<String, Object>> getTaskStatistics(CrmSendTaskPageReqVO pageReqVO) {
        // 从 MySQL send_task 表查询任务列表
        PageResult<CrmSendTaskDO> taskPage = sendTaskMapper.selectPage(pageReqVO);

        // 采集所有 send_log 数据用于补充统计
        List<CrmCampaignSendLogDO> allLogs = sendLogMapper.selectList(
                new MPJLambdaWrapperX<CrmCampaignSendLogDO>().orderByDesc(CrmCampaignSendLogDO::getId));

        List<Map<String, Object>> resultList = new ArrayList<>();
        for (CrmSendTaskDO task : taskPage.getList()) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("taskId", task.getId());
            item.put("taskType", task.getTaskType());
            item.put("channel", task.getChannel());
            item.put("templateCode", task.getTemplateCode());
            item.put("customerCount", task.getCustomerCount());
            item.put("status", task.getStatus());
            item.put("createTime", task.getCreateTime());

            // 从 send_log 聚合统计数据
            int total = 0, success = 0, fail = 0, open = 0;
            for (CrmCampaignSendLogDO log : allLogs) {
                if (log.getCampaignId() != null && log.getCampaignId().equals(task.getActivityId())) {
                    total += log.getTotalCount() != null ? log.getTotalCount() : 0;
                    success += log.getSuccessCount() != null ? log.getSuccessCount() : 0;
                    fail += log.getFailCount() != null ? log.getFailCount() : 0;
                    open += log.getOpenCount() != null ? log.getOpenCount() : 0;
                }
            }

            item.put("totalCount", total);
            item.put("successCount", success);
            item.put("failCount", fail);
            item.put("openCount", open);
            item.put("deliverRate", total > 0 ? Math.round(success * 10000.0 / total) / 100.0 : 0);
            // 打开率 = OPEN / SUCCESS，未打开显示 "-"（不计入失败）
            item.put("openRate", success > 0 ? Math.round(open * 10000.0 / success) / 100.0 : null);
            resultList.add(item);
        }

        return new PageResult<>(resultList, taskPage.getTotal());
    }

}
