package com.blsa.ezilog.dao;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.blsa.ezilog.model.category.Category;

public interface CategoryDao extends JpaRepository<Category, Long> {
    
    
    Optional<Category> findCategoryByName(String name);
    
    
    @Query(value = "SELECT * FROM category",
            countQuery = "SELECT count(*) FROM category",
            nativeQuery = true)
    Page<Category> categoryList(Pageable request);
    
}
