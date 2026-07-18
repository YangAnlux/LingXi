package com.meession.etm.module.iot.framework.tdengine.config;

import com.meession.etm.module.iot.service.device.message.IotDeviceMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TDengineTableInitRunner implements ApplicationRunner {

    private final IotDeviceMessageService deviceMessageService;

    @Override
    public void run(ApplicationArguments args) {
        try {
            deviceMessageService.defineDeviceMessageStable();
            log.info("[run][TDengine初始化设备消息表结构成功]");
        } catch (Exception ex) {
            // 修改：TDengine不可用时不强制退出，只记录警告日志
            log.warn("[run][TDengine初始化设备消息表结构失败，IoT模块设备消息功能将不可用，请检查TDengine服务是否正常启动]", ex);
            // System.exit(1); // 删除强制退出
        }
    }

}