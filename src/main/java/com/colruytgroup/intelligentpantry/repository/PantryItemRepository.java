package com.colruytgroup.intelligentpantry.repository;

import com.colruytgroup.intelligentpantry.entity.PantryItem;
import com.colruytgroup.intelligentpantry.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface PantryItemRepository  extends JpaRepository<PantryItem, Long> {

    Optional<PantryItem> findByItemNameIgnoreCase(
            String itemName);

    List<PantryItem> findByItemNameContainingIgnoreCase(
            String itemName);

    List<PantryItem> findByCategory(
            Category category);

    List<PantryItem> findByExpiryDateBefore(
            LocalDate date);

    List<PantryItem> findByExpiryDateBetween(
            LocalDate startDate,
            LocalDate endDate);

    boolean existsByItemNameIgnoreCase(
            String itemName);
}