package com.global.hr.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.global.hr.models.Video;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByCourseId(Long courseId); // جلب كل الفيديوهات الخاصة بكورس معين
}
