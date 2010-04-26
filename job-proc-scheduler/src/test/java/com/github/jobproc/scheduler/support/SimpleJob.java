package com.github.jobproc.scheduler.support;

import com.github.jobproc.scheduler.IJob;
import com.github.jobproc.scheduler.IJobContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class SimpleJob<TestJobParameters> implements IJob {

    private final Logger LOG = LoggerFactory.getLogger(SimpleJob.class);

    public void run(IJobContext aContext, Object aParameters) throws Exception {
        
    }
}
