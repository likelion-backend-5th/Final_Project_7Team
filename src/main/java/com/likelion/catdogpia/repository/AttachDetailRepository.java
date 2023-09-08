package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.entity.attach.Attach;
import com.likelion.catdogpia.domain.entity.attach.AttachDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttachDetailRepository extends JpaRepository<AttachDetail, Long> {
    @Query("select a.fileUrl " +
            "from AttachDetail a " +
            "where a.attach =:attach " +
            "order by a.id")
    List<String> findFileUrls(Attach attach);
}
