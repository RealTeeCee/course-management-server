package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.PartDto;

public interface PartService {
    public PartDto findById(long id);

    public List<PartDto> findAll();

    public void save(PartDto authorDto);

    public void deletePart(long authorId);
}
