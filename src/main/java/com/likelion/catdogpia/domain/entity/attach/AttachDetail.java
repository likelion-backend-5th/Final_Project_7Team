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
    @Column(name = "attach_detail_id")
    private Long attachDetailId;

    @Column(name = "file_url", length = 100, nullable = false)
    private String fileUrl;

    @Column(name = "file_size", nullable = false)
    private Integer fileSize;

    @Column(nullable = false, length = 260)
    private String realname;

    @Builder
    public AttachDetail(Long attachDetailId, String fileUrl, Integer fileSize, String realname) {
        this.attachDetailId = attachDetailId;
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
        this.realname = realname;
    }
}
