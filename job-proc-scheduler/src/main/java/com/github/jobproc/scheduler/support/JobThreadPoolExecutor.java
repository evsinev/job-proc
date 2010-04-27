package com.github.jobproc.scheduler.support;

import com.github.jobproc.scheduler.support.dao.IQueueDao;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class JobThreadPoolExecutor extends ThreadPoolExecutor {

    public JobThreadPoolExecutor(IQueueDao aQueueDao, int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        theQueueDao = aQueueDao;
    }

    @Override
    protected void afterExecute(Runnable aRunnable, Throwable aException) {
        JobTask jobTask = (JobTask) aRunnable;
        if(aException!=null) {
            theQueueDao.setJobCompleted(jobTask.getJobId(), "E", aException.getMessage());
        } else {
            theQueueDao.setJobCompleted(jobTask.getJobId(), "Y", null);
        }
    }

    private final IQueueDao theQueueDao;
}
