package com.aptech.coursemanagementserver.services.servicesImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.SectionDto;
import com.aptech.coursemanagementserver.models.Section;
import com.aptech.coursemanagementserver.repositories.SectionRepository;
import com.aptech.coursemanagementserver.services.SectionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SectionServiceImpl implements SectionService {
    private final SectionRepository sectionRepository;

    @Override
    public Section findSectionByName(String sectionName) {
        return sectionRepository.findSectionByName(sectionName);
    }

    @Override
    public List<Section> findAllByCourseId(long courseId) {
        return sectionRepository.findAllByCourseId(courseId);
    }

    @Override
    public List<Section> findAll() {
        return sectionRepository.findAll();
    }

    @Override
    public Section save(SectionDto sectionDto) {
        Section section = sectionRepository.findSectionByName(sectionDto.getName());
        return sectionRepository.save(section);
    }

    @Override
    public List<Section> saveAll(List<SectionDto> sectionsDto) {
        List<Section> sections = sectionsDto.stream().map(sectionDto -> findSectionByName(sectionDto.getName()))
                .collect(Collectors.toList());
        return sectionRepository.saveAll(sections);
    }

}
