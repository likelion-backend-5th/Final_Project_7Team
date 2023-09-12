package com.likelion.catdogpia.repository;


import com.likelion.catdogpia.domain.entity.notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
