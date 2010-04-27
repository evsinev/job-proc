package com.github.jobproc.scheduler.support;

import com.github.jobproc.scheduler.IJob;
import com.github.jobproc.scheduler.IJobContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JobTask to run job
 */
class JobTask implements Runnable {
    
    private static final Logger LOG = LoggerFactory.getLogger(JobTask.class);

    public JobTask(long aJobId, IJob<Object> aJob, Object aJobParameters, IJobContext aContext) {
        theJobId = aJobId;
        theJob = aJob;
        theJobParameters = aJobParameters;
        theJobContext = aContext;
    }

    public void run() {
        try {
            theJob.run(theJobContext, theJobParameters);
        } catch (Exception e) {
            LOG.error("Error executing " + theJob, e);
        }
    }

    /** Job id */
    public long getJobId() { return theJobId; }

    /** Job id */
    private final long theJobId;

    private final IJob<Object> theJob;
    private final Object theJobParameters;
    private final IJobContext theJobContext;
}
