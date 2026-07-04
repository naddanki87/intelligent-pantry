package com.colruytgroup.intelligentpantry.entity;

import java.util.LinkedHashMap;
import java.util.Map;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.colruytgroup.intelligentpantry.entity.preference.Metadata;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A user's recipe preferences, stored in Azure Cosmos DB.
 *
 * <p>Only {@code id}, {@code userId} and server-managed {@code metadata} are
 * fixed. The actual preference content lives in the schemaless {@link #preferences}
 * map, so its structure can vary from document to document without a code change.
 *
 * <p>Partitioned by {@code userId} so every preference document for a user lives
 * in the same logical partition and can be read/queried efficiently.
 */
@Container(containerName = "userPreferences")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Preference {

    @Id
    private String id;

    @PartitionKey
    private String userId;

    /** Schemaless preference content; structure varies per document. */
    @Builder.Default
    private Map<String, Object> preferences = new LinkedHashMap<>();

    private Metadata metadata;
}
