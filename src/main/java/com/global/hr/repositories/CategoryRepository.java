package com.global.hr.repositories;

import com.global.hr.models.Category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	List<Category> findByNameIn(List<String> expertsInterests);
}
