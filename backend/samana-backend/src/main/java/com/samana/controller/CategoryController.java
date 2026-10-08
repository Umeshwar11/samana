package com.samana.controller;

import com.samana.dto.CategoryRequest;
import com.samana.dto.CategoryResponse;
import com.samana.entity.Category;
import com.samana.service.CategoryService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin("*")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    // CREATE CATEGORY
    @PostMapping
    public CategoryResponse createCategory(
            @Valid @RequestBody CategoryRequest request) {

        Category category =
                categoryService.createCategory(request);

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }


    // GET ALL CATEGORIES
    @GetMapping
    public List<CategoryResponse> getAllCategories() {

        return categoryService.getAllCategories()
                .stream()
                .map(category -> new CategoryResponse(
                        category.getId(),
                        category.getName(),
                        category.getDescription()
                ))
                .toList();
    }


    // GET CATEGORY BY ID
    @GetMapping("/{id}")
    public CategoryResponse getCategoryById(
            @PathVariable Long id) {

        Category category =
                categoryService.getCategoryById(id);

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }


    // UPDATE CATEGORY
    @PutMapping("/{id}")
    public CategoryResponse updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {

        Category category =
                categoryService.updateCategory(id, request);

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }


    // DELETE CATEGORY
    @DeleteMapping("/{id}")
    public String deleteCategory(
            @PathVariable Long id) {

        return categoryService.deleteCategory(id);
    }
}
