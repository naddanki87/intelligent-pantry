package com.colruytgroup.intelligentpantry.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "azure.storage")
@Data
public class AzureStorageProperties {

    private String connectionString;

    private String containerName;
}
