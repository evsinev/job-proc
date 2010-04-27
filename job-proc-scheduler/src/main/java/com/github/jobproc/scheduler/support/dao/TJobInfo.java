package com.github.jobproc.scheduler.support.dao;

import com.github.jobproc.scheduler.support.JobInfoBuilder;

import javax.persistence.Column;

/**
 * Job info
 */
public class TJobInfo {

    /** Job name */
    @Column(name="job_name")
    public String getJobName() { return theJobName; }
    public void setJobName(String aJobName) { theJobName = aJobName; }

    /** Job parameters */
    @Column(name="job_parameters")
    public String getJobParameters() { return theJobParameters; }
    public void setJobParameters(String aJobParameters) { theJobParameters = aJobParameters; }

    @Override
    public String toString() {
        return JobInfoBuilder.toString(this);
    }

    /** Job parameters */
    private String theJobParameters;
    /** Job name */
    private String theJobName;
}
