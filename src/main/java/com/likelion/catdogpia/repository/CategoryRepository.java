package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    @Query("select c " +
            " from CategoryEntity c " +
            "where c.useYn = 'Y'")
    List<CategoryEntity> findByUseYnEqualsY();
}
