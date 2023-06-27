package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.SubcribesDto;

public interface SubcribesService {
    public SubcribesDto findById(long id);

    public List<SubcribesDto> findAll();

    public void subcribe(SubcribesDto subcribesDto);

    public void unSubcribes(long subcribesId);
}
