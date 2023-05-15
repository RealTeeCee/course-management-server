package com.aptech.coursemanagementserver.services.servicesImpl;

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
    public List<Category> saveAll(List<CategoryDto> categories) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'saveAll'");
    }

    @Override
    public Category findById(long id) {
        return categoryRepository.findById(id).get();
    }

}
