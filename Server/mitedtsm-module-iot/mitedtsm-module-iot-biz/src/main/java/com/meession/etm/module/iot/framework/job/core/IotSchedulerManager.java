package com.meession.etm.module.iot.framework.job.core;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import java.util.Map;
import java.util.Properties;

/**
 * IoT 模块的 Scheduler 管理类，基于 Quartz 实现
 *
 * @author 密讯
 */
@Slf4j
public class IotSchedulerManager {

    private static final String SCHEDULER_NAME = "iotScheduler";

    private final SchedulerFactoryBean schedulerFactoryBean;

    private Scheduler scheduler;

    public IotSchedulerManager(ApplicationContext applicationContext) {
        log.info("[IotSchedulerManager][使用内存存储模式，不依赖数据库]");
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        SpringBeanJobFactory jobFactory = new SpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        schedulerFactoryBean.setJobFactory(jobFactory);
        schedulerFactoryBean.setAutoStartup(true);
        schedulerFactoryBean.setSchedulerName(SCHEDULER_NAME);
        schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(true);
        Properties properties = new Properties();
        schedulerFactoryBean.setQuartzProperties(properties);
        properties.put("org.quartz.scheduler.instanceName", SCHEDULER_NAME);
        properties.put("org.quartz.scheduler.instanceId", "AUTO");
        properties.put("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
        properties.put("org.quartz.threadPool.threadCount", "25");
        properties.put("org.quartz.threadPool.threadPriority", "5");
        properties.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        this.schedulerFactoryBean = schedulerFactoryBean;
    }

    public void start() throws Exception {
        log.info("[start][Scheduler 初始化开始]");
        schedulerFactoryBean.afterPropertiesSet();
        schedulerFactoryBean.start();
        this.scheduler = schedulerFactoryBean.getScheduler();
        log.info("[start][Scheduler 初始化完成]");
    }

    public void stop() {
        log.info("[stop][Scheduler 关闭开始]");
        schedulerFactoryBean.stop();
        this.scheduler = null;
        log.info("[stop][Scheduler 关闭完成]");
    }

    public void addOrUpdateJob(Class <? extends Job> jobClass, String jobName,
                               String cronExpression, Map<String, Object> jobDataMap)
            throws SchedulerException {
        if (scheduler.checkExists(new JobKey(jobName))) {
            this.updateJob(jobName, cronExpression);
        } else {
            this.addJob(jobClass, jobName, cronExpression, jobDataMap);
        }
    }

    public void addJob(Class <? extends Job> jobClass, String jobName,
                       String cronExpression, Map<String, Object> jobDataMap)
            throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .usingJobData(new JobDataMap(jobDataMap))
                .withIdentity(jobName).build();
        Trigger trigger = this.buildTrigger(jobName, cronExpression);
        scheduler.scheduleJob(jobDetail, trigger);
    }

    public void updateJob(String jobName, String cronExpression)
            throws SchedulerException {
        Trigger newTrigger = this.buildTrigger(jobName, cronExpression);
        scheduler.rescheduleJob(new TriggerKey(jobName), newTrigger);
    }

    public void deleteJob(String jobName) throws SchedulerException {
        scheduler.pauseTrigger(new TriggerKey(jobName));
        scheduler.unscheduleJob(new TriggerKey(jobName));
        scheduler.deleteJob(new JobKey(jobName));
    }

    public void pauseJob(String jobName) throws SchedulerException {
        scheduler.pauseJob(new JobKey(jobName));
    }

    public void resumeJob(String jobName) throws SchedulerException {
        scheduler.resumeJob(new JobKey(jobName));
        scheduler.resumeTrigger(new TriggerKey(jobName));
    }

    public void triggerJob(String jobName) throws SchedulerException {
        scheduler.triggerJob(new JobKey(jobName));
    }

    private Trigger buildTrigger(String jobName, String cronExpression) {
        return TriggerBuilder.newTrigger()
                .withIdentity(jobName)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();
    }

}