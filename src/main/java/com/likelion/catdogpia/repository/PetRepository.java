package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.entity.mypage.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
}
