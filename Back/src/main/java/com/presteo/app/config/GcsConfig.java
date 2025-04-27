package com.presteo.app.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Configuration
public class GcsConfig {

    @Value("${gcp.project-id}")
    private String projectId;

    @Value("${github.token}")
    private String githubToken;

    @Value("${github.gist.id}")
    private String gistId;

    @Bean
    public Storage storage() throws IOException {
        GoogleCredentials credentials = getCredentialsFromGithubGist();

        return StorageOptions.newBuilder()
                .setCredentials(credentials)
                .setProjectId(projectId)
                .build()
                .getService();
    }

    private GoogleCredentials getCredentialsFromGithubGist() throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        // Setup GitHub API request with authorization
        String gistUrl = "https://api.github.com/gists/" + gistId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + githubToken);
        headers.set("Accept", "application/vnd.github.v3+json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        // Get the gist content
        ResponseEntity<Map> response = restTemplate.exchange(
                gistUrl,
                HttpMethod.GET,
                entity,
                Map.class
        );

        Map gistData = response.getBody();
        if (gistData == null || !gistData.containsKey("files")) {
            throw new IOException("Failed to retrieve gist data");
        }

        // Get the content from the first file in the gist
        Map<String, Map<String, Object>> files = (Map<String, Map<String, Object>>) gistData.get("files");
        if (files.isEmpty()) {
            throw new IOException("No files found in gist");
        }

        String content = (String) files.values().iterator().next().get("content");
        if (content == null) {
            throw new IOException("No content found in gist file");
        }

        // Parse the JSON content as Google credentials
        try (ByteArrayInputStream stream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8))) {
            return GoogleCredentials.fromStream(stream);
        }
    }
}