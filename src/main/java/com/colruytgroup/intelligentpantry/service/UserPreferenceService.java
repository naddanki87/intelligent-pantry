package com.colruytgroup.intelligentpantry.service;

import com.colruytgroup.intelligentpantry.dto.request.UserPreferenceRequest;
import com.colruytgroup.intelligentpantry.entity.UserPreference;

public interface UserPreferenceService {

    UserPreference save(
            UserPreferenceRequest request);

    UserPreference get(String username);

    void delete(String username);
}
