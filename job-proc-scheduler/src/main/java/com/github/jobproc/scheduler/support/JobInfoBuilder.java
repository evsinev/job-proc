package com.github.jobproc.scheduler.support;

import com.github.jobproc.scheduler.IJob;
import com.github.jobproc.scheduler.support.dao.TJobInfo;
import com.thoughtworks.xstream.XStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class JobInfoBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(JobInfoBuilder.class);

    private static final XStream XML_STREAM = new XStream();


    public TJobInfo build(Class<? extends IJob> aJobClass, Object aJobParameters) {

        TJobInfo info = new TJobInfo();
        info.setJobName(aJobClass.getName());

        String xml = XML_STREAM.toXML(aJobParameters);
        info.setJobParameters(xml);

        return info;

    }


    public Object parseXml(String aJobParametersXml) {
        return XML_STREAM.fromXML(aJobParametersXml);
    }

    public static String toString(TJobInfo aInfo) {
        String parameters;
        try {
            parameters = XML_STREAM.fromXML(aInfo.getJobParameters()).toString();
        } catch (Exception e) {
            parameters = "ERROR";
            LOG.error("Can't parse parameters "+aInfo.getJobParameters());
        }
        return aInfo.getJobName()+"("+parameters+")";
    }
}
