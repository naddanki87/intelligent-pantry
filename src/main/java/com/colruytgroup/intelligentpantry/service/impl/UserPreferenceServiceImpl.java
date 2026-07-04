package com.colruytgroup.intelligentpantry.service.impl;

import com.colruytgroup.intelligentpantry.dto.request.UserPreferenceRequest;
import com.colruytgroup.intelligentpantry.entity.UserPreference;
import com.colruytgroup.intelligentpantry.repository.UserPreferenceRepository;
import com.colruytgroup.intelligentpantry.service.UserPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPreferenceServiceImpl implements UserPreferenceService {

    private final UserPreferenceRepository repository;

    @Override
    public UserPreference save(UserPreferenceRequest request) {

        UserPreference preference =
                UserPreference.builder()
                        .username(request.username())
                        .preferences(request.preferences())
                        .build();

        return repository.save(preference);
    }

    public UserPreference get(String username) {

        return repository.findById(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User preference not found for " + username));
    }

    public void delete(String username) {

        if (!repository.existsById(username)) {
            throw new RuntimeException(
                    "User preference not found for " + username);
        }

        repository.deleteById(username);
    }
}
