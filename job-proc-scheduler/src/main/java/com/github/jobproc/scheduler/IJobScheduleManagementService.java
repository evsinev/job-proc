package com.github.jobproc.scheduler;

/**
 * Manages schedule service
 */
public interface IJobScheduleManagementService {
    
    void registerJob(IJob aJob);

    void start();

    void stop();

}
