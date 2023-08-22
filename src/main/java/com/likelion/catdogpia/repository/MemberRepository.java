package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.dto.admin.MemberDto;
import com.likelion.catdogpia.domain.entity.user.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("select new com.likelion.catdogpia.domain.dto.admin.MemberDto(" +
            "m.name, m.loginId, m.nickname, m.phone, " +
            "m.email, a.address, m.role, m.createdAt, m.blackListYn, " +
            "(select coalesce(sum(p.point), 0) from Point p where p.member = m)," +
            "(select coalesce(count(r.id), 0) from Report r where r.member = m), " +
            "(select coalesce(sum(o.totalAmount), 0) from Orders o where o.member = m)) " +
            "from Member m " +
            "left join fetch Point p on p.member = m " +
            "left join fetch Address a on a.member = m " +
            "where m.id = :memberId")
    MemberDto findByMember(Long memberId);
}
