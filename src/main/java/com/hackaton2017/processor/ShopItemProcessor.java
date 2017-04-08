package com.hackaton2017.processor;

import com.hackaton2017.domain.*;
import com.hackaton2017.parser.ShopItemParser;
import com.hackaton2017.parser.impl.WildberriesShopItemParser;
import com.hackaton2017.repository.GoalRepository;
import com.hackaton2017.repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

/**
 * Created by Kanstantsin_Tolstsik on 4/4/2017.
 */
@Component
public class ShopItemProcessor {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private GoalRepository goalRepository;

    private static final Logger log = LoggerFactory.getLogger(ShopItemProcessor.class);

    @Scheduled(fixedRate = 10000)
    public void processShopItem() {

        final List<Job> incompleteJobs = jobRepository.findAllByIsCompleted(false);

        for (final Job job : incompleteJobs) {
            ShopItemParser parser = null;
            if (Shop.WILDBERRIES.equals(job.getShop())) {
                parser = new WildberriesShopItemParser();
            }
            final ShopItem shopItem = parser.parse(job);
            for (final Goal goal : job.getGoals()) {
                if (isGoalCompleted(shopItem, goal)) {
                    goal.setCompleted(true);
                    goalRepository.save(goal);
                }
            }

            final List<Goal> goals = job.getGoals();
            boolean isJobCompleted = false;
            for (final Goal goal : goals) {
                if (goal.getCompleted().equals(Boolean.TRUE)) {
                    isJobCompleted = true;
                    continue;
                } else {
                    isJobCompleted = false;
                    break;
                }
            }
            job.setCompleted(isJobCompleted);
            jobRepository.save(job);
        }
    }

    private boolean isGoalCompleted(final ShopItem shopItem, final Goal goal) {
        if (GoalType.SIZE_WAITING.equals(goal.getGoalType())) {
            final Collection<String> sizes = shopItem.getSize();
            if (sizes.contains(goal.getData())) return true;
        }
        return false;
    }
}
