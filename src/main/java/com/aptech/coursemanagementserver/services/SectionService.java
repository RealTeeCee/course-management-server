package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.SectionDto;
import com.aptech.coursemanagementserver.dtos.baseDto.BaseDto;
import com.aptech.coursemanagementserver.models.Section;

public interface SectionService {
    public Section findSectionByName(String sectionName);

    public SectionDto findAllNameByCourseId(long courseId);

    public List<Section> findAllByCourseId(long courseId);

    public List<Section> findAll();

    public BaseDto saveSectionsToCourse(SectionDto section, long courseId);
}
