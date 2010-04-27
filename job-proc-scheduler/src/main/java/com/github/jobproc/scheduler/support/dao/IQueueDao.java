package com.github.jobproc.scheduler.support.dao;

import java.util.List;

/**
 * Queue in database
 */
public interface IQueueDao {

    /**
     * Register scheduler
     * @param aSchedulerId scheduler id
     * @param aJobDescriptions descriptions
     */
    void registerScheduler(String aSchedulerId, List<TJobDescription> aJobDescriptions);

    /**
     * Put to queue job
     *
     * @param aJobInfo job
     */
    void push(TJobInfo aJobInfo);

    /**
     * Pulls jobs to run
     *  
     * @param aSchedulerId scheduler id
     * @param aCount count jobs to execute
     * @return jobs to run
     */
    List<TJobInfo> pull(String aSchedulerId, int aCount);

    /**
     * Sets job state to COMPLETED
     * 
     * @param aJobId job id
     * @param aStatus status
     * @param aErrorMessage error message
     */
    void setJobCompleted(long aJobId, String aStatus, String aErrorMessage);
}
