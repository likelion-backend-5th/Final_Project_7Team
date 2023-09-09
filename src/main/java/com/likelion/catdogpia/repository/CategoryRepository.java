package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    // 소분류
//    List<CategoryEntity> findByCategoryId(Long categoryId);

    // 대분류
    List<CategoryEntity> findByParentCategory_Id(Long categoryId);

    Optional<CategoryEntity> findCategoryByParentCategoryId(Long categoryId);

    List<CategoryEntity> findAllById(Long categoryId);
  
      @Query("select c " +
            " from CategoryEntity c " +
            "where c.useYn = 'Y'")
    List<CategoryEntity> findByUseYnEqualsY();

    List<CategoryEntity> findByParentCategory_Name(String name);
}
