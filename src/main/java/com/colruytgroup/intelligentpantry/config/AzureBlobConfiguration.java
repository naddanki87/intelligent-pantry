package com.colruytgroup.intelligentpantry.config;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AzureBlobConfiguration {

    private final AzureStorageProperties properties;

    @Bean
    public BlobContainerClient blobContainerClient() {

        BlobServiceClient serviceClient =
                new BlobServiceClientBuilder()
                        .connectionString(
                                properties.getConnectionString())
                        .buildClient();

        BlobContainerClient container =
                serviceClient.getBlobContainerClient(
                        properties.getContainerName());

        if (!container.exists()) {
            container.create();
        }

        return container;
    }
}
