package com.blsa.ezilog.dao;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blsa.ezilog.model.category.Category;

public interface CategoryDao extends JpaRepository<Category, BigInteger> {
    
    
    Optional<Category> findCategoryByName(String name);
    
}
