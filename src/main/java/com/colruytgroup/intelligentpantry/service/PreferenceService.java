package com.colruytgroup.intelligentpantry.service;

import java.util.List;

import com.colruytgroup.intelligentpantry.dto.PreferenceRequest;
import com.colruytgroup.intelligentpantry.dto.PreferenceResponse;

public interface PreferenceService {

    PreferenceResponse create(PreferenceRequest request);

    /**
     * Flexible read. Any combination of {@code userId} / {@code id} may be given:
     * both → point read; id only → cross-partition lookup; userId only → all of
     * that user's preferences. At least one must be present.
     */
    List<PreferenceResponse> find(String userId, String id);

    PreferenceResponse update(String userId, String id, PreferenceRequest request);

    void delete(String userId, String id);
}
