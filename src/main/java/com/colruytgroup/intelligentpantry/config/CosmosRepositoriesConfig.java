package com.colruytgroup.intelligentpantry.config;

import org.springframework.context.annotation.Configuration;

import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;

/**
 * Enables Spring Data Cosmos repositories for the preferences store only.
 * Everything else is persisted through JPA/H2 (see {@link JpaConfig}).
 *
 * <p>Note: the class is intentionally not named {@code CosmosConfig} because
 * Azure's autoconfiguration already registers a bean called {@code cosmosConfig}.
 */
@Configuration
@EnableCosmosRepositories(basePackages = "com.colruytgroup.intelligentpantry.cosmos.repository")
public class CosmosRepositoriesConfig {
}
