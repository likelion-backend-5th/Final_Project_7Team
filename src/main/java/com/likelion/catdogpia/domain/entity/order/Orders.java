package com.likelion.catdogpia.domain.entity.order;

import com.likelion.catdogpia.domain.entity.product.OrderProduct;
import com.likelion.catdogpia.domain.entity.user.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name ="orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    // 회원
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false, length = 150)
    private String address;

    @Column(length = 150)
    private String request;

    @Column(length = 30)
    private String cardCompany;

    private int totalAmount;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime orderAt;

    private LocalDateTime cancelAt;

    @OneToMany(mappedBy = "order")
    private List<OrderProduct> orderProductList;

    @Builder
    public Orders(Long id, Member member, String name, String phone, String address, String request, String cardCompany, int totalAmount, LocalDateTime orderAt, LocalDateTime cancelAt, List<OrderProduct> orderProductList) {
        this.id = id;
        this.member = member;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.request = request;
        this.cardCompany = cardCompany;
        this.totalAmount = totalAmount;
        this.orderAt = orderAt;
        this.cancelAt = cancelAt;
        this.orderProductList = orderProductList;
    }
}
