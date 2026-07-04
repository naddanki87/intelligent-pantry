package com.colruytgroup.intelligentpantry.config;

import com.azure.storage.blob.BlobContainerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AzureStorageHealthService {

    private final BlobContainerClient containerClient;

    public String validateConnection() {

        try {

            String containerName =
                    containerClient.getBlobContainerName();

            containerClient.listBlobs()
                    .stream()
                    .limit(1)
                    .toList();

            return "Connected to Azure Blob Storage. Container="
                    + containerName;

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Azure Storage connection failed",
                    ex);
        }
    }
}
