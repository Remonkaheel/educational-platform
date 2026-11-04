package com.global.hr.services;

import com.global.hr.models.Video;
import com.global.hr.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class VideoStreamingService {

    @Autowired
    private VideoRepository videoRepository;

    @Value("${video.upload.dir}")
    private String uploadDir;

    // Get the video file as a resource
    public Resource getVideoAsResource(String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists()) {
                return resource;
            } else {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Video not found: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Video not found: " + filename, e);
        }
    }

    // Stream the video with pause and resume support
    public byte[] streamVideoChunk(String filename, long start, long end) throws IOException {
        File file = new File(uploadDir + File.separator + filename);
        
        if (!file.exists()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Video not found: " + filename);
        }

        long fileSize = file.length();
        
        // If the file end is not specified, use the file size
        if (end <= 0) {
            end = fileSize - 1;
        }
        
        // Validate the range
        if (start < 0 || end >= fileSize || start > end) {
            throw new ResponseStatusException(
                    HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE, "Invalid range");
        }
        
        // Calculate the chunk size
        long chunkSize = end - start + 1;
        byte[] buffer = new byte[(int) chunkSize];
        
        // Read the requested chunk from the file
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            randomAccessFile.seek(start);
            randomAccessFile.read(buffer, 0, (int) chunkSize);
            return buffer;
        }
    }

    // Get a video thumbnail (a library such as FFmpeg can be used)
    public byte[] generateThumbnail(String filename) throws IOException {
        // Use FFmpeg or any other library to extract a thumbnail from the video
        // This is a simple example - in the real application you would need to use a library such as JavaCV
        
        // For demonstration purposes, we return a default image
        Path defaultThumbnailPath = Paths.get(uploadDir, "default-thumbnail.jpg");
        return java.nio.file.Files.readAllBytes(defaultThumbnailPath);
    }
}