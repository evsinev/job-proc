package com.github.jobproc.scheduler.support;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Test
 */
public class JobSchedulerServiceImplTest {

    private final Logger LOG = LoggerFactory.getLogger(JobSchedulerServiceImplTest.class);

    @Test
    public void test() throws IOException, InterruptedException {

        final int COUNT = 20;

        final CountDownLatch latch = new CountDownLatch(COUNT);

        final JobSchedulerServiceImpl scheduler = new JobSchedulerServiceImpl(new SimpleQueueDao());
        scheduler.setThreadsCount(10);
        scheduler.registerJob(new SimpleJob(1000, latch));

        Thread t = new Thread(new Runnable() {
            public void run() {
                scheduler.start();

                for(int i=0; i<COUNT; i++) {
                    scheduler.scheduleJob(SimpleJob.class, new SimpleJobParameters(i+1));
                }
            }
        });
        t.start();

        LOG.info("Waiting 10sec ...");
        latch.await(10, TimeUnit.SECONDS);

        scheduler.stop();

    }

}
