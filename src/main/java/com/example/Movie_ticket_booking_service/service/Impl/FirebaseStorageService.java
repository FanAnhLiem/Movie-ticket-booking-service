package com.example.Movie_ticket_booking_service.service.Impl;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FirebaseStorageService {
    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("Poster file is empty");
        }

        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

        Bucket bucket = StorageClient.getInstance().bucket();
        Blob blob = bucket.create(fileName, file.getBytes(), file.getContentType());

        return String.format(
                "https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media",
                bucket.getName(),
                fileName.replace("/", "%2F")
        );
    }
}
