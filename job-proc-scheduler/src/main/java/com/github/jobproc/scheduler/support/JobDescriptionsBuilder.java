package com.github.jobproc.scheduler.support;

import com.github.jobproc.scheduler.JobDescription;
import com.github.jobproc.scheduler.support.dao.TJobDescription;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class JobDescriptionsBuilder {


    public List<TJobDescription> build(Set<Map.Entry<String,JobDescription>> aDescriptions) {
        List<TJobDescription> descriptions = new LinkedList<TJobDescription>();
        for (Map.Entry<String, JobDescription> entry : aDescriptions) {
            TJobDescription description = new TJobDescription();
            description.setJobName(entry.getKey());
            final JobDescription descr = entry.getValue();
            description.setPriority(descr.getPriority());
            description.setRepeatCount(descr.getRepeatCount());
            description.setRepeatInterval(
                    descr.getRepeatIntervalValue()+""+ descr.getRepeatIntervalUnit()
            );
            descriptions.add(description);
        }
        return descriptions;
    }
}
