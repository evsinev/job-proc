package com.github.jobproc.scheduler;

/**
 * Job description
 */
public class JobDescription {

    public enum RepeatIntervalUnit {
        MINUTE, HOUR, DAY, MONTH, YEAR
    }

    public JobDescription(int aPriority, int aRepeatCount, int aRepeatIntervalValue, RepeatIntervalUnit aRepeatIntervalUnit ) {
        theRepeatIntervalUnit = aRepeatIntervalUnit;
        theRepeatIntervalValue = aRepeatIntervalValue;
        theRepeatCount = aRepeatCount;
        thePriority = aPriority;
    }

    /** Job priority */
    public int getPriority() { return thePriority; }

    /** RepeatCount */
    public int getRepeatCount() { return theRepeatCount; }

    /** Repeat interval */
    public int getRepeatIntervalValue() { return theRepeatIntervalValue; }

    /** Repeat interval unit */
    public RepeatIntervalUnit getRepeatIntervalUnit() { return theRepeatIntervalUnit; }

    /** Repeat interval unit */
    private final RepeatIntervalUnit theRepeatIntervalUnit;
    /** Repeat interval */
    private final int theRepeatIntervalValue;
    /** RepeatCount */
    private final int theRepeatCount;
    /** Job priority */
    private final int thePriority;
}
