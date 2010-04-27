package com.github.jobproc.scheduler.support;

import com.github.jobproc.scheduler.IJob;
import com.github.jobproc.scheduler.IJobContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 *
 */
public class SimpleJob implements IJob<SimpleJobParameters> {

    private final Logger LOG = LoggerFactory.getLogger(SimpleJob.class);

    public SimpleJob(long aWait, CountDownLatch aLatch) {
        theLatch = aLatch;
        theWait = aWait;
    }

    public void run(IJobContext aContext, SimpleJobParameters aParameters) throws Exception {
        if(LOG.isDebugEnabled()) {
            LOG.debug("Waiting "+theWait);
            Thread.sleep(theWait);
        }
        LOG.info("Simple job done");
        theLatch.countDown();
    }

    private CountDownLatch theLatch;
    private long theWait;
}
