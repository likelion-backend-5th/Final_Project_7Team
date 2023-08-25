package com.likelion.catdogpia.service;

import com.likelion.catdogpia.domain.dto.admin.*;
import com.likelion.catdogpia.domain.entity.CategoryEntity;
import com.likelion.catdogpia.domain.entity.attach.Attach;
import com.likelion.catdogpia.domain.entity.attach.AttachDetail;
import com.likelion.catdogpia.domain.entity.product.Product;
import com.likelion.catdogpia.domain.entity.product.ProductOption;
import com.likelion.catdogpia.domain.entity.user.Member;
import com.likelion.catdogpia.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    // 회원관련
    private final MemberRepository memberRepository;
    // 상품 관련
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final AttachRepository attachRepository;
    private final AttachDetailRepository attachDetailRepository;
    private final S3UploadService s3UploadService;
    // 공통
    private final QueryRepository queryRepository;

    // 회원관리 목록
    public Page<MemberListDto> findMemberList(Pageable pageable, String filter, String keyword) {
        // 사용자가 관리자인지 확인하는 로직 필요

        // 목록 리턴
        return queryRepository.findByFilterAndKeyword(pageable, filter, keyword);
    }

    // 사용자 상세 조회
    public MemberDto findMember(Long memberId) {
        // 관리자인지 확인하는 로직 필요

        return memberRepository.findByMember(memberId);
    }

    // 사용자 정보 변경
    @Transactional
    public void modifyMember(MemberDto memberDto, Long memberId) {
        // 관리자인지 확인하는 로직 필요

        // 회원 수정
        Member findMember = memberRepository.findById(memberId).orElseThrow(RuntimeException::new);
        findMember.changeMember(memberDto);
    }

    // 이메일, 닉네임 사용여부 확인
    public Boolean isDuplicated(String email,String nickname, Long memberId) {
        return memberRepository.findByEmailOrNicknameAndIdNot(email, nickname, memberId).isEmpty();
    }

    // 회원 삭제
    @Transactional
    public void removeMember(Long memberId) {
        // 관리자 인지 확인하는 로직 필요

        Member findMember = memberRepository.findById(memberId).orElseThrow(IllegalArgumentException::new);
        // 회원 삭제
        memberRepository.delete(findMember);
    }

    // 상품 목록
    public Page<ProductListDto> findProductList(Pageable pageable, String filter, String keyword) {
        // 관리자 인지 확인하는 로직 필요

        // 목록 조회
        return queryRepository.findByProductAndFilterAndKeyword(pageable, filter, keyword);
    }

    // 카테고리 목록
    public List<CategoryDto> findCategory() {
        // 넘겨질 list 생성
        List<CategoryDto> returnList = new ArrayList<>();
        // 카테고리 목록 조회
        List<CategoryEntity> findCategories = categoryRepository.findByUseYnEqualsY();

        // 카테고리가 없으면 오류 발생
        if(findCategories.isEmpty()) {
            log.info("findCategories is empty!");
            throw new RuntimeException();
        }

        // Entity -> DTO
        for (CategoryEntity findCategory : findCategories) {
            returnList.add(CategoryDto.fromEntity(findCategory));
        }

        return returnList;
    }

    // 상품 등록
    @Transactional
    public void createProduct(ProductDto productDto, MultipartFile mainImg, MultipartFile detailImg) throws IOException {

        // 관리자인지 확인하는 로직 필요

        // 상품 등록 로직
        // 1. 등록할 카테고리를 찾아옴
        CategoryEntity findCategory = categoryRepository.findById(productDto.getCategoryId()).orElseThrow(IllegalArgumentException::new);
        // 2. 상품 등록 전 상품 이미지를 S3로 업로드
        // 파일 ID 생성
        log.info("파일 ID 생성");
        Attach saveAttach = attachRepository.save(Attach.builder().createdAt(LocalDateTime.now()).build());
        // 대표 이미지 저장
        String mainImgFileUrl = s3UploadService.upload(mainImg, "product");
        attachDetailRepository.save(AttachDetail.builder()
                .attach(saveAttach)
                .fileSize(mainImg.getSize())
                .realname(mainImg.getOriginalFilename())
                .fileUrl(mainImgFileUrl)
                .build());
        log.info("대표 이미지 저장 완료");
        // 상품 상세 이미지 저장
        String detailImgFileUrl = s3UploadService.upload(detailImg, "product");
        attachDetailRepository.save(AttachDetail.builder()
                .attach(saveAttach)
                .fileSize(detailImg.getSize())
                .realname(detailImg.getOriginalFilename())
                .fileUrl(detailImgFileUrl)
                .build());
        log.info("상품 상세 이미지 저장 완료");
        // 3. 상품 등록
        Product saveProduct = productRepository.save(Product.builder()
                .name(productDto.getName())
                .price(productDto.getPrice())
                .status(productDto.getStatus())
                .category(findCategory)
                .attach(saveAttach)
                .build());
        log.info("상품 등록 완료");
        // 4. 상품 옵션 등록
        for (ProductOptionDto productOption : productDto.getProductOptionList()) {
                productOptionRepository.save(ProductOption.builder()
                        .color(productOption.getColor())
                        .size(productOption.getSize())
                        .stock(productOption.getStock())
                        .product(saveProduct)
                        .build());
        }
        log.info("상품 옵션 저장 완료");
    }
}

