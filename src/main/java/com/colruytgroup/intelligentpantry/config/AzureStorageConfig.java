package com.colruytgroup.intelligentpantry.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(
        AzureStorageProperties.class)
@Configuration
public class AzureStorageConfig {
}
