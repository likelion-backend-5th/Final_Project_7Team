package com.likelion.catdogpia.service;

import com.likelion.catdogpia.domain.dto.mypage.AddressFormDto;
import com.likelion.catdogpia.domain.dto.mypage.AddressResponseDto;
import com.likelion.catdogpia.domain.entity.mypage.Address;
import com.likelion.catdogpia.domain.entity.user.Member;
import com.likelion.catdogpia.repository.AddressRepository;
import com.likelion.catdogpia.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final MemberRepository memberRepository;

    // 배송지 목록 조회
    public Page<AddressResponseDto> readAllAddress(String loginId, Integer page, Integer limit) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Pageable pageable = PageRequest.of(page, limit, Sort.by("id").descending());
        Page<Address> addressPage = addressRepository.findAllByMemberId(pageable, member.getId());
        Page<AddressResponseDto> response = addressPage.map(AddressResponseDto::fromEntity);

        return response;
    }

    // 배송지 등록
    public void saveAddress(String loginId, AddressFormDto dto) {

        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
        Member member = optionalMember.get();

        // 기본 배송지 여부 체크X => N으로 저장
        if (dto.getDefaultAddress() == null) {
            dto.setDefaultAddress('N');
        } else {
            // 기본 배송지 여부 체크O => 이미 기본 배송지로 등록된 배송지가 존재한다면 N으로 변경
            Optional<Address> optionalAddress = addressRepository.findDefaultAddressesByMemberId(member.getId());
            if (optionalAddress.isPresent()) {
                optionalAddress.get().updateDefaultAddress('N');
            }
        }

        addressRepository.save(dto.toEntity(member));
    }

}
