package com.colruytgroup.intelligentpantry.service;

import com.colruytgroup.intelligentpantry.dto.response.WasteAnalysis;
import com.colruytgroup.intelligentpantry.entity.PantryItem;

public interface WasteAnalysisService {

    WasteAnalysis analyze(PantryItem item);

    WasteAnalysis dashBoardAnalyze(PantryItem item);
}
