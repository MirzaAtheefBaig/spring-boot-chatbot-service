package com.hackaton2017.controller;

import com.hackaton2017.domain.Goal;
import com.hackaton2017.domain.Job;
import com.hackaton2017.repository.GoalRepository;
import com.hackaton2017.repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Created by Kanstantsin_Tolstsik on 4/8/2017.
 */
@RestController
public class GoalController {

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private JobRepository jobRepository;

    private static final Logger log = LoggerFactory.getLogger(GoalController.class);

    @RequestMapping(value = "/goals", method = RequestMethod.GET)
    ResponseEntity<Iterable<Goal>> getGoals() {
        final Iterable<Goal> goals = goalRepository.findAll();
        return new ResponseEntity<>(goals, HttpStatus.OK);
    }

    @RequestMapping(value = "/jobs/{jobId}/goals", method = RequestMethod.POST)
    ResponseEntity<?> createGoal(@PathVariable String jobId, @RequestBody Goal goal) {
        final Job job = jobRepository.findJobById(Long.parseLong(jobId));
        goal.setJob(job);
        final Goal createdGoal = goalRepository.save(goal);
        final HttpHeaders responseHeaders = new HttpHeaders();
        final URI createdJobURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdGoal.getId()).toUri();
        responseHeaders.setLocation(createdJobURI);
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }
}
