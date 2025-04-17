package com.presteo.app.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.*;

@Configuration
public class GcsConfig {

    @Value("${gcp.project-id}")
    private String projectId;

    @Value("${gcp.credentials.location}")
    private Resource credentialResource;

    @Bean
    public Storage storage() throws IOException {
        GoogleCredentials credentials;
        try (var stream = credentialResource.getInputStream()) {
            credentials = ServiceAccountCredentials.fromStream(stream);
        }

        return StorageOptions.newBuilder()
                .setCredentials(credentials)
                .setProjectId(projectId)
                .build()
                .getService();
    }


}
