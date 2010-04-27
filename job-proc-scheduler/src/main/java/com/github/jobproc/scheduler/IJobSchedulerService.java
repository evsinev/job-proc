package com.github.jobproc.scheduler;

/**
 * Scheduler
 */
public interface IJobSchedulerService {

    void scheduleJob(Class<? extends IJob> aJobClass, Object aJobParameters);

}
