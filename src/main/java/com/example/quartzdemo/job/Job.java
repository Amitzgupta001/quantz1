package com.example.quartzdemo.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;


@Component
public class Job extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(Job.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());
        System.out.println("===================== task excute at time " + jobExecutionContext.getFireTime());
    }

}
