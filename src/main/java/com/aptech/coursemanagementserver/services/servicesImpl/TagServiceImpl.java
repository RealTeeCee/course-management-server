package com.aptech.coursemanagementserver.services.servicesImpl;

import java.util.List;

import org.springframework.stereotype.Service;

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
    public boolean save(Tag tag) {
        tagRepository.save(tag);
        return true;
    }

    @Override
    public boolean saveAll(List<Tag> tags) {
        tagRepository.saveAll(tags);
        return true;
    }

}
