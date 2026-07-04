package com.colruytgroup.intelligentpantry.repository;

import com.colruytgroup.intelligentpantry.entity.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, String> {
}
