package com.aptech.coursemanagementserver.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aptech.coursemanagementserver.models.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {
    Section findSectionByName(String name);

    List<Section> findAllByCourseId(long courseId);

    @Query("SELECT s FROM Section s JOIN s.course c WHERE c.name = :courseName")
    Set<Section> findAllByCourseName(@Param("courseName") String courseName);
}
