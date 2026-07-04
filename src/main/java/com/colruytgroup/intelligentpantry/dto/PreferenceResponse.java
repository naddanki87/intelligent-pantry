package com.colruytgroup.intelligentpantry.dto;

import java.util.Map;

import com.colruytgroup.intelligentpantry.entity.preference.Metadata;

/**
 * Outgoing preference payload: fixed {@code id}/{@code userId}/{@code metadata}
 * plus the schemaless {@code preferences} content.
 */
public record PreferenceResponse(

        String id,

        String userId,

        Map<String, Object> preferences,

        Metadata metadata
) {
}
