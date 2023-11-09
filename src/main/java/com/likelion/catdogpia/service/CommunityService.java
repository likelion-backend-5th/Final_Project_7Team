package com.likelion.catdogpia.service;

import com.likelion.catdogpia.domain.dto.admin.CategoryDto;
import com.likelion.catdogpia.domain.dto.community.ArticleDto;
import com.likelion.catdogpia.domain.dto.community.ArticleListDto;
import com.likelion.catdogpia.domain.dto.community.HotArticleListDto;
import com.likelion.catdogpia.domain.dto.community.LikeArticleDto;
import com.likelion.catdogpia.domain.entity.CategoryEntity;
import com.likelion.catdogpia.domain.entity.attach.Attach;
import com.likelion.catdogpia.domain.entity.attach.AttachDetail;
import com.likelion.catdogpia.domain.entity.community.Article;
import com.likelion.catdogpia.domain.entity.community.Comment;
import com.likelion.catdogpia.domain.entity.community.LikeArticle;
import com.likelion.catdogpia.domain.entity.report.Report;
import com.likelion.catdogpia.domain.entity.user.Member;
import com.likelion.catdogpia.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommunityService {
    private final CategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final AttachRepository attachRepository;
    private final S3UploadService s3UploadService;
    private final AttachDetailRepository attachDetailRepository;
    private final CommentRepository commentRepository;
    private final QueryRepository queryRepository;
    private final LikeArticleRepository likeArticleRepository;
    private final ReportRepository reportRepository;

    // 커뮤니티 중분류 카테고리 받아오기
    public List<CategoryDto> findCategory() {
        // 넘겨질 list 생성
        List<CategoryDto> returnList = new ArrayList<>();

        // 커뮤니티 카테고리 목록 조회
        List<CategoryEntity> findCategories = categoryRepository.findByParentCategory_Name("커뮤니티");

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

    //글쓰기(이미지X)
    @Transactional
    public void createArticle(String title, String content, Long categoryId, String loginId) throws IOException {
        CategoryEntity findCategory = categoryRepository.findById(categoryId).orElseThrow(IllegalArgumentException::new);
        Optional<Member> member = memberRepository.findByLoginId(loginId);
        Attach saveAttach = attachRepository.save(Attach.builder().createdAt(LocalDateTime.now()).build());
        articleRepository.save(Article.builder()
                .viewCnt(0)
                .title(title)
                .content(content)
                .category(findCategory)
                .member(member.get())
                .attach(saveAttach)
                .build());
        log.info("커뮤니티 글 등록 완료");
    }

    //글쓰기(이미지 O)
    @Transactional
    public void createArticle(String title, String content, List<MultipartFile> images, Long categoryId, String loginId) throws IOException {
        CategoryEntity findCategory = categoryRepository.findById(categoryId).orElseThrow(IllegalArgumentException::new);
        Optional<Member> member = memberRepository.findByLoginId(loginId);
        Attach saveAttach = attachRepository.save(Attach.builder().createdAt(LocalDateTime.now()).build());
        for (MultipartFile img : images) {
            String imgUrl = s3UploadService.upload(img, "article");
            attachDetailRepository.save(AttachDetail.builder()
                    .attach(saveAttach)
                    .fileSize(img.getSize())
                    .realname(img.getOriginalFilename())
                    .fileUrl(imgUrl)
                    .build());
            log.info("이미지 저장 완료");
        }
        articleRepository.save(Article.builder()
                .viewCnt(0)
                .title(title)
                .content(content)
                .category(findCategory)
                .member(member.get())
                .attach(saveAttach)
                .build());
        log.info("커뮤니티 글 등록 완료");
    }

    //글조회
    public ArticleDto findArticle(Long articleId) {
        Article findArticle = articleRepository.findById(articleId).orElseThrow(IllegalArgumentException::new);

        return ArticleDto.fromEntity(findArticle);
    }

    //글수정(이미지 그대로)
    @Transactional
    public void updateArticle(Long articleId, String title, String content, Long categoryId, String loginId) {
        Optional<Article> optionalArticle = articleRepository.findById(articleId);
        if (optionalArticle.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        Article article = optionalArticle.get();
        if (article.getMember().getLoginId().equals(loginId)) {
            if (article.getDeletedAt() != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            //제목
            if (title != null)
                article.updateTitle(title);
            //내용
            if (content != null)
                article.updateContent(content);
            //카테고리
            if (!article.getCategory().getId().equals(categoryId)) {
                Optional<CategoryEntity> optionalCategory = categoryRepository.findById(categoryId);
                if(optionalCategory.isEmpty())
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                CategoryEntity newCategory = optionalCategory.get();
                article.updateCategory(newCategory);
            }
        }
    }

    //글수정 (이미지추가)
    @Transactional
    public void updateArticle(Long articleId, String title, String content, List<MultipartFile> images, Long categoryId, String loginId) {
        Optional<Article> optionalArticle = articleRepository.findById(articleId);
        if (optionalArticle.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        Article article = optionalArticle.get();
        if (article.getMember().getLoginId().equals(loginId)) {
            if (article.getDeletedAt() != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            //제목
            if (title != null)
                article.updateTitle(title);
            //내용
            if (content != null)
                article.updateContent(content);
            //카테고리
            if (!article.getCategory().getId().equals(categoryId)) {
                Optional<CategoryEntity> optionalCategory = categoryRepository.findById(categoryId);
                if(optionalCategory.isEmpty())
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                CategoryEntity newCategory = optionalCategory.get();
                article.updateCategory(newCategory);
            }
            //이미지
            List<AttachDetail> existingImages = article.getAttach().getAttachDetailList();
            //이미지 추가
            for(MultipartFile newImage : images) {
                try {
                    String imgUrl = s3UploadService.upload(newImage, "article");
                    AttachDetail newAttachDetail = AttachDetail.builder()
                            .attach(article.getAttach())
                            .fileSize(newImage.getSize())
                            .realname(newImage.getOriginalFilename())
                            .fileUrl(imgUrl)
                            .build();
                    attachDetailRepository.save(newAttachDetail);
                    log.info("이미지 저장 완료");
                } catch (IOException e) {
                    // 이미지 업로드 실패 처리
                    log.error("이미지 업로드 오류", e);
                }
            }
        }
    }

    //이미지삭제
    @Transactional
    public void deleteImage(Long articleId, String fileName) {
        Optional<Article> optionalArticle = articleRepository.findById(articleId);
        if (optionalArticle.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found");
        }

        Article article = optionalArticle.get();

        List<AttachDetail> existingImages = article.getAttach().getAttachDetailList();
        for (AttachDetail image : existingImages) {
            if (image.getRealname().equals(fileName)) {
                String checkFilename = image.getFileUrl();
                int idx = checkFilename.indexOf("article/");
                String deleteFilename = image.getFileUrl().substring(idx);
                s3UploadService.deleteFile(deleteFilename);

                attachDetailRepository.delete(image);
                log.info("이미지 삭제 완료");
                return;
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");
    }

    //글삭제
    @Transactional
    public void deleteArticle(Long articleId, String loginId) {
        Article article = articleRepository.findById(articleId).orElseThrow(IllegalArgumentException::new);
        if (article.getMember().getLoginId().equals(loginId)) {
            if (article.getDeletedAt() != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            articleRepository.deleteById(articleId);
        }
    }

    //댓글쓰기
    @Transactional
    public void createComment(Long articleId, String loginId, String content) {
        Article article = articleRepository.findById(articleId).orElseThrow(IllegalArgumentException::new);
        Optional<Member> member = memberRepository.findByLoginId(loginId);

        commentRepository.save(Comment.builder()
                .article(article)
                .member(member.get())
                .content(content)
                .build());
        log.info("댓글 등록 완료");
    }

    //댓글수정
    @Transactional
    public void updateComment(Long commentId, String loginId, String content) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(IllegalArgumentException::new);
        if (comment.getMember().getLoginId().equals(loginId)) {
            if (comment.getDeletedAt() != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            comment.updateContent(content);
        }
    }

    //댓글삭제
    @Transactional
    public void deleteComment(Long commentId, String loginId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(IllegalArgumentException::new);
        if (comment.getMember().getLoginId().equals(loginId)) {
            if (comment.getDeletedAt() != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
            commentRepository.deleteById(commentId);
        }
    }

    //조회수
    @Transactional
    public int updateViewCnt(Long articleId) {
        return articleRepository.updateViewCnt(articleId);
    }

    //전체 글 목록 조회
    public Page<ArticleListDto> readArticleList(Pageable pageable, String filter, String keyword) {
        return queryRepository.findByArticleAndFilterAndKeyword(pageable, filter, keyword);
    }

    //카테고리별 글 목록 조회
    public Page<ArticleListDto> readArticleListByCategory(Pageable pageable, Long categoryId, String filter, String keyword) {
        return queryRepository.findByArticleAndCategoryAndFilterAndKeyword(pageable, categoryId, filter, keyword);
    }

    //인기글 조회
    public List<HotArticleListDto> findPopularArticlesWithinOneWeek() {
        // 현재 시간에서 일주일 전의 시간 계산
        LocalDateTime oneWeekAgo = LocalDateTime.now().minus(1, ChronoUnit.WEEKS);

        // 일주일 이내에 작성된 인기글 조회
        return articleRepository.findTop3PopularArticlesWithinOneWeek(oneWeekAgo);
    }

    //좋아요
    @Transactional
    public String likeUnlike(Long articleId, String loginId) {
        Article article = articleRepository.findById(articleId).orElseThrow(IllegalArgumentException::new);
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(IllegalArgumentException::new);
        Optional<LikeArticle> optionalLikeArticle = likeArticleRepository.findByArticle_IdAndMember_LoginId(articleId, loginId);
        if(optionalLikeArticle.isEmpty()) {
            likeArticleRepository.save(LikeArticle.builder()
                    .article(article)
                    .member(member)
                    .build());
            return "like";
        }
        else {
            LikeArticle like = optionalLikeArticle.get();
            likeArticleRepository.deleteById(like.getId());
            return "unlike";
        }
    }

    //해당 글 좋아요 리스트
    public List<String> checkLike(Long articleId) {
        List<String> likeMemberList = new ArrayList<>();
        List<LikeArticle> likeList = likeArticleRepository.findByArticle_Id(articleId);

        if(likeList.isEmpty()) {
            return likeMemberList;
        }

        // Entity -> DTO
        for (LikeArticle likeArticle : likeList) {
            likeMemberList.add(likeArticle.getMember().getNickname());
        }

        return likeMemberList;
    }

    //게시글 신고
    @Transactional
    public void reportArticle(Long articleId, String loginId, Long writerId, String content) {
        Optional<Article> article = articleRepository.findById(articleId);
        Optional<Member> member = memberRepository.findByLoginId(loginId);
        Optional<Member> writer = memberRepository.findById(writerId);

        reportRepository.save(Report.builder()
                .article(article.get())
                .member(member.get())
                .writer(writer.get())
                .content(content)
                .build());
        log.info("신고 완료");
    }

    //댓글 신고
    @Transactional
    public void reportComment(Long commentId, String loginId, Long writerId, String content) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        Optional<Member> member = memberRepository.findByLoginId(loginId);
        Optional<Member> writer = memberRepository.findById(writerId);

        reportRepository.save(Report.builder()
                .comment(comment.get())
                .member(member.get())
                .writer(writer.get())
                .content(content)
                .build());
        log.info("신고 완료");
    }
}