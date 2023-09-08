package com.likelion.catdogpia.domain.dto.mypage;

import com.likelion.catdogpia.domain.entity.mypage.Pet;
import com.likelion.catdogpia.domain.entity.user.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetFormDto {

    @NotBlank
    private String name;

    @NotBlank
    private String breed;

    private Double weight;

    public Pet toEntity(Member member) {
        return Pet.builder()
                .name(name)
                .breed(breed)
                .weight(weight)
                .member(member)
                .build();
    }
}
