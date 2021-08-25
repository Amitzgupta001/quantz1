package com.example.quartzdemo.controller;

import com.example.quartzdemo.job.Job;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
public class JobSchedulerController {
    private static final Logger logger = LoggerFactory.getLogger(JobSchedulerController.class);

    @Autowired
    private Scheduler scheduler;

    @GetMapping("/schedule")
    public ResponseEntity<String> scheduleEmail(@Valid @RequestParam String action, @Valid @RequestParam String time) throws SchedulerException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date =  sdf.parse(time);

        JobDetail jobDetail = buildJobDetail(action);
        Trigger trigger = buildJobTrigger(jobDetail, date);
        scheduler.scheduleJob(jobDetail, trigger);
        return ResponseEntity.ok(trigger.toString());
    }

    private JobDetail buildJobDetail(String action) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("action", action);

        return JobBuilder.newJob(Job.class)
                .withIdentity(UUID.randomUUID().toString(), "schedule")
                .withDescription("performe Job")
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail, Date date) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "job-triggers")
                .withDescription("job Trigger")
                .startAt(date)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();
    }
}
