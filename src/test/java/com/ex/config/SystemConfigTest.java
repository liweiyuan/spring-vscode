package com.ex.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;

import com.ex.entity.User;
import com.ex.service.UserService;

@ExtendWith(MockitoExtension.class)
public class SystemConfigTest {

    @Test
    void afterPropertiesSet_schedulesTasks_and_tasksInvokeUserService() throws Exception {
        // create isolated mocks for this test to avoid interference from other tests
        TaskScheduler localScheduler = Mockito.mock(TaskScheduler.class);
        UserService localUserService = Mockito.mock(UserService.class);
        when(localUserService.getAllUsers()).thenReturn(Arrays.asList(new User(1L, "Alice", "a@example.com")));

        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        ArgumentCaptor<Trigger> triggerCaptor = ArgumentCaptor.forClass(Trigger.class);

        SystemConfig localConfig = new SystemConfig(localUserService, localScheduler);

        // when
        localConfig.afterPropertiesSet();

        // then: verify schedule called 3 times with Runnable + Trigger
        verify(localScheduler, times(3)).schedule(runnableCaptor.capture(), triggerCaptor.capture());

        List<Runnable> runnables = runnableCaptor.getAllValues();
        List<Trigger> triggers = triggerCaptor.getAllValues();

        assertEquals(3, runnables.size());
        assertEquals(3, triggers.size());

        // At least one trigger should be PeriodicTrigger and one CronTrigger
        boolean hasPeriodic = triggers.stream().anyMatch(t -> t instanceof PeriodicTrigger);
        boolean hasCron = triggers.stream().anyMatch(t -> t instanceof CronTrigger);

        assertTrue(hasPeriodic, "Expected a PeriodicTrigger to be scheduled");
        assertTrue(hasCron, "Expected a CronTrigger to be scheduled");

        // Execute captured runnables and verify they call userService.getAllUsers()
        Mockito.clearInvocations(localUserService);
        for (Runnable r : runnables) {
            r.run();
        }

        verify(localUserService, atLeast(3)).getAllUsers();
    }
}
