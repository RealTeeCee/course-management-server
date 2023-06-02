package com.aptech.coursemanagementserver.services.servicesImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.aptech.coursemanagementserver.dtos.CategoryDto;
import com.aptech.coursemanagementserver.models.Category;
import com.aptech.coursemanagementserver.repositories.CategoryRepository;
import com.aptech.coursemanagementserver.services.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category save(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        categoryRepository.save(category);
        return category;
    }

    @Override
    public boolean saveAll(List<CategoryDto> categoryDtos) {
        List<Category> categories = new ArrayList<>();

        for (CategoryDto categoryDto : categoryDtos) {
            Category category = new Category();
            category.setName(categoryDto.getName());
            categories.add(category);
        }
        categoryRepository.saveAll(categories);
        return true;
    }

    @Override
    public Category findById(long id) {
        return categoryRepository.findById(id).get();
    }

}
