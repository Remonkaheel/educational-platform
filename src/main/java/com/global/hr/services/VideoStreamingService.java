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

    // الحصول على ملف الفيديو كمورد
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

    // تدفق الفيديو مع دعم الإيقاف المؤقت واستئناف التشغيل
    public byte[] streamVideoChunk(String filename, long start, long end) throws IOException {
        File file = new File(uploadDir + File.separator + filename);
        
        if (!file.exists()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Video not found: " + filename);
        }

        long fileSize = file.length();
        
        // إذا كانت نهاية الملف غير محددة، استخدم حجم الملف
        if (end <= 0) {
            end = fileSize - 1;
        }
        
        // التحقق من صحة النطاق
        if (start < 0 || end >= fileSize || start > end) {
            throw new ResponseStatusException(
                    HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE, "Invalid range");
        }
        
        // حساب حجم الجزء
        long chunkSize = end - start + 1;
        byte[] buffer = new byte[(int) chunkSize];
        
        // قراءة الجزء المطلوب من الملف
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            randomAccessFile.seek(start);
            randomAccessFile.read(buffer, 0, (int) chunkSize);
            return buffer;
        }
    }

    // الحصول على صورة مصغرة للفيديو (يمكن استخدام مكتبة مثل FFmpeg)
    public byte[] generateThumbnail(String filename) throws IOException {
        // استخدم FFmpeg أو أي مكتبة أخرى لاستخراج صورة مصغرة من الفيديو
        // هذا مثال بسيط - في التطبيق الفعلي ستحتاج إلى استخدام مكتبة مثل JavaCV
        
        // لغرض المثال، نعيد صورة افتراضية
        Path defaultThumbnailPath = Paths.get(uploadDir, "default-thumbnail.jpg");
        return java.nio.file.Files.readAllBytes(defaultThumbnailPath);
    }
}