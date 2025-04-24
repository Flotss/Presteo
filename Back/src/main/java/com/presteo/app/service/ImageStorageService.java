package com.presteo.app.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class ImageStorageService {

    private final Storage storage;

    @Value("${gcs.bucket.name}")
    private String bucketName;

    @Value("${gcs.folder.user-profile-images}")
    private String userProfileImagesFolder;

    @Value("${gcs.folder.service-images}")
    private String serviceImagesFolder;

    public ImageStorageService(Storage storage) {
        this.storage = storage;
    }


    public String uploadUserProfileImage(@NotNull MultipartFile file) throws IOException {
        return uploadImage(file, userProfileImagesFolder);
    }

    public String uploadServiceImage(@NotNull MultipartFile file) throws IOException {
        return uploadImage(file, serviceImagesFolder);
    }


    private String uploadImage(@NotNull MultipartFile file, String path) throws IOException {
        String filename = String.format("%s/%s", path, UUID.randomUUID());
        BlobId blobId = BlobId.of(bucketName, filename);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        storage.create(blobInfo, file.getBytes());
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, filename);
    }
}
