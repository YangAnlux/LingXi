package com.meession.etm.module.crm.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * CRM 模块 RabbitMQ 配置
 * 声明群发消息队列、交换机与绑定关系
 */
@Configuration
public class CrmRabbitMQConfiguration {

    public static final String EXCHANGE_NAME = "crm.send.exchange";

    public static final String SMS_QUEUE = "crm.sms.queue";
    public static final String EMAIL_QUEUE = "crm.email.queue";

    public static final String SMS_ROUTING_KEY = "crm.send.sms";
    public static final String EMAIL_ROUTING_KEY = "crm.send.email";

    @Bean
    public DirectExchange crmSendExchange() {
        return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue crmSmsQueue() {
        return new Queue(SMS_QUEUE, true);
    }

    @Bean
    public Queue crmEmailQueue() {
        return new Queue(EMAIL_QUEUE, true);
    }

    @Bean
    public Binding crmSmsBinding() {
        return BindingBuilder.bind(crmSmsQueue()).to(crmSendExchange()).with(SMS_ROUTING_KEY);
    }

    @Bean
    public Binding crmEmailBinding() {
        return BindingBuilder.bind(crmEmailQueue()).to(crmSendExchange()).with(EMAIL_ROUTING_KEY);
    }

}
