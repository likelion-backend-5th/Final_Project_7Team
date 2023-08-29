package com.likelion.catdogpia.domain.entity.user;

import com.likelion.catdogpia.domain.dto.admin.MemberDto;
import com.likelion.catdogpia.domain.entity.BaseEntity;
import com.likelion.catdogpia.domain.entity.cart.Cart;
import com.likelion.catdogpia.domain.entity.community.Article;
import com.likelion.catdogpia.domain.entity.community.Comment;
import com.likelion.catdogpia.domain.entity.consultation.Consultation;
import com.likelion.catdogpia.domain.entity.mypage.Address;
import com.likelion.catdogpia.domain.entity.mypage.Pet;
import com.likelion.catdogpia.domain.entity.mypage.Point;
import com.likelion.catdogpia.domain.entity.mypage.WishList;
import com.likelion.catdogpia.domain.entity.notion.Notion;
import com.likelion.catdogpia.domain.entity.order.Orders;
import com.likelion.catdogpia.domain.entity.product.QnA;
import com.likelion.catdogpia.domain.entity.report.Report;
import com.likelion.catdogpia.domain.entity.review.Review;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder
@Entity
@SQLDelete(sql = "UPDATE member SET deleted_at = now() WHERE id = ?")
@Where(clause = "deleted_at is null")
@EntityListeners(AuditingEntityListener.class)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false, unique = true)
    private String loginId;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(length = 60, nullable = false, unique = true)
    private String email;

    @Column(length = 30, nullable = false, unique = true)
    private String nickname;

    @Column(length = 11, nullable = false, unique = true)
    private String phone;

    @Column(length = 5, nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(length = 1)
    private Character socialLogin;

    @Column(length = 1, nullable = false)
    private Character blackListYn;

    //배송지 연관관계
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Address> addressList = new ArrayList<>();

    //반려동물 연관관계
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Pet> petList = new ArrayList<>();

    //1:1상담 연관관계
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Consultation> consultationList = new ArrayList<>();

    //공지사항 연관관계
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Notion> notionList = new ArrayList<>();

    //신고 연관관계
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Report> reportList = new ArrayList<>();

    //댓글 연관관계
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    //커뮤니티 글 연관관계
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Article> articleList = new ArrayList<>();

    //리뷰 연관관계
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Review> reviewList = new ArrayList<>();

    //주문 연관관계
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Orders> orderList = new ArrayList<>();

    //적립금 연관관계
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Point> pointList = new ArrayList<>();

    //장바구니 연관관계
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Cart> cartList = new ArrayList<>();

    //위시리스트 연관관계
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<WishList> wishLists = new ArrayList<>();

    //상품Q&A 연관관계
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<QnA> qnAList = new ArrayList<>();

    //== 회원 수정 ==//
    public void changeMember(MemberDto member) {
        this.name = member.getName();
        this.nickname = member.getNickname();
        this.phone = member.getPhone();
        this.email = member.getEmail();
        this.blackListYn = member.getBlackListYn();
    }

    @Builder
    public Member(Long id, String loginId, String password, String name, String email, String nickname, String phone, Role role, Character socialLogin, Character blackListYn, List<Address> addressList, List<Pet> petList, List<Consultation> consultationList, List<Notion> notionList, List<Report> reportList, List<Comment> commentList, List<Article> articleList, List<Review> reviewList, List<Orders> orderList, List<Point> pointList, List<Cart> cartList, List<WishList> wishLists, List<QnA> qnAList) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.nickname = nickname;
        this.phone = phone;
        this.role = role;
        this.socialLogin = socialLogin;
        this.blackListYn = blackListYn;
        this.addressList = addressList;
        this.petList = petList;
        this.consultationList = consultationList;
        this.notionList = notionList;
        this.reportList = reportList;
        this.commentList = commentList;
        this.articleList = articleList;
        this.reviewList = reviewList;
        this.orderList = orderList;
        this.pointList = pointList;
        this.cartList = cartList;
        this.wishLists = wishLists;
        this.qnAList = qnAList;
    }

    //임시 비밀번호 세팅
    @Builder
    public void setTempPassword(String password) {
        this.password = password;
    }
}
