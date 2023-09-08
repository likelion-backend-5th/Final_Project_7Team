package com.likelion.catdogpia.service;

import com.likelion.catdogpia.domain.dto.mypage.PetFormDto;
import com.likelion.catdogpia.domain.entity.user.Member;
import com.likelion.catdogpia.repository.MemberRepository;
import com.likelion.catdogpia.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetService {

    private final MemberRepository memberRepository;
    private final PetRepository petRepository;

    // 펫 등록
    public void savePet(String loginId, PetFormDto petFormDto) {

        Member member = memberRepository.findByLoginId(loginId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        petRepository.save(petFormDto.toEntity(member));

    }

}
