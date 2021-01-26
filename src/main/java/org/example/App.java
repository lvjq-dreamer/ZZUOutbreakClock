package org.example;

import org.example.job.ClockInJob;
import org.example.job.ResettingJob;
import org.example.job.SendMailJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 启动类
 * @author 沉晓
 */
public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) throws SchedulerException {
        log.info("疫情打卡系统启动");
        resettingJob();
        clockInJob();
        sendMail();
    }

    /**
     * 打卡操作
     * @throws SchedulerException
     */
    public static void clockInJob() throws SchedulerException {
        log.info("打卡-定时任务初始化");
        JobDetail clockInJobDetail = JobBuilder.newJob(ClockInJob.class).withIdentity("clockInJob","group1").build();

        // 创建一个Trigger实例
        Trigger clockInTrigger = TriggerBuilder.newTrigger()
                .withIdentity("clockInTrigger", "group1").startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/20 0,1,5,6,7,8 * * ? *"))
                .build();
        SchedulerFactory clockInSfact = new StdSchedulerFactory();
        Scheduler clockInScheduler = clockInSfact.getScheduler();
        clockInScheduler.start();
        clockInScheduler.scheduleJob(clockInJobDetail, clockInTrigger);
        log.info("打卡-定时任务初始化完成");
    }

    /**
     * 重置打卡操作
     * @throws SchedulerException
     */
    public static void resettingJob() throws SchedulerException {
        log.info("重置-定时任务初始化");
        JobDetail resettingJobDetail = JobBuilder.newJob(ResettingJob.class).withIdentity("resettingJob","group2").build();
        // 创建一个Trigger实例
        Trigger resettingTrigger = TriggerBuilder.newTrigger()
                .withIdentity("resettingTrigger", "group2").startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule("0 30 10,14,18,22 * * ? *"))
                .build();
        SchedulerFactory resettingSfact = new StdSchedulerFactory();
        Scheduler resettingScheduler = resettingSfact.getScheduler();
        resettingScheduler.start();
        resettingScheduler.scheduleJob(resettingJobDetail, resettingTrigger);
        log.info("重置-定时任务初始化完成");
    }

    /**
     * 发送邮件
     * @throws SchedulerException
     */
    public static void sendMail() throws SchedulerException {
        log.info("发送邮件-定时任务初始化");
        JobDetail sendMailJobDetail = JobBuilder.newJob(SendMailJob.class).withIdentity("sendMailJob","group3").build();
        // 创建一个Trigger实例
        Trigger sendMailTrigger = TriggerBuilder.newTrigger()
                .withIdentity("sendMailTrigger", "group3").startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/20 9 * * ? *"))
                .build();
        SchedulerFactory sendMailSfact = new StdSchedulerFactory();
        Scheduler sendMailScheduler = sendMailSfact.getScheduler();
        sendMailScheduler.start();
        sendMailScheduler.scheduleJob(sendMailJobDetail, sendMailTrigger);
        log.info("发送邮件-定时任务初始化完成");
    }
}
