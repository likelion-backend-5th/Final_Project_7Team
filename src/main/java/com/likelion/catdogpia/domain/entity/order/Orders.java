package com.likelion.catdogpia.domain.entity.order;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name ="orders")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    // 회원
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
//    private Member member;

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

    @Builder
    public Orders(Long id, String name, String phone, String address, String request, String cardCompany,
                  int totalAmount, LocalDateTime orderAt, LocalDateTime cancelAt) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.request = request;
        this.cardCompany = cardCompany;
        this.totalAmount = totalAmount;
        this.orderAt = orderAt;
        this.cancelAt = cancelAt;
    }
}
