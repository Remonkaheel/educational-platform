package com.global.hr.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.global.hr.models.Video;
import com.global.hr.services.VideoService;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @GetMapping
    public ResponseEntity<List<Video>> getAllVideos() {
        return ResponseEntity.ok(videoService.getAllVideos());
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Video>> getVideosByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(videoService.getVideosByCourse(courseId));
    }

    @PostMapping("/upload/{courseId}")
    public ResponseEntity<String> uploadVideo(
            @PathVariable Long courseId,
            @ModelAttribute Video video,
            @RequestParam("file") MultipartFile file) throws IOException {

        videoService.saveVideo(courseId, video, file);
        return ResponseEntity.ok("Video uploaded successfully!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Video> getVideoById(@PathVariable Long id) {
        return ResponseEntity.ok(videoService.getVideoById(id));
    }
}
