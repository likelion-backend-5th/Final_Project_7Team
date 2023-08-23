package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.entity.mypage.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    // 특정 회원의 기본 배송지 여부 필터링 조회
    @Query("SELECT a FROM Address a WHERE a.member.id = :memberId AND a.defaultAddress = 'Y'")
    Optional<Address> findDefaultAddressesByMemberId(@Param("memberId") Long memberId);

}
