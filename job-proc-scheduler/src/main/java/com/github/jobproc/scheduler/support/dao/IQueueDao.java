package com.github.jobproc.scheduler.support.dao;

import java.util.List;

/**
 * Queue in database
 */
public interface IQueueDao {

    /**
     * Put to queue job
     *
     * @param aJobInfo job
     */
    void push(TJobInfo aJobInfo);


    /**
     * Pulls jobs to run
     *  
     * @return jobs to run
     */
    List<TJobInfo> pull(int aCount);
}
