package com.github.jobproc.scheduler;

/**
 * Manages schedule service
 */
public interface IJobScheduleManagementService {
    
    void registerJob(IJob aJob, JobDescription aDescription);

    void start();

    void stop();

}
