/*
 * Copyright (c) 2017. @author lev-gc
 */

package com.lesson.learn.quartz.job;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <b>QuartzScheduler</b>
 * <p/>
 * Scheduler
 * <p/>
 *
 * @author lev-gc
 * @version 0.1.0
 * @date 2017/9/4 15:59
 */
@Component
public class QuartzScheduler {

    /**
     * The constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(QuartzScheduler.class);

    /**
     * The Scheduler.
     */
    private final Scheduler scheduler;

    /**
     * Instantiates a new Quartz scheduler.
     *
     * @param scheduler the scheduler
     */
    @Autowired
    public QuartzScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * Start.
     *
     * @throws InterruptedException the interrupted exception
     * @throws SchedulerException   the scheduler exception
     */
    public void start() throws InterruptedException, SchedulerException {
        LOG.info("Quartz Scheduler Startup!");
        for (int i = 0; i < 20; i++) {
            long updateInterval = 1;
            String jobId = String.valueOf(i + 1);

            JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put("id", jobId);
            // TODO: add what you need in QuartzJob to JobDataMap

            JobDetail jobDetail = JobBuilder.newJob(QuartzJob.class)
                    .usingJobData(jobDataMap)
                    .withIdentity(jobId)
                    .build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(jobDetail)
                    .withSchedule(SimpleScheduleBuilder.repeatMinutelyForever((int) updateInterval)
                            .withMisfireHandlingInstructionNextWithRemainingCount())
                    // TODO: you can also use CronScheduleBuilder if necessary:
                    // TODO: CronScheduleBuilder.cronSchedule("0 0/" + updateInterval + " * * * ?").withMisfireHandlingInstructionFireAndProceed()
                    .withIdentity(jobId + "_trigger")
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
            // TODO: you can add time slot to all these jobs by this:
            // TODO: Thread.sleep(1000);
        }
        // TODO: if AutoStartup of scheduler is false, start it by this:
        // TODO: scheduler.start();
    }

    /**
     * Stop.
     */
    public void stop() throws SchedulerException {
        scheduler.shutdown();
    }

}
