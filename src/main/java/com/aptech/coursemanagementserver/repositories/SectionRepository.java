package com.aptech.coursemanagementserver.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.aptech.coursemanagementserver.models.Section;

import jakarta.transaction.Transactional;

public interface SectionRepository extends JpaRepository<Section, Long> {
    Section findSectionByName(String name);

    @Query(value = """
            SELECT s FROM Section s INNER JOIN Course c\s
            ON s.course.id = c.id\s
            WHERE c.id = :courseId
            """)
    List<Section> findAllByCourseId(long courseId);

    @Query("SELECT s FROM Section s JOIN s.course c WHERE c.name = :courseName")
    Set<Section> findAllByCourseName(@Param("courseName") String courseName);

    @Modifying
    @Transactional
    @Query(value = "Delete from section WHERE id= :id", nativeQuery = true)
    void deleteSectionsById(long id);

}
