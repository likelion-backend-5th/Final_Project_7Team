package com.likelion.catdogpia.service;

import com.likelion.catdogpia.domain.dto.mypage.ReviewFormDto;
import com.likelion.catdogpia.domain.dto.mypage.ReviewListDto;
import com.likelion.catdogpia.domain.dto.mypage.ReviewProductDto;
import com.likelion.catdogpia.domain.dto.mypage.ReviewUpdateDto;
import com.likelion.catdogpia.domain.entity.attach.Attach;
import com.likelion.catdogpia.domain.entity.attach.AttachDetail;
import com.likelion.catdogpia.domain.entity.product.OrderProduct;
import com.likelion.catdogpia.domain.entity.review.Review;
import com.likelion.catdogpia.domain.entity.user.Member;
import com.likelion.catdogpia.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final AttachRepository attachRepository;
    private final AttachDetailRepository attachDetailRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderRepository orderRepository;

    private final S3UploadService s3UploadService;

    // 리뷰 내역 조회
    @Transactional(readOnly = true)
    public Page<ReviewListDto> findAllReview(String loginId, Integer page) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<ReviewListDto> reviewListPage = reviewRepository.findAllByMemberId(pageable, member.getId());

        return reviewListPage;
    }

    // 리뷰 작성 페이지
    public ReviewProductDto getOrderProduct(String loginId, Long opId) {

        return orderProductRepository.findProductByOrderProductId(opId);
    }

    // 리뷰 등록
    public void saveReview(String loginId, Long opId, MultipartFile reviewImg, ReviewFormDto reviewFormDto) throws IOException {

        // 상품 구매자와 일치하는지 확인
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        OrderProduct orderProduct = orderProductRepository.findById(opId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Long orderMemberId = orderRepository.findMemberIdByOrderProductId(opId);
        if (orderMemberId != member.getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "member 불일치");
        }

        // 이미 등록한 리뷰가 있는지 확인
        if (reviewRepository.findByOrderProductId(opId) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 등록한 리뷰 존재");
        }

        Attach attach = null;
        // (첨부 이미지가 있다면) 리뷰 이미지 S3로 업로드
        if (reviewImg != null) {
            attach = attachRepository.save(Attach.builder().createdAt(LocalDateTime.now()).build());
            String reviewImgFileUrl = s3UploadService.upload(reviewImg, "review");
            attachDetailRepository.save(AttachDetail.builder()
                    .attach(attach)
                    .fileSize(reviewImg.getSize())
                    .realname(reviewImg.getOriginalFilename())
                    .fileUrl(reviewImgFileUrl)
                    .build());
            log.info("리뷰 이미지 저장 완료");
        }

        // 리뷰 등록
        reviewRepository.save(Review.builder()
                .member(member)
                .orderProduct(orderProduct)
                .attach(attach)
                .description(reviewFormDto.getDescription())
                .rating(reviewFormDto.getRating())
                .build());
        log.info("리뷰 등록 완료");

        // TODO 포인트 적립
    }

    // 리뷰 조회
    public ReviewUpdateDto getReview(String loginId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ReviewUpdateDto.fromEntity(review);
    }

    // 리뷰 수정
    @Transactional
    public void updateReview(String loginId, Long reviewId, MultipartFile reviewImg, ReviewFormDto reviewFormDto) throws IOException {

        // 리뷰 조회
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 이미지 파일 변경
        Attach attach = null;
        if (reviewImg != null && review.getAttach() != null) {
            AttachDetail attachDetail = review.getAttach().getAttachDetailList().get(0);
            String filename = attachDetail.getFileUrl();
            int idx = filename.indexOf("review/");

            String deleteFilename = attachDetail.getFileUrl().substring(idx);
            s3UploadService.deleteFile(deleteFilename);

            String reviewImgFileUrl = s3UploadService.upload(reviewImg, "review");

            attachDetail.changeFile(
                    attachDetail.getId(),
                    reviewImgFileUrl,
                    reviewImg.getSize(),
                    reviewImg.getOriginalFilename(),
                    review.getAttach());
        } else if (reviewImg != null && review.getAttach() == null) {
            attach = attachRepository.save(Attach.builder().createdAt(LocalDateTime.now()).build());
            String mainImgFileUrl = s3UploadService.upload(reviewImg, "review");
            attachDetailRepository.save(AttachDetail.builder()
                    .attach(attach)
                    .fileSize(reviewImg.getSize())
                    .realname(reviewImg.getOriginalFilename())
                    .fileUrl(mainImgFileUrl)
                    .build());

            review.updateReviewAttach(attach);
        } else if (reviewImg == null && review.getAttach() != null) { // 기존 이미지 삭제
            AttachDetail attachDetail = review.getAttach().getAttachDetailList().get(0);
            String filename = attachDetail.getFileUrl();
            int idx = filename.indexOf("review/");

            String deleteFilename = attachDetail.getFileUrl().substring(idx);
            s3UploadService.deleteFile(deleteFilename);

            // 연관관계 제거후 attach 삭제
            Long attachId = review.getAttach().getId();
            review.deleteReviewAttach();
            attachDetailRepository.deleteById(attachDetail.getId());
            attachRepository.deleteById(attachId);
        }

        // 리뷰 정보 수정
        review.updateReview(reviewFormDto);
        log.info("리뷰 수정 완료");

    }

    // 리뷰 삭제
    public void deleteReview(String testtest, Long reviewId) {

        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        reviewRepository.deleteById(reviewId);
        
        // TODO 포인트 회수

    }
}
