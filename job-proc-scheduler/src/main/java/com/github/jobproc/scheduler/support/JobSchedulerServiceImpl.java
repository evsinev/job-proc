package com.github.jobproc.scheduler.support;

import com.github.jobproc.scheduler.IJob;
import com.github.jobproc.scheduler.IJobContext;
import com.github.jobproc.scheduler.IJobScheduleManagementService;
import com.github.jobproc.scheduler.IJobSchedulerService;
import com.github.jobproc.scheduler.support.dao.IQueueDao;
import com.github.jobproc.scheduler.support.dao.TJobInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

/**
 * Implementation
 */
public class JobSchedulerServiceImpl implements IJobSchedulerService, IJobScheduleManagementService {

    private final static Logger LOG = LoggerFactory.getLogger(JobSchedulerServiceImpl.class);

    public JobSchedulerServiceImpl(IQueueDao aQueueDao) {
        theQueueDao = aQueueDao;
    }

    /** Thread count */
    public int getThreadsCount() {
        return theTaskExecutor.getCorePoolSize();
    }
    
    public void setThreadsCount(int aThreadsCount) {
        theTaskExecutor.setCorePoolSize(aThreadsCount);
    }

    /**
     * {@inheritDoc}
     */
    public void scheduleJob(Class<? extends IJob> aJobClass, Object aJobParameters) {
        LOG.info("Pushing job to queue {}...", aJobClass.getName());

        theQueueDao.push(theJobInfoBuilder.build(aJobClass, aJobParameters));
        
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({"unchecked"})
    public void registerJob(IJob aJob) {
        String jobName = aJob.getClass().getName();
        theJobs.put(jobName, aJob);
        LOG.info("Registered job: {}", jobName);
    }

    /**
     * {@inheritDoc}
     */
    public void start() {
        LOG.info("Starting job scheduler...");

        theIsStarted = true;
        thePeriodicExecutor.execute(thePullTask);
    }

    /**
     * {@inheritDoc}
     */
    public void stop() {
        LOG.info("Stopping job scheduler...");
        theIsStarted = false;
        LOG.info("Stopping periodic executor...");
        thePeriodicExecutor.shutdown();
        LOG.info("Stopping task executor...");
        theTaskExecutor.shutdown();
    }


    private class PullTask implements Runnable {
        public void run() {
            while(theIsStarted) {

                List<TJobInfo> jobs = theQueueDao.pull(getEvailableThreadsCount());
                if(LOG.isDebugEnabled()) {
                    LOG.debug("Pulled jobs: "+jobs);
                }
                for (TJobInfo info : jobs) {

                    IJob<Object> job  = theJobs.get(info.getJobName());
                    if(job!=null) {
                        Object       jobParameters   = theJobInfoBuilder.parseXml(info.getJobParameters());
                        IJobContext  context         = new IJobContext();

                        theTaskExecutor.execute(new Task(job, jobParameters, context));
                    } else {
                        LOG.error("Job with name '{}' not found", info.getJobName());
                    }

                }

                try {
                    Thread.sleep(theSleepTime);
                } catch (InterruptedException e) {
                    LOG.error("Cannot sleep "+theSleepTime, e);
                }
            }
        }
    }

    private static class Task implements Runnable {
        public Task(IJob<Object> aJob, Object aJobParameters, IJobContext aContext) {
            theJob = aJob;
            theJobParameters = aJobParameters;
            theJobContext = aContext;
        }

        public void run() {
            try {
                theJob.run(theJobContext, theJobParameters);
            } catch (Exception e) {
                LOG.error("Error executing "+theJob, e);
            }
        }

        private final IJob<Object> theJob;
        private final Object theJobParameters;
        private final IJobContext theJobContext;
    }
    private int getEvailableThreadsCount() {
        return theTaskExecutor.getCorePoolSize() - theTaskExecutor.getActiveCount();
    }


    private final JobInfoBuilder theJobInfoBuilder = new JobInfoBuilder();
    private final HashMap<String, IJob<Object>> theJobs = new HashMap<String, IJob<Object>>();
    private final IQueueDao theQueueDao;

    private final ExecutorService thePeriodicExecutor = Executors.newSingleThreadExecutor();
    // getActiveCount
    private final ThreadPoolExecutor theTaskExecutor  = new ThreadPoolExecutor(1, 1,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>());

    private final PullTask thePullTask = new PullTask();
    private volatile boolean theIsStarted = false;
    private volatile long theSleepTime = 1000;
    /** Thread count */
    private int theThreadCount;

}
