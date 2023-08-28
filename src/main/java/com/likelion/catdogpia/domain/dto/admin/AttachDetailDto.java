package com.likelion.catdogpia.domain.dto.admin;

import com.likelion.catdogpia.domain.entity.attach.AttachDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AttachDetailDto {

    private Long id;
    private String fileUrl;
    private String realname;

    // == Entity -> DTO == //
    public static AttachDetailDto fromEntity(AttachDetail attachDetail) {
        return AttachDetailDto.builder()
                .id(attachDetail.getId())
                .fileUrl(attachDetail.getFileUrl())
                .realname(attachDetail.getRealname())
                .build();
    }

    @Builder
    public AttachDetailDto(Long id, String fileUrl, String realname) {
        this.id = id;
        this.fileUrl = fileUrl;
        this.realname = realname;
    }
}
