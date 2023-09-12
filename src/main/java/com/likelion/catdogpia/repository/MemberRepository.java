package com.likelion.catdogpia.repository;


import com.likelion.catdogpia.domain.dto.admin.MemberDto;
import com.likelion.catdogpia.domain.dto.mypage.MemberProfileDto;
import com.likelion.catdogpia.domain.entity.user.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    
    Optional<Member> findByLoginId(String loginId);
    
    Boolean existsByLoginId(String loginId);

    Boolean existsByNickname(String nickname);

    Boolean existsByEmail(String email);

    Optional<Member> findByNameAndEmail(String name, String email);

    Optional<Member> findByLoginIdAndNameAndEmail(String loginId, String name, String email);

    Optional<Member> findByEmail(String email);
  
    @Query("select new com.likelion.catdogpia.domain.dto.admin.MemberDto(" +
            "m.id, m.name, m.loginId, m.nickname, m.phone, " +
            "m.email, a.address, m.role, m.createdAt, m.blackListYn, " +
            "(select coalesce(sum(p.point), 0) from Point p where p.member = m)," +
            "(select coalesce(count(r.id), 0) from Report r where r.member = m), " +
            "(select coalesce(sum(o.totalAmount), 0) from Orders o where o.member = m)) " +
            "from Member m " +
            "left join fetch Point p on p.member = m " +
            "left join fetch Address a on a.member = m and a.defaultAddress = 'Y'" +
            "where m.id = :memberId")
    MemberDto findByMember(Long memberId);

    // 닉네임, 이메일 사용여부
    @Query("select m from Member m " +
            "where m.id != :memberId " +
            "and(m.email = :email or m.nickname = :nickname)")
    List<Member> findByEmailOrNicknameAndIdNot(String email, String nickname, Long memberId);

    // 회원 정보 수정시 보여줄 데이터
    @Query("select new com.likelion.catdogpia.domain.dto.mypage.MemberProfileDto(m.loginId, m.name, m.phone, m.email, m.nickname)" +
            "FROM Member m " +
            "WHERE m.loginId = :loginId")
    MemberProfileDto findProfileDtoByLoginId(@Param("loginId") String loginId);
}
