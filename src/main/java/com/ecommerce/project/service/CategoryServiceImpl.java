package com.ecommerce.project.service;

import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService{
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()) {
            throw new APIException("No categories found in the database.");
        }
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        Category existingCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if(existingCategory != null) {
            System.out.println(category.getCategoryName());
            throw new APIException("Category with this name "+ category.getCategoryName()+" already exists.");
        }
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        categoryRepository.delete(category);
        return "Updated Category with categoryId " + categoryId + " deleted successfully !!";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        Category savedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        Category existingCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if(existingCategory != null) {
            throw new APIException("Category with this name "+ category.getCategoryName()+" already exists.");
        }
        category.setCategoryId(categoryId);
        savedCategory = categoryRepository.save(category);
        return savedCategory;
    }

}
