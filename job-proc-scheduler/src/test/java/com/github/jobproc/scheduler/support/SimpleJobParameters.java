package com.github.jobproc.scheduler.support;

/**
 * 
 */
public class SimpleJobParameters {

    public SimpleJobParameters(long aId) {
        theId = aId;
    }

    /** id */
     public long getId() { return theId; }
     public void setId(long aId) { theId = aId; }

    @Override
    public String toString() {
        return "id:"+theId;
    }

    /** id */
     private long theId;

}
