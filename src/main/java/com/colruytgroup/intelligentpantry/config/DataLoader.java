package com.colruytgroup.intelligentpantry.config;

import com.colruytgroup.intelligentpantry.entity.PantryItem;
import com.colruytgroup.intelligentpantry.enums.Category;
import com.colruytgroup.intelligentpantry.repository.PantryItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final PantryItemRepository repository;

    @Bean
    CommandLineRunner loadData() {

        return args -> {

            if(repository.count() == 0){

                repository.save(
                        PantryItem.builder()
                                .itemName("Milk")
                                .quantity(2.0)
                                .unit("Packets")
                                .purchaseDate(LocalDate.now())
                                .expiryDate(LocalDate.now().plusDays(1))
                                .category(Category.DAIRY)
                                .build());

                repository.save(
                        PantryItem.builder()
                                .itemName("Eggs")
                                .quantity(12.0)
                                .unit("Pieces")
                                .purchaseDate(LocalDate.now())
                                .expiryDate(LocalDate.now().plusDays(10))
                                .category(Category.OTHER)
                                .build());
            }
        };
    }
}
