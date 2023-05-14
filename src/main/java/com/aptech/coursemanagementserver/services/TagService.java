package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.models.Tag;

public interface TagService {
    public Tag findTagByName(String tagName);

    public List<Tag> findAll();

    public boolean save(Tag tag);

    public boolean saveAll(List<Tag> tags);
}
