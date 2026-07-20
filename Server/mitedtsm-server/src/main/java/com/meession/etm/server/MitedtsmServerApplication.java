package com.meession.etm.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"${mitedtsm.info.base-package}.server", "${mitedtsm.info.base-package}.module"})
@ComponentScan(basePackages = {"com.meession.etm.module"})
public class MitedtsmServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MitedtsmServerApplication.class, args);
    }
}

