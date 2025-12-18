package com.ex.config;

import java.time.Duration;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import com.ex.service.UserService;

@Component
public class SystemConfig implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(SystemConfig.class);

    private final UserService userService;

    private final TaskScheduler taskScheduler;

    public SystemConfig(UserService userService, TaskScheduler taskScheduler) {
        this.userService = userService;
        this.taskScheduler = taskScheduler;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // Initialization logic can be added here if needed
        // Use TaskScheduler + PeriodicTrigger for fixed-delay scheduling
        PeriodicTrigger fixedDelayTrigger = new PeriodicTrigger(Duration.ofSeconds(20));
        fixedDelayTrigger.setFixedRate(false); // fixed delay
        taskScheduler.schedule(createTask("SampleTask"), fixedDelayTrigger);

        // Example of scheduling a single-run task using TaskScheduler
        Trigger exampleTrigger = context -> {
            long nextExecutionTime = System.currentTimeMillis() + 15000; // 15 seconds later
            return Instant.ofEpochMilli(nextExecutionTime);
        };
        taskScheduler.schedule(createTask("TaskSchedulerTask"), exampleTrigger);

        // Cron-style trigger (every 30 seconds)
        Trigger cronTrigger = new CronTrigger("0/30 * * * * *"); // Every 30 seconds
        taskScheduler.schedule(createTask("CronTask"), cronTrigger);
    }

    private Runnable createTask(String taskName) {
        return () -> {
            try {
                logger.info("Executing task: {}", taskName);
                // Example operation: print all users
                if (userService != null && userService.getAllUsers() != null) {
                    userService.getAllUsers()
                            .forEach(user -> logger.info("User ID: {}, Name: {}", user.getId(), user.getName()));
                }
            } catch (Exception e) {
                logger.error("Error executing task {}: {}", taskName, e.getMessage(), e);
            }
        };
    }

}
