package com.aptech.coursemanagementserver.services.servicesImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.TagDto;
import com.aptech.coursemanagementserver.models.Tag;
import com.aptech.coursemanagementserver.repositories.TagRepository;
import com.aptech.coursemanagementserver.services.TagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public Tag findTagByName(String name) {
        return tagRepository.findTagByName(name);
    }

    @Override
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    @Override
    public boolean save(TagDto tagDto) {
        Tag tag = tagRepository.findTagByName(tagDto.getName());

        tagRepository.save(tag);
        return true;
    }

    @Override
    public boolean saveAll(List<TagDto> tagsDto) {
        List<Tag> tags = tagsDto.stream().map(tagDto -> findTagByName(tagDto.getName())).collect(Collectors.toList());
        tagRepository.saveAll(tags);
        return true;
    }

}
