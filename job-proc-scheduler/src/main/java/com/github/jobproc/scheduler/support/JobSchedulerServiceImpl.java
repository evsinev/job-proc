package com.github.jobproc.scheduler.support;

import com.github.jobproc.scheduler.*;
import com.github.jobproc.scheduler.support.dao.IQueueDao;
import com.github.jobproc.scheduler.support.dao.TJobInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Implementation
 */
public class JobSchedulerServiceImpl implements IJobSchedulerService, IJobScheduleManagementService {

    private final static Logger LOG = LoggerFactory.getLogger(JobSchedulerServiceImpl.class);

    public JobSchedulerServiceImpl(IQueueDao aQueueDao) {
        theQueueDao = aQueueDao;
        theTaskExecutor = new JobThreadPoolExecutor(theQueueDao, 1, 1,
                                              0L, TimeUnit.MILLISECONDS,
                                              new LinkedBlockingQueue<Runnable>());
        
    }

    /** Thread count
     *  
     * @return threads count
     */
    public int getThreadsCount() {
        return theTaskExecutor.getCorePoolSize();
    }
    
    public void setThreadsCount(int aThreadsCount) {
        theTaskExecutor.setCorePoolSize(aThreadsCount);
    }

    /** Sleep time */
    public long getSleepTime() {
        return theSleepTime;
    }

    public void setSleepTime(long aSleepTime) {
        theSleepTime = aSleepTime;
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
    public void registerJob(IJob aJob, JobDescription aDescription) {
        if(theIsStarted) {
            throw new IllegalStateException("Scheduler is started");
        }
        String jobName = aJob.getClass().getName();
        theJobs.put(jobName, aJob);
        theJobDescriptions.put(jobName, aDescription);
        LOG.info("Registered job: {}", jobName);
    }

    /**
     * {@inheritDoc}
     */
    public void start() {
        LOG.info("Starting job scheduler...");

        theQueueDao.registerScheduler(theSchedulerId, theJobDescriptionsBuilder.build(theJobDescriptions.entrySet()));
        theIsStarted = true;
        thePeriodicExecutor.execute(thePullTask);
    }

    /**
     * {@inheritDoc}
     */
    public void stop() {
        LOG.info("Stopping job scheduler...");
        theIsStarted = false;
        LOG.info("  Stopping periodic executor...");
        thePeriodicExecutor.shutdown();
        LOG.info("  Stopping task executor...");
        theTaskExecutor.shutdown();
    }


    private class PullTask implements Runnable {
        public void run() {
            while(theIsStarted) {

                List<TJobInfo> jobs = theQueueDao.pull(theSchedulerId, getEvailableThreadsCount());
                if(LOG.isDebugEnabled()) {
                    LOG.debug("Pulled jobs: "+jobs);
                }
                for (TJobInfo info : jobs) {

                    IJob<Object> job  = theJobs.get(info.getJobName());
                    if(job!=null) {
                        Object       jobParameters   = theJobInfoBuilder.parseXml(info.getJobParameters());
                        IJobContext  context         = new IJobContext();
                        long         jobId           = info.getJobId();

                        theTaskExecutor.execute(new JobTask(jobId, job, jobParameters, context));
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

    private int getEvailableThreadsCount() {
        return theTaskExecutor.getCorePoolSize() - theTaskExecutor.getActiveCount();
    }


    private final String theSchedulerId = UUID.randomUUID().toString();
    
    private final HashMap<String, IJob<Object>> theJobs = new HashMap<String, IJob<Object>>();
    private final HashMap<String, JobDescription> theJobDescriptions = new HashMap<String, JobDescription>();

    private final IQueueDao theQueueDao;

    private final ExecutorService thePeriodicExecutor = Executors.newSingleThreadExecutor();
    private final JobThreadPoolExecutor theTaskExecutor;

    private volatile boolean theIsStarted = false;
    private volatile long theSleepTime = 1000;
    
    private final PullTask thePullTask = new PullTask();

    private final JobInfoBuilder theJobInfoBuilder = new JobInfoBuilder();
    private final JobDescriptionsBuilder theJobDescriptionsBuilder = new JobDescriptionsBuilder();

}
