package com.global.hr.controllers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/upload")
public class VideoUploadController {

    @Value("${video.upload.dir}")
    private String UPLOAD_DIR;
    
    @Value("${video.upload.dir:./temp/}")
    private String TEMP_DIR;
    
    // Store upload session information
    private final ConcurrentHashMap<String, UploadSession> uploadSessions = new ConcurrentHashMap<>();
    
    static class UploadSession {
        String fileName;
        long fileSize;
        int totalChunks;
        int receivedChunks;
        String tempDir;
        
        public UploadSession(String fileName, long fileSize, int totalChunks) {
            this.fileName = fileName;
            this.fileSize = fileSize;
            this.totalChunks = totalChunks;
            this.receivedChunks = 0;
            this.tempDir = UUID.randomUUID().toString();
        }
    }
    
    @PostMapping("/initialize")
    public ResponseEntity<Map<String, String>> initializeUpload(
            @RequestBody Map<String, Object> request) {
        
        String fileName = (String) request.get("fileName");
        long fileSize = Long.parseLong(request.get("fileSize").toString());
        int totalChunks = Integer.parseInt(request.get("totalChunks").toString());
        
        // Generate unique upload ID
        String uploadId = UUID.randomUUID().toString();
        
        // Create session
        UploadSession session = new UploadSession(fileName, fileSize, totalChunks);
        uploadSessions.put(uploadId, session);
        
        // Create temp directory for chunks
        try {
            Path tempPath = Paths.get(TEMP_DIR + session.tempDir);
            Files.createDirectories(tempPath);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to initialize upload: " + e.getMessage()));
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("uploadId", uploadId);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/chunk")
    public ResponseEntity<Map<String, Object>> uploadChunk(
            @RequestParam("file") MultipartFile file,
            @RequestParam("uploadId") String uploadId,
            @RequestParam("chunkIndex") int chunkIndex) {
        
        UploadSession session = uploadSessions.get(uploadId);
        if (session == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Upload session not found"));
        }
        
        try {
            // Save chunk to temp directory
            Path chunkPath = Paths.get(TEMP_DIR + session.tempDir + "/" + chunkIndex);
            file.transferTo(chunkPath);
            
            // Update received chunks count
            session.receivedChunks++;
            
            Map<String, Object> response = new HashMap<>();
            response.put("received", session.receivedChunks);
            response.put("total", session.totalChunks);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to save chunk: " + e.getMessage()));
        }
    }
    
    @PostMapping("/finalize")
    public ResponseEntity<Map<String, String>> finalizeUpload(
            @RequestBody Map<String, String> request) {
        
        String uploadId = request.get("uploadId");
        UploadSession session = uploadSessions.get(uploadId);
        
        if (session == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Upload session not found"));
        }
        
        if (session.receivedChunks != session.totalChunks) {
            return ResponseEntity.status(400).body(Map.of("error", 
                    "Cannot finalize incomplete upload. Received " + session.receivedChunks + 
                    " of " + session.totalChunks + " chunks"));
        }
        
        try {
            // Create directory if it doesn't exist
            Path dirPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
            
            // Generate unique filename
            String fileExtension = "";
            if (session.fileName.contains(".")) {
                fileExtension = session.fileName.substring(session.fileName.lastIndexOf("."));
            }
            String newFilename = UUID.randomUUID().toString() + fileExtension;
            Path filePath = Paths.get(UPLOAD_DIR + newFilename);
            
            // Combine chunks into final file
            try (FileOutputStream outputStream = new FileOutputStream(filePath.toFile())) {
                for (int i = 0; i < session.totalChunks; i++) {
                    Path chunkPath = Paths.get(TEMP_DIR + session.tempDir + "/" + i);
                    byte[] chunkData = Files.readAllBytes(chunkPath);
                    outputStream.write(chunkData);
                    
                    // Delete chunk after writing
                    Files.delete(chunkPath);
                }
            }
            
            // Delete temp directory
            Files.delete(Paths.get(TEMP_DIR + session.tempDir));
            
            // Remove session
            uploadSessions.remove(uploadId);
            
            return ResponseEntity.ok(Map.of("message", "File uploaded successfully", "filename", newFilename));
            
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to finalize upload: " + e.getMessage()));
        }
    }
    
    @GetMapping("/status/{uploadId}")
    public ResponseEntity<Map<String, Object>> getUploadStatus(@PathVariable String uploadId) {
        UploadSession session = uploadSessions.get(uploadId);
        
        if (session == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Upload session not found"));
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("fileName", session.fileName);
        response.put("fileSize", session.fileSize);
        response.put("receivedChunks", session.receivedChunks);
        response.put("totalChunks", session.totalChunks);
        response.put("progress", (double) session.receivedChunks / session.totalChunks);
        
        return ResponseEntity.ok(response);
    }
    
    // Optional: Endpoint to check if chunks already exist (to resume after app restart)
    @GetMapping("/chunks/{uploadId}")
    public ResponseEntity<Map<String, Object>> getExistingChunks(@PathVariable String uploadId) {
        UploadSession session = uploadSessions.get(uploadId);
        
        if (session == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Upload session not found"));
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("uploadId", uploadId);
        
        try {
            Path tempDir = Paths.get(TEMP_DIR + session.tempDir);
            if (Files.exists(tempDir)) {
                File[] chunkFiles = tempDir.toFile().listFiles();
                
                if (chunkFiles != null) {
                    int[] existingChunks = new int[chunkFiles.length];
                    
                    for (int i = 0; i < chunkFiles.length; i++) {
                        existingChunks[i] = Integer.parseInt(chunkFiles[i].getName());
                    }
                    
                    response.put("existingChunks", existingChunks);
                }
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to retrieve chunks: " + e.getMessage()));
        }
    }
}
