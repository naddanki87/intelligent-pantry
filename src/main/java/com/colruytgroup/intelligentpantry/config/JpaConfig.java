package com.colruytgroup.intelligentpantry.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Scopes JPA (H2) repositories to their own package so they do not clash with
 * the Cosmos DB repositories that back preferences (see {@link CosmosRepositoriesConfig}).
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.colruytgroup.intelligentpantry.repository")
public class JpaConfig {
}
