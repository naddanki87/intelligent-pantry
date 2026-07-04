package com.colruytgroup.intelligentpantry.dto;

import java.util.Map;

/**
 * Incoming preference payload.
 *
 * <p>{@code id} is optional on create (a UUID is generated when absent).
 * {@code userId} is required on create but may instead be supplied as a query
 * parameter on update; validation therefore happens in the service. The
 * schemaless {@code preferences} object carries the actual content and may hold
 * any structure. {@code metadata} is server-managed and not accepted here.
 */
public record PreferenceRequest(

        String id,

        String userId,

        Map<String, Object> preferences
) {
}
