package com.colruytgroup.intelligentpantry.service.impl;

import com.azure.cosmos.models.PartitionKey;
import com.colruytgroup.intelligentpantry.cosmos.repository.PreferenceRepository;
import com.colruytgroup.intelligentpantry.dto.PreferenceRequest;
import com.colruytgroup.intelligentpantry.dto.PreferenceResponse;
import com.colruytgroup.intelligentpantry.entity.Preference;
import com.colruytgroup.intelligentpantry.entity.preference.Metadata;
import com.colruytgroup.intelligentpantry.service.PreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PreferenceServiceImpl implements PreferenceService {

    private final PreferenceRepository repository;

    @Override
    public PreferenceResponse create(PreferenceRequest request) {
        String userId = requireText(request.userId(), "userId is required to create a preference");
        String id = hasText(request.id()) ? request.id() : UUID.randomUUID().toString();

        String now = Instant.now().toString();
        Preference preference = toEntity(request, userId, id, new Metadata(now, now, 1));

        return map(repository.save(preference));
    }

    @Override
    public List<PreferenceResponse> find(String userId, String id) {
        List<PreferenceResponse> responses = new ArrayList<>();
        resolve(userId, id).forEach(preference -> responses.add(map(preference)));

        return responses;
    }

    @Override
    public PreferenceResponse update(String userId, String id, PreferenceRequest request) {
        // Identity may come from the query params or fall back to the body.
        String targetUserId = firstNonBlank(userId, request.userId());
        String targetId = firstNonBlank(id, request.id());
        Preference existing = resolveSingle(targetUserId, targetId);

        Metadata previous = existing.getMetadata();
        String createdAt = (previous != null && previous.createdAt() != null)
                ? previous.createdAt()
                : Instant.now().toString();
        int nextVersion = (previous != null && previous.version() != null)
                ? previous.version() + 1
                : 1;

        Metadata metadata = new Metadata(createdAt, Instant.now().toString(), nextVersion);
        // Identity stays pinned to the resolved document; the body only supplies content.
        Preference preference = toEntity(request, existing.getUserId(), existing.getId(), metadata);

        return map(repository.save(preference));
    }

    @Override
    public void delete(String userId, String id) {
        Preference existing = resolveSingle(userId, id);
        repository.deleteById(existing.getId(), new PartitionKey(existing.getUserId()));
    }

    /**
     * Resolve preferences from any combination of userId / id.
     * both → point read; id only → cross-partition lookup; userId only → list.
     */
    private List<Preference> resolve(String userId, String id) {
        boolean hasUser = hasText(userId);
        boolean hasId = hasText(id);

        if (hasUser && hasId) {
            return repository.findById(id, new PartitionKey(userId))
                    .map(List::of)
                    .orElseGet(List::of);
        }
        if (hasId) {
            return repository.findById(id)
                    .map(List::of)
                    .orElseGet(List::of);
        }
        if (hasUser) {
            List<Preference> list = new ArrayList<>();
            repository.findByUserId(userId).forEach(list::add);
            return list;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide at least one of userId or id");
    }

    /** Resolve to exactly one preference; 404 if none, 400 if the criteria are ambiguous. */
    private Preference resolveSingle(String userId, String id) {
        List<Preference> matches = resolve(userId, id);
        if (matches.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Preference not found for userId=" + userId + ", id=" + id);
        }
        if (matches.size() > 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Multiple preferences match userId=" + userId + "; specify an id");
        }
        return matches.get(0);
    }

    private Preference toEntity(PreferenceRequest request, String userId, String id, Metadata metadata) {
        Map<String, Object> preferences = request.preferences() != null
                ? new LinkedHashMap<>(request.preferences())
                : new LinkedHashMap<>();

        return Preference.builder()
                .id(id)
                .userId(userId)
                .preferences(preferences)
                .metadata(metadata)
                .build();
    }

    private PreferenceResponse map(Preference preference) {
        return new PreferenceResponse(
                preference.getId(),
                preference.getUserId(),
                preference.getPreferences(),
                preference.getMetadata());
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private static String firstNonBlank(String first, String second) {
        return hasText(first) ? first : second;
    }

    private static String requireText(String value, String message) {
        if (!hasText(value)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
        return value;
    }
}
