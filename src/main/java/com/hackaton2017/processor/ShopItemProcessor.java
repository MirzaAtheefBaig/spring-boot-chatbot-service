package com.hackaton2017.processor;

import com.hackaton2017.domain.*;
import com.hackaton2017.parser.ShopItemParser;
import com.hackaton2017.parser.impl.LamodaShopItemParses;
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
            if (Shop.LAMODA.equals(job.getShop())) {
                parser = new LamodaShopItemParses();
            }
            final ShopItem shopItem = parser.parse(job);
            List<Goal> goals = goalRepository.findAllByJob(job);
            if (goals == null || goals.isEmpty()) continue;
            for (final Goal goal : goals) {
                if (isGoalCompleted(shopItem, goal)) {
                    goal.setCompleted(true);
                } else {
                    goal.setCompleted(false);
                }
                goalRepository.save(goal);
            }
        }
    }

    private boolean isGoalCompleted(final ShopItem shopItem, final Goal goal) {
        if (GoalType.SIZE_WAITING.equals(goal.getGoalType())) {
            final Collection<String> sizes = shopItem.getSize();
            if (sizes.contains(goal.getData())) return true;
        }
        if (GoalType.COST_WAITING.equals(goal.getGoalType())) {
            final Double waitingCost = Double.parseDouble(goal.getData());
            final Double actualCost = shopItem.getCost();
            if (Double.compare(waitingCost, actualCost) >= 0) return true;
        }
        return false;
    }
}
