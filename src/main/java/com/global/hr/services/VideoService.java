package com.global.hr.services;

import com.global.hr.models.Course;
import com.global.hr.models.Video;
import com.global.hr.repositories.CourseRepository;
import com.global.hr.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Value("${video.upload.dir}")
    private String uploadDir;

    public Video uploadVideo(MultipartFile file, String title, String description, Long courseId) throws IOException {
        // التحقق من وجود الكورس
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + courseId));

        // إنشاء مجلد التخزين إذا لم يكن موجوداً
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // إنشاء اسم فريد للملف
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
        
        // حفظ الملف على السيرفر
        String filePath = uploadDir + File.separator + uniqueFilename;
        Path path = Paths.get(filePath);
        Files.write(path, file.getBytes());

        // إنشاء وحفظ معلومات الفيديو في قاعدة البيانات
        Video video = new Video();
        video.setTitle(title);
        video.setDescription(description);
        video.setFilePath(filePath);
        video.setCourse(course);
        video.setVideoUrl("/api/videos/stream/" + uniqueFilename);
        video.setVideoThumbnailUrl("/api/videos/thumbnail/" + uniqueFilename);
        video.setRating(0.0); // تعيين تقييم مبدئي

        return videoRepository.save(video);
    }

    public byte[] getVideoBytes(String filename) throws IOException {
        Path path = Paths.get(uploadDir + File.separator + filename);
        return Files.readAllBytes(path);
    }

    public Video getVideoById(Long id) {
        return videoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Video not found with id: " + id));
    }

    // خدمة للحصول على معلومات الفيديو (الحجم والمدة)
    public VideoInfo getVideoInfo(Long id) throws IOException {
        Video video = getVideoById(id);
        File videoFile = new File(video.getFilePath());
        
        VideoInfo info = new VideoInfo();
        info.setSize(videoFile.length());
        // تحتاج مكتبة مثل mp4parser للحصول على مدة الفيديو
        // info.setDuration(getVideoDuration(videoFile));
        
        return info;
    }

    // كلاس لتخزين معلومات الفيديو
    public static class VideoInfo {
        private long size;
        private long duration;

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }
    }
}












//package com.global.hr.services;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.global.hr.models.Video;
//import com.global.hr.models.Course;
//import com.global.hr.repositories.VideoRepository;
//import com.global.hr.repositories.CourseRepository;
//
//@Service
//public class VideoService {
//    @Autowired
//    private VideoRepository videoRepository;
//    @Autowired
//    private CourseRepository courseRepository; // لجلب الكورس قبل ربط الفيديو به
//
//    private static final String UPLOAD_DIR = "src/main/resources/static/videos";
//
//    public List<Video> getAllVideos() {
//        return videoRepository.findAll();
//    }
//    public List<Video> getVideosByCourse(Long courseId) {
//        return videoRepository.findByCourseId(courseId);
//    }
//
//    public void saveVideo(Long courseId, Video video, MultipartFile file) throws IOException {
//    	 Course course = courseRepository.findById(courseId)
//    	            .orElseThrow(() -> new RuntimeException("Course not found"));
//    	if (!file.isEmpty()) {
//            // إنشاء مجلد التحميل إذا لم يكن موجودًا
//            Path uploadPath = (Path) Paths.get(UPLOAD_DIR);
//            if (!Files.exists(uploadPath)) {
//                Files.createDirectories(uploadPath);
//            }
//
//            // حفظ الملف في المجلد
//            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//            Path filePath = uploadPath.resolve(fileName);
//            Files.copy(file.getInputStream(), filePath);
//
//            // تعيين مسار الملف في الكيان
//            video.setFilePath(filePath.toString());
//        }
//        video.setCourse(course);
//        videoRepository.save(video);
//    }
//
//    public Video getVideoById(Long id) {
//        return videoRepository.findById(id).orElseThrow(() -> new RuntimeException("Video not found"));
//    }
//}
