package com.likelion.catdogpia.service;

import com.likelion.catdogpia.domain.dto.admin.*;
import com.likelion.catdogpia.domain.entity.CategoryEntity;
import com.likelion.catdogpia.domain.entity.attach.Attach;
import com.likelion.catdogpia.domain.entity.attach.AttachDetail;
import com.likelion.catdogpia.domain.entity.community.Article;
import com.likelion.catdogpia.domain.entity.community.Comment;
import com.likelion.catdogpia.domain.entity.order.Orders;
import com.likelion.catdogpia.domain.entity.product.OrderProduct;
import com.likelion.catdogpia.domain.entity.product.OrderStatus;
import com.likelion.catdogpia.domain.entity.product.Product;
import com.likelion.catdogpia.domain.entity.product.ProductOption;
import com.likelion.catdogpia.domain.entity.user.Member;
import com.likelion.catdogpia.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    // 상품관련
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final AttachRepository attachRepository;
    private final AttachDetailRepository attachDetailRepository;
    private final S3UploadService s3UploadService;
    // 주문관련
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    // 커뮤니티 관련
    private final CommunityRepository communityRepository;
    private final CommentRepository commentRepository;
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
        Member findMember = memberRepository.findById(memberId).orElseThrow(IllegalArgumentException::new);
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
            return returnList;
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

    // 상품 조회
    public ProductDto findProduct(Long productId) {
        // 상품 조회
        Product findProduct = productRepository.findById(productId).orElseThrow(IllegalArgumentException::new);

        return ProductDto.fromEntity(findProduct);
    }

    // 상품 수정
    @Transactional
    public void modifyProduct(Long productId, ProductDto product, MultipartFile mainImg, MultipartFile detailImg) throws IOException {

        // 상품 수정 로직

        // 1. 상품 id를 가지고 상품 조회
        Product findProduct = productRepository.findById(productId).orElseThrow(RuntimeException::new);

        // 2. 카테고리 변경
        if(!findProduct.getCategory().getParentCategory().getId().equals(product.getCategoryId())) {
            // 변경된 카테고리를 넣어줌
            CategoryEntity findCategory = categoryRepository.findById(product.getCategoryId()).orElseThrow(IllegalArgumentException::new);
            findProduct.changeCategory(findCategory);
        }

        // 3. 파일 변경
        // 대표이미지 변경
        if(mainImg != null) {
            // 인덱스값 추출
            AttachDetail findAttachDetail = findProduct.getAttach().getAttachDetailList().get(0);
            String filename = findAttachDetail.getFileUrl();
            int idx = filename.indexOf("product/");
            // 삭제할 파일명 추출
            String deleteFilename = findAttachDetail.getFileUrl().substring(idx);
            // 이미지 삭제
            s3UploadService.deleteFile(deleteFilename);
            // 이미지 업로드
            String mainImgFileUrl = s3UploadService.upload(mainImg, "product");
            log.info("이미지 : " + mainImgFileUrl);
            // 이미지 수정
            findAttachDetail.changeFile(
                    findAttachDetail.getId(),
                    mainImgFileUrl,
                    mainImg.getSize(),
                    mainImg.getOriginalFilename(),
                    findProduct.getAttach());
            log.info("대표 이미지 수정 완료");
        }
        // 상품 상세 이미지 변경
        if(detailImg != null) {
            // 인덱스값 추출
            AttachDetail findAttachDetail = findProduct.getAttach().getAttachDetailList().get(1);
            String filename = findAttachDetail.getFileUrl();
            int idx = filename.indexOf("product/");
            // 삭제할 파일명 추출
            String deleteFilename = findProduct.getAttach().getAttachDetailList().get(1).getFileUrl().substring(idx);
            // 이미지 삭제
            s3UploadService.deleteFile(deleteFilename);
            // 이미지 업로드
            String detailImgFileUrl = s3UploadService.upload(detailImg, "product");
            // 이미지 수정
            findAttachDetail.changeFile(
                    findAttachDetail.getId(),
                    detailImgFileUrl,
                    detailImg.getSize(),
                    detailImg.getOriginalFilename(),
                    findProduct.getAttach());
            log.info("상품 상세 이미지 수정 완료");
        }

        // 4. 상품 정보 수정
        findProduct.changeProduct(product);

        // 5. 상품 옵션 수정
        // 5-1. 기존 옵션이 더 많을 때(상품 옵션이 삭제 되었을 때)
        if(findProduct.getProductOptionList().size() > product.getProductOptionList().size()) {
            for (int i = 0; i < findProduct.getProductOptionList().size(); i++) {
                ProductOption productOption = findProduct.getProductOptionList().get(i);

                if(i < product.getProductOptionList().size()) {
                    ProductOptionDto productOptionDto = product.getProductOptionList().get(i);
                    log.info("Id" + productOption.getId());
                    log.info("dtoId" + productOption.getId());
                    if(productOption.getId().equals(productOptionDto.getId())) {
                        productOption.changeProductOption(productOptionDto, findProduct);
                    }
                } else {
                    productOptionRepository.delete(productOption);
                }
            }
        }
        // 5-2. 수정하는 옵션이 더 많을 때
        else if (findProduct.getProductOptionList().size() < product.getProductOptionList().size()) {
            for (int i = 0; i < product.getProductOptionList().size(); i++) {
                ProductOptionDto productOptionDto = product.getProductOptionList().get(i);

                if(i < findProduct.getProductOptionList().size()) {
                    ProductOption productOption = findProduct.getProductOptionList().get(i);
                    if(productOption.getId().equals(productOptionDto.getId())) {
                        productOption.changeProductOption(productOptionDto, findProduct);
                    } else {
                        // 기존 옵션을 삭제하고 새로운 옵션 추가
                        productOptionRepository.delete(productOption);
                        addProductOption(findProduct, productOptionDto);
                    }
                } else {
                    // 옵션 추가
                    addProductOption(findProduct, productOptionDto);
                }
            }
        }
        // 5-3. 리스트의 갯수가 같을 때
        else {
            for (int i = 0; i < product.getProductOptionList().size(); i++) {
                ProductOptionDto productOptionDto = product.getProductOptionList().get(i);
                ProductOption productOption = findProduct.getProductOptionList().get(i);

                if(productOption.getId().equals(productOptionDto.getId())) {
                    productOption.changeProductOption(productOptionDto, findProduct);
                } else {
                    // 기존 옵션을 삭제하고 새로운 옵션 추가
                    productOptionRepository.delete(productOption);
                    addProductOption(findProduct, productOptionDto);
                }
            }
        }

    }
    // 상품 옵션 추가
    private void addProductOption(Product findProduct, ProductOptionDto productOptionDto) {
        productOptionRepository.save(ProductOption.builder()
                .size(productOptionDto.getSize())
                .color(productOptionDto.getColor())
                .stock(productOptionDto.getStock())
                .product(findProduct)
                .build());
    }

    // 상품 삭제
    @Transactional
    public void removeProduct(Long productId) {
        // 권한 체크 필요

        // 상품 조회
        Product findProduct = productRepository.findById(productId).orElseThrow(IllegalArgumentException::new);
        // 상품 옵션 삭제
        productOptionRepository.deleteAll(findProduct.getProductOptionList());
        // 상품 삭제
        productRepository.delete(findProduct);
    }

    // 상품명 중복 확인
    public Boolean isDuplicatedProductName(String name, Long productId) {

        if(productId != null) {
            return productRepository.findByIdAndName(productId, name) != null;
        } else {
            return productRepository.findByName(name) != null;
        }
    }

    // 주문 목록
    public Page<OrderListDto> findOrderList(Pageable pageable, String filter, String keyword, String toDate, String fromDate, String orderStatus) {
        return queryRepository.findByOrderList(pageable, filter, keyword, toDate, fromDate, orderStatus != null ? OrderStatus.valueOf(orderStatus) : null);
    }

    // 주문 상태 변경
    @Transactional
    public void changeOrderStatus(List<OrderStatusUpdateDto> updateDtoList) {

        for (OrderStatusUpdateDto updateDto : updateDtoList) {
            // id를 가지고 해당 주문 상품을 찾음
            OrderProduct findOrderProduct = orderProductRepository.findById(updateDto.getId()).orElseThrow(IllegalArgumentException::new);
            // 이미 같으면 update 하지 않고 스킵
            if(findOrderProduct.getOrderStatus().name().equals(updateDto.getStatus())) continue;
            else {
                findOrderProduct.changeStatus(updateDto.getStatus());
            }
        }
    }

    // 주문내역 조회
    public List<OrderDto> findOrder(Long orderId) {
        // 해당 주문 내역이 있는지 확인 조회
        Orders orders = orderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);

        // 주문 목록 리턴
        return queryRepository.findOrder(orderId);
    }

    // 커뮤니티 목록 조회
    public Page<CommunityListDto> findCommunityList(Pageable pageable, String filter, String keyword) {
        return queryRepository.findByCommunityList(pageable, filter, keyword);
    }

    // 커뮤니티 삭제
    @Transactional
    public void deleteCommunities(List<Long> deleteList) {

        // 커뮤니티 id가 있으면 삭제
        for (Long deleteId : deleteList) {
            if(communityRepository.existsById(deleteId)) {
                communityRepository.deleteById(deleteId);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    // 커뮤니티 상세
    public CommunityDto findCommunity(Long communityId) {
        Article findArticle = communityRepository.findById(communityId).orElseThrow(IllegalArgumentException::new);
        // 커뮤니티 조회
        CommunityDto community = CommunityDto.fromEntity(findArticle);
        // 파일이 있으면 fileUrl 조회해서 넘김
        if(findArticle.getAttach() != null) {
            List<String> findList = attachDetailRepository.findFileUrls(findArticle.getAttach());
            community.setFileUrlList(findList);
        }
        return community;
    }

    // 댓글 조회
    public Page<CommentDto> findCommentList(Long communityId, Pageable pageable) {
        Article findArticle = communityRepository.findById(communityId).orElseThrow(IllegalArgumentException::new);
         // 댓글이 있으면 페이지 네이션
        if(!findArticle.getCommentList().isEmpty()) {
            return commentRepository.findByArticle(findArticle, pageable)
                            .map(c -> CommentDto.builder()
                                    .id(c.getId())
                                    .content(c.getContent())
                                    .commentWriter(c.getMember().getName())
                                    .createdAt(c.getCreatedAt())
                                    .build());

        } else return null;
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long communityId, Long commentId) {
        if(commentRepository.existsById(commentId)) {
            commentRepository.deleteById(commentId);
        } else {
            throw new IllegalArgumentException();
        }
    }

    // 댓글 등록
    @Transactional
    public void createComment(Long communityId, String content) {

        Article findArticle = communityRepository.findById(communityId).orElseThrow(IllegalArgumentException::new);
        // 권한에서 사용자에 대한 정보가져오기
        // 현재는 권한이 구현되지 않아 일단 1번으로 하기로함
        Member findMember = memberRepository.findById(1L).orElseThrow(IllegalArgumentException::new);

        commentRepository.save(Comment.builder()
                .article(findArticle)
                .content(content)
                .member(findMember)
                .build());
    }
}

