package com.blsa.ezilog.dao;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blsa.ezilog.model.blog.Blog;

public interface BlogDao extends JpaRepository<Blog, String> {
    Optional<Blog> getBlogByName(String name);

    Optional<Blog> getBlogByUrl(String url);

    Optional<Blog> getBlogById(BigInteger id);
}
