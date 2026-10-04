package com.samana.repository;

import com.samana.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {

    List<Experience> findByHostId(Long hostId);

    List<Experience> findByCategoryId(Long categoryId);
}