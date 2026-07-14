package com.meession.etm.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;

/**
 * 项目的启动类
 *
 * @author 密讯
 */
@SuppressWarnings("SpringComponentScan")
@SpringBootApplication(
        scanBasePackages = {"${mitedtsm.info.base-package}.server", "${mitedtsm.info.base-package}.module"},
        exclude = {
                RabbitAutoConfiguration.class
        },
        excludeName = {
                "com.meession.etm.framework.encrypt.config.MitedtsmApiEncryptAutoConfiguration"
        }
)
public class MitedtsmServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MitedtsmServerApplication.class, args);
    }

}
