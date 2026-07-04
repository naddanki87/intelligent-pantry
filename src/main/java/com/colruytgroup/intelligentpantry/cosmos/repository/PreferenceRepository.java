package com.colruytgroup.intelligentpantry.cosmos.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.colruytgroup.intelligentpantry.entity.Preference;

@Repository
public interface PreferenceRepository extends CosmosRepository<Preference, String> {

    List<Preference> findByUserId(String userId);
}
