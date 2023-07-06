package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.SearchDto;

public interface HomeService {
    public List<SearchDto> searchAll(String name);
}
