package com.github.jobproc.scheduler;

/**
 * IJob
 */
public interface IJob<T> {

    void run(IJobContext aContext, T aParameters) throws Exception;

}
