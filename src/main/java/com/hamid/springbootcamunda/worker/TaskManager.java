package com.hamid.springbootcamunda.worker;

import com.hamid.springbootcamunda.dto.TaskDto;
import com.hamid.springbootcamunda.service.TaskService;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeVariable;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@EnableZeebeClient
@RestController
@Slf4j
@RequiredArgsConstructor
public class TaskManager {

    private final TaskService taskService;

    @ZeebeWorker(type = "task_creation")
    public void createTask(final JobClient client, final ActivatedJob job,
                           @ZeebeVariable String title,
                           @ZeebeVariable String description,
                           @ZeebeVariable String due_date) {
        log.info("Creating a task with the title of {}", title);


        TaskDto taskDto = taskService.createTask(TaskDto.builder().title(title).description(description).build());
        client.newCompleteCommand(job.getKey())
                .variables("{\"id\": \"" + taskDto.getTitle() + "\"}")
                .send()
                .exceptionally(throwable -> {
                    throw new RuntimeException("Could not complete job " + job, throwable);
                });
    }
}
