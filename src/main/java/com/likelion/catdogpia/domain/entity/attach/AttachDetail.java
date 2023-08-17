package com.likelion.catdogpia.domain.entity.attach;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class AttachDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String fileUrl;

    @Column(nullable = false)
    private int fileSize;

    @Column(nullable = false, length = 260)
    private String realname;

    //파일 연관관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attach_id")
    private Attach attach;

    @Builder
    public AttachDetail(Long id, String fileUrl, int fileSize, String realname, Attach attach) {
        this.id = id;
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
        this.realname = realname;
        this.attach = attach;
    }
}
