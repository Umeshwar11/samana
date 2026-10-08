package com.samana.config;

import com.samana.entity.Category;
import com.samana.repository.CategoryRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryDataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public CategoryDataInitializer(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {

        List<String> categories = List.of(
                "Adventure",
                "Festival Experiences",
                "Cultural Experiences",
                "Food & Culinary Experiences",
                "Heritage Experiences",
                "Nature Experiences",
                "Art & Craft Experiences",
                "Wellness Experiences",
                "Photography Experiences",
                "Local Life Experiences",
                "Music Experiences",
                "Sports Experiences",
                "Family Experiences",
                "Spiritual Experiences",
                "Workshop Experiences",
                "Guided Tour Experiences",
                "Community Experiences"
        );

        for (String categoryName : categories) {

            if (!categoryRepository.existsByName(categoryName)) {

                Category category = new Category();

                category.setName(categoryName);

                category.setDescription(
                        categoryName + " offered by SAMANA hosts."
                );

                categoryRepository.save(category);
            }
        }

        System.out.println("SAMANA categories initialized successfully.");
    }
}