package com.github.jobproc.scheduler.support.dao;

import javax.persistence.Column;

/**
 *
 */
public class TJobDescription {
    
    /** Job name */
    @Column(name="job_name")
    public String getJobName() { return theJobName; }
    public void setJobName(String aJobName) { theJobName = aJobName; }

    /** Repeat count */
    @Column(name="repeat_count")
    public int getRepeatCount() { return theRepeatCount; }
    public void setRepeatCount(int aRepeatCount) { theRepeatCount = aRepeatCount; }

    /** Repeat interval */
    @Column(name="repeat_interval")
    public String getRepeatInterval() { return theRepeatInterval; }
    public void setRepeatInterval(String aRepeatInterval) { theRepeatInterval = aRepeatInterval; }

    /** Priority */
    @Column(name="priority")
    public int getPriority() { return thePriority; }
    public void setPriority(int aPriority) { thePriority = aPriority; }

    /** Priority */
    private int thePriority;
    /** Repeat interval */
    private String theRepeatInterval;
    /** Repeat count */
    private int theRepeatCount;
    /** Job name */
    private String theJobName;
}
