package com.github.jobproc.scheduler.support;

import com.github.jobproc.scheduler.support.dao.IQueueDao;
import com.github.jobproc.scheduler.support.dao.TJobDescription;
import com.github.jobproc.scheduler.support.dao.TJobInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 */
public class SimpleQueueDao implements IQueueDao {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleQueueDao.class);

    public void push(TJobInfo aJobInfo) {
        if(LOG.isDebugEnabled()) {
            LOG.debug("Pushing {}", aJobInfo);
        }

        theLock.lock();
        try {
            theQueue.add(aJobInfo);
        } finally {
            theLock.unlock();
        }
    }

    public void registerScheduler(String aSchedulerId, List<TJobDescription> aJobDescriptions) {
        if(LOG.isDebugEnabled()) {
            LOG.debug("Registering {}:", aSchedulerId);
            LOG.debug("  {}", aJobDescriptions);
        }
    }

    public List<TJobInfo> pull(String aSchedulerId, int aCount) {
        if(LOG.isDebugEnabled()) {
            LOG.debug("Pulling {} : {}", aSchedulerId, aCount);
        }
        theLock.lock();
        try {
            LinkedList<TJobInfo> list = new LinkedList<TJobInfo>();
            TJobInfo job ;
            for(int i=0; i<aCount && (job=theQueue.poll())!=null; i++) {
                list.add(job);
            }
            return Collections.unmodifiableList(list);
        } finally {
            theLock.unlock();
        }
    }

    public void setJobCompleted(long aJobId, String aStatus, String aErrorMessage) {
        if(LOG.isDebugEnabled()) {
            LOG.debug("Job {} has status {} with error ", new Object[]{aJobId, aStatus, aErrorMessage});
        }
    }

    private final Lock theLock = new ReentrantLock();
    private final Queue<TJobInfo> theQueue = new LinkedList<TJobInfo>();

}
