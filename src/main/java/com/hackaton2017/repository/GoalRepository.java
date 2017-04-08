package com.hackaton2017.repository;

import com.hackaton2017.domain.Goal;
import com.hackaton2017.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Kanstantsin_Tolstsik on 4/8/2017.
 */
public interface GoalRepository extends JpaRepository<Goal, Long> {

    List<Goal> findAllByJob(Job job);
}
