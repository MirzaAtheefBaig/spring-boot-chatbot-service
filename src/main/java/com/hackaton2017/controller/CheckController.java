package com.hackaton2017.controller;

import com.hackaton2017.domain.Checker;
import com.hackaton2017.domain.Goal;
import com.hackaton2017.domain.Job;
import com.hackaton2017.repository.GoalRepository;
import com.hackaton2017.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Kanstantsin_Tolstsik on 4/8/2017.
 */
@RestController
public class CheckController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private GoalRepository goalRepository;


    @RequestMapping(value = "/check", method = RequestMethod.GET)
    ResponseEntity<Checker> check() {
        Checker checker = new Checker();
        final Iterable<Job> jobs = jobRepository.findAllByIsCompleted(false);
        for (Job job : jobs) {
            List<Goal> goals = goalRepository.findAllByJob(job);
            boolean result = false;
            for (Goal goal : goals) {
                result = goal.getCompleted().booleanValue();
                if (!result) break;
            }

            if (result) {
                String message = "Your item is available. Please use the following link to order " + job.getUrl();
                checker.setResult(message);
                job.setCompleted(true);
                jobRepository.save(job);
                break;
            }
        }
        if (checker.getResult() == null) checker.setResult("No completed jobs founded.");
        return new ResponseEntity<>(checker, HttpStatus.OK);
    }
}
