package com.meession.etm.module.iot.framework.tdengine.config;

import com.meession.etm.module.iot.service.device.message.IotDeviceMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

/**
 * TDengine 表初始化的 Configuration
 *
 * @author alwayssuper
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TDengineTableInitRunner implements ApplicationRunner {

    private final IotDeviceMessageService deviceMessageService;

    @Override
    public void run(ApplicationArguments args) {
        log.info("[run][TDengine表初始化已跳过，本地开发环境]");
    }

}
