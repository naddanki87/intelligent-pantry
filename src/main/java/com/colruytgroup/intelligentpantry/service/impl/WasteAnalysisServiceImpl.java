package com.colruytgroup.intelligentpantry.service.impl;

import com.colruytgroup.intelligentpantry.dto.response.WasteAnalysis;
import com.colruytgroup.intelligentpantry.entity.PantryItem;
import com.colruytgroup.intelligentpantry.enums.WasteRisk;
import com.colruytgroup.intelligentpantry.service.WasteAnalysisService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class WasteAnalysisServiceImpl implements WasteAnalysisService {

    @Override
    public WasteAnalysis analyze(PantryItem item) {

        long daysRemaining = ChronoUnit.DAYS.between(
                LocalDate.now(),
                item.getExpiryDate());

        WasteRisk risk;
        int wasteScore;

        if (daysRemaining <= 1) {
            risk = WasteRisk.HIGH;
            wasteScore = calculateWasteScore(daysRemaining, item.getQuantity());

        } else if (daysRemaining <= 3) {
            risk = WasteRisk.MEDIUM;
            wasteScore = calculateWasteScore(daysRemaining, item.getQuantity());

        } else {
            risk = WasteRisk.LOW;
            wasteScore = calculateWasteScore(daysRemaining, item.getQuantity());
        }


        return new WasteAnalysis(
                item.getItemName(),
                item.getQuantity().longValue(),
                daysRemaining,
                risk,
                wasteScore);
    }

    @Override
    public WasteAnalysis dashBoardAnalyze(PantryItem item) {

        if (item.getExpiryDate() == null) {

            return new WasteAnalysis(
                    item.getItemName(),
                    item.getQuantity().longValue(),
                    Long.MAX_VALUE,
                    WasteRisk.LOW,
                    0
            );
        }

        long daysRemaining =
                ChronoUnit.DAYS.between(
                        LocalDate.now(),
                        item.getExpiryDate());
        WasteRisk risk;
        int wasteScore;

        if (daysRemaining <= 1) {

            risk = WasteRisk.HIGH;
            wasteScore = 100;

        } else if (daysRemaining <= 3) {

            risk = WasteRisk.MEDIUM;
            wasteScore = 70;

        } else {

            risk = WasteRisk.LOW;
            wasteScore = 30;
        }

        return new WasteAnalysis(
                item.getItemName(),
                item.getQuantity().longValue(),
                daysRemaining,
                risk,
                wasteScore
        );
    }

    private int calculateWasteScore(
            long daysRemaining,
            double quantity) {

        int score = 0;

        if(daysRemaining <= 1) {
            score += 70;
        }
        else if(daysRemaining <= 3) {
            score += 50;
        }
        else {
            score += 20;
        }

        if(quantity >= 10) {
            score += 30;
        }
        else if(quantity >= 5) {
            score += 20;
        }
        else {
            score += 10;
        }

        return Math.min(score, 100);
    }


}
