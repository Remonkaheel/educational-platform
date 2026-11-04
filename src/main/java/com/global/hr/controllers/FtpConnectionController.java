package com.global.hr.controllers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

@RestController
public class FtpConnectionController {

    @Value("${ftp.host:localhost}")
    private String ftpHost;

    @Value("${ftp.username:}")
    private String ftpUsername;

    @Value("${ftp.password:}")
    private String ftpPassword;

    @Value("${ftp.port:2121}")
    private int ftpPort;

    @GetMapping("/ftp/connection-details")
    public FtpConnectionDetails getFtpConnectionDetails() {
        // FTP connection details come from the configuration (see application.properties)
        return new FtpConnectionDetails(ftpHost, ftpUsername, ftpPassword, ftpPort);
    }

    // Method to retrieve the server's IPv4 address
    private String getServerIPv4() {
        try {
            // Get the local host (server)
            InetAddress inetAddress = InetAddress.getLocalHost();
            
            // If the local host name resolves to an IPv6 address, find the first IPv4 address
            if (inetAddress.getHostAddress().contains(":")) {
                Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                while (networkInterfaces.hasMoreElements()) {
                    NetworkInterface networkInterface = networkInterfaces.nextElement();
                    Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress address = addresses.nextElement();
                        if (address instanceof java.net.Inet4Address) {
                            return address.getHostAddress();  // Return the first IPv4 address found
                        }
                    }
                }
            } else {
                // If it's already an IPv4 address, return it directly
                return inetAddress.getHostAddress();
            }
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
        return "Unable to retrieve IP";
    }

    // Class to represent FTP connection details
    public static class FtpConnectionDetails {
        private String ip;
        private String username;
        private String password;
        private int port;

        // Constructor
        public FtpConnectionDetails(String ip, String username, String password, int port) {
            this.ip = ip;
            this.username = username;
            this.password = password;
            this.port = port;
        }

        // Getters and Setters
        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }
}










































//package com.global.hr.controllers;
//
//import com.global.hr.models.Video;
//import com.global.hr.services.VideoService;
//import com.global.hr.services.VideoStreamingService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.io.IOException;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/videos")
//public class VideoController {
//
//    @Autowired
//    private VideoService videoService;
//
//    @Autowired
//    private VideoStreamingService videoStreamingService;
//
//    // Upload the video (instructors only)
//   // @PreAuthorize("hasRole('INSTRUCTOR')")
//    @PostMapping("/upload")
//    public ResponseEntity<Video> uploadVideo(
//            @RequestParam("file") MultipartFile file,
//            @RequestParam("title") String title,
//            @RequestParam("description") String description,
//            @RequestParam("courseId") Long courseId
//    ) {
//        try {
//            Video uploadedVideo = videoService.uploadVideo(file, title, description, courseId);
//            return ResponseEntity.ok(uploadedVideo);
//        } catch (IOException e) {
//            throw new ResponseStatusException(
//                    HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload video", e);
//        }
//    }
//
//    // Stream the video with pause and resume support
//    @GetMapping("/stream/{filename}")
//    public ResponseEntity<byte[]> streamVideo(
//            @PathVariable String filename,
//            @RequestHeader(value = "Range", required = false) String rangeHeader
//    ) {
//        try {
//            // Parse the Range header if present
//            long start = 0;
//            long end = 0;
//            
//            if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
//                String[] ranges = rangeHeader.substring(6).split("-");
//                start = Long.parseLong(ranges[0]);
//                
//                if (ranges.length > 1 && !ranges[1].isEmpty()) {
//                    end = Long.parseLong(ranges[1]);
//                }
//            }
//            
//            // Get the file information
//            Resource videoResource = videoStreamingService.getVideoAsResource(filename);
//            long fileSize = videoResource.contentLength();
//            
//            // If the end is not specified, use the file size
//            if (end <= 0) {
//                end = fileSize - 1;
//            }
//            
//            // Read the requested chunk of the video
//            byte[] data = videoStreamingService.streamVideoChunk(filename, start, end);
//            
//            // Build the response
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("Content-Range", String.format("bytes %d-%d/%d", start, end, fileSize));
//            headers.add("Accept-Ranges", "bytes");
//            headers.add("Content-Type", "video/mp4");
//            headers.add("Content-Length", String.valueOf(data.length));
//            
//            return new ResponseEntity<>(data, headers, HttpStatus.PARTIAL_CONTENT);
//        } catch (IOException e) {
//            throw new ResponseStatusException(
//                    HttpStatus.INTERNAL_SERVER_ERROR, "Failed to stream video", e);
//        }
//    }
//
//    // Get a video thumbnail
//    @GetMapping("/thumbnail/{filename}")
//    public ResponseEntity<byte[]> getVideoThumbnail(@PathVariable String filename) {
//        try {
//            byte[] thumbnailData = videoStreamingService.generateThumbnail(filename);
//            
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.IMAGE_JPEG);
//            
//            return new ResponseEntity<>(thumbnailData, headers, HttpStatus.OK);
//        } catch (IOException e) {
//            throw new ResponseStatusException(
//                    HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate thumbnail", e);
//        }
//    }
//
//    // Get the video information
//    @GetMapping("/{id}/info")
//    public ResponseEntity<VideoService.VideoInfo> getVideoInfo(@PathVariable Long id) {
//        try {
//            VideoService.VideoInfo info = videoService.getVideoInfo(id);
//            return ResponseEntity.ok(info);
//        } catch (IOException e) {
//            throw new ResponseStatusException(
//                    HttpStatus.INTERNAL_SERVER_ERROR, "Failed to get video info", e);
//        }
//    }
//}











//package com.global.hr.controllers;
//
//import java.io.IOException;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.global.hr.models.Video;
//import com.global.hr.services.VideoService;
//
//@RestController
//@RequestMapping("/api/videos")
//public class VideoController {
//
//    @Autowired
//    private VideoService videoService;
//
//    @GetMapping
//    public ResponseEntity<List<Video>> getAllVideos() {
//        return ResponseEntity.ok(videoService.getAllVideos());
//    }
//
//    @GetMapping("/course/{courseId}")
//    public ResponseEntity<List<Video>> getVideosByCourse(@PathVariable Long courseId) {
//        return ResponseEntity.ok(videoService.getVideosByCourse(courseId));
//    }
//
//    @PostMapping("/upload/{courseId}")
//    public ResponseEntity<String> uploadVideo(
//            @PathVariable Long courseId,
//            @ModelAttribute Video video,
//            @RequestParam("file") MultipartFile file) throws IOException {
//
//        videoService.saveVideo(courseId, video, file);
//        return ResponseEntity.ok("Video uploaded successfully!");
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Video> getVideoById(@PathVariable Long id) {
//        return ResponseEntity.ok(videoService.getVideoById(id));
//    }
//}
