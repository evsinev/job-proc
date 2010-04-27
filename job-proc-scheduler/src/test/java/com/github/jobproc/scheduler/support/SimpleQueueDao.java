package com.github.jobproc.scheduler.support;

import com.github.jobproc.scheduler.support.dao.IQueueDao;
import com.github.jobproc.scheduler.support.dao.TJobInfo;

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

    public void push(TJobInfo aJobInfo) {
        theLock.lock();
        try {
            theQueue.add(aJobInfo);
        } finally {
            theLock.unlock();
        }
    }

    public List<TJobInfo> pull(int aCount) {
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

    private final Lock theLock = new ReentrantLock();
    private final Queue<TJobInfo> theQueue = new LinkedList<TJobInfo>();

}
