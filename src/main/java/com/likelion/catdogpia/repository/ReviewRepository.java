package com.likelion.catdogpia.repository;

import com.likelion.catdogpia.domain.dto.mypage.ReviewListDto;
import com.likelion.catdogpia.domain.entity.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT NEW com.likelion.catdogpia.domain.dto.mypage.ReviewListDto(r.id, r.description, " +
            "(SELECT COALESCE(ad.fileUrl, '') FROM AttachDetail ad WHERE ad.attach.id = r.attach.id ORDER BY ad.id ASC), " +
            "(SELECT COUNT(ad) -1 FROM AttachDetail ad WHERE ad.attach.id = r.attach.id), " +
            "p.name, po.size, po.color, r.createdAt, r.rating) " +
            "FROM Review r " +
            "LEFT JOIN Attach a ON a.id = r.attach.id " +
            "LEFT JOIN AttachDetail ad ON ad.attach.id = r.attach.id " +
            "JOIN OrderProduct op ON op.id = r.orderProduct.id " +
            "JOIN ProductOption po ON po.id = op.productOption.id " +
            "JOIN Product p ON p.id = po.product.id ")
    Page<ReviewListDto> findAllByMemberId(Pageable pageable, @Param("memberId") Long memberId);

    Review findByOrderProductId(Long orderProductId);

}
