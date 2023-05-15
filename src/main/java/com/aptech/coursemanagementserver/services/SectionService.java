package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.SectionDto;
import com.aptech.coursemanagementserver.models.Section;

public interface SectionService {
    public Section findSectionByName(String sectionName);

    public List<Section> findAllByCourseId(long courseId);

    public List<Section> findAll();

    public Section save(SectionDto section);

    public List<Section> saveAll(List<SectionDto> sections);
}
