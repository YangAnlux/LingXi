package com.meession.etm.module.iot.framework.job.config;

import com.meession.etm.module.iot.framework.job.core.IotSchedulerManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * IoT 模块的 Job 自动配置类
 *
 * @author 密讯
 */
@Configuration
@Slf4j
public class IotJobConfiguration {

    @Bean(initMethod = "start", destroyMethod = "stop")
    @ConditionalOnProperty(prefix = "mitedtsm.iot", name = "quartz-enabled", havingValue = "true", matchIfMissing = false)
    public IotSchedulerManager iotSchedulerManager(ApplicationContext applicationContext) {
        return new IotSchedulerManager(applicationContext);
    }

}