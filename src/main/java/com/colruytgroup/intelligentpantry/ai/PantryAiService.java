package com.colruytgroup.intelligentpantry.ai;

import com.colruytgroup.intelligentpantry.dto.response.PantryExtractionResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PantryAiService {

    PantryExtractionResponse extractInventory(
            String text);

    PantryExtractionResponse extractInventoryFromImage(MultipartFile file) throws IOException;
}
