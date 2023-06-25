package com.aptech.coursemanagementserver.services.servicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.PartDto;
import com.aptech.coursemanagementserver.exceptions.BadRequestException;
import com.aptech.coursemanagementserver.models.Course;
import com.aptech.coursemanagementserver.models.Part;
import com.aptech.coursemanagementserver.repositories.CourseRepository;
import com.aptech.coursemanagementserver.repositories.PartRepository;
import com.aptech.coursemanagementserver.services.PartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class PartServiceImpl implements PartService {
    private final PartRepository partRepository;
    private final CourseRepository courseRepository;

    @Override
    public PartDto findById(long id) {
        Part part = partRepository.findById(id).orElseThrow(() -> new NoSuchElementException(
                "This part with partId: [" + id + "] is not exist."));

        return toDto(part);
    }

    @Override
    public List<PartDto> findAll() {
        List<Part> parts = partRepository.findAll();
        List<PartDto> partDtos = new ArrayList<>();

        for (Part part : parts) {
            PartDto partDto = toDto(part);
            partDtos.add(partDto);
        }

        return partDtos;
    }

    @Override
    public void save(PartDto partDto) {
        Part part = new Part();
        if (partDto.getId() > 0) {
            part = partRepository.findById(partDto.getId()).orElseThrow(
                    () -> new NoSuchElementException(
                            "This part with partId: [" + partDto.getId() + "] is not exist."));
        }
        Course course = courseRepository.findById(partDto.getCourseId()).orElseThrow(
                () -> new NoSuchElementException(
                        "This course with courseId: [" + partDto.getCourseId() + "] is not exist."));

        part.setCourse(course);
        part.setLimitTime(partDto.getLimitTime());
        part.setMaxPoint(partDto.getMaxPoint());

        if (part.getStatus() == 0 && partDto.getStatus() == 1 && part.getQuestions().size() < 4) {
            throw new BadRequestException("The limit question must be greater than or equal 4.");
        }
        part.setStatus(partDto.getStatus());

        partRepository.save(part);
    }

    @Override
    public void deletePart(long partId) {
        Part part = partRepository.findById(partId).orElseThrow(
                () -> new NoSuchElementException("This part with partId: [" + partId + "] is not exist."));

        if (part.getQuestions().size() > 0) {
            throw new BadRequestException("The part 've already had question.");
        }
        partRepository.delete(part);
    }

    private PartDto toDto(Part part) {
        PartDto partDto = PartDto.builder()
                .id(part.getId())
                .maxPoint(part.getMaxPoint())
                .limitTime(part.getLimitTime())
                .courseId(part.getCourse().getId())
                .build();
        return partDto;
    }
}
