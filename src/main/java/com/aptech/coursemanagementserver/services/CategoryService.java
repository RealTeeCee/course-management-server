package com.aptech.coursemanagementserver.services;

import java.util.List;

import com.aptech.coursemanagementserver.dtos.CategoryDto;
import com.aptech.coursemanagementserver.models.Category;

public interface CategoryService {
    public Category findById(long id);

    public Category save(CategoryDto category);

    public boolean saveAll(List<CategoryDto> categories);
}
