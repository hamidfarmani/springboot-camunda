package com.hamid.springbootcamunda.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeVariable;
import io.camunda.zeebe.spring.client.annotation.ZeebeWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@EnableZeebeClient
@RestController
@Slf4j
public class TaskManager {

    @ZeebeWorker(type = "connection_happend")
    public void getInformation(final JobClient client, final ActivatedJob job, @ZeebeVariable String person_uuid) {
        UUID recovery_certificate_uuid = UUID.randomUUID();
        log.info("Generating certificate of recovery for person " + person_uuid +"...");
        log.info("Generated certificate ID: " + recovery_certificate_uuid);
        log.info("Storing Recovery Certificate in external database...");

        client.newCompleteCommand(job.getKey())
                .variables("{\"recovery_certificate_uuid\": \"" + recovery_certificate_uuid + "\"}")
                .send()
                .exceptionally( throwable -> { throw new RuntimeException("Could not complete job " + job, throwable); });
    }
}
