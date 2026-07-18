package com.meession.etm.module.crm.dal.tdengine;

import com.meession.etm.module.crm.dal.tdengine.CrmSendLogTDengineMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * TDengine 超级表初始化 Runner
 * 应用启动时自动检查并创建 send_logs 超级表
 */
@Component
@Slf4j
public class CrmTDengineTableInitRunner implements ApplicationRunner {

    @Resource
    private CrmSendLogTDengineMapper sendLogTDengineMapper;

    @Override
    public void run(ApplicationArguments args) {
        try {
            int count = sendLogTDengineMapper.showSTable();
            if (count == 0) {
                sendLogTDengineMapper.createSTable();
                log.info("[CrmTDengineTableInitRunner][send_logs 超级表创建成功]");
            } else {
                log.info("[CrmTDengineTableInitRunner][send_logs 超级表已存在，跳过创建]");
            }
        } catch (Exception e) {
            log.warn("[CrmTDengineTableInitRunner][TDengine 初始化失败（可能数据源未配置），跳过: {}]", e.getMessage());
        }
    }

}
