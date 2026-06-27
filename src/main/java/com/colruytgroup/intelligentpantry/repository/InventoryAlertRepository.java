package com.colruytgroup.intelligentpantry.repository;

import com.colruytgroup.intelligentpantry.entity.InventoryAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryAlertRepository extends JpaRepository<InventoryAlert, Long> {
}
