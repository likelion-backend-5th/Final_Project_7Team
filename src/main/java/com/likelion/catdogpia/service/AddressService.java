package com.likelion.catdogpia.service;

import com.likelion.catdogpia.domain.dto.mypage.AddressFormDto;
import com.likelion.catdogpia.domain.dto.mypage.AddressListDto;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final MemberRepository memberRepository;

    // 배송지 목록 조회
    public Page<AddressListDto> readAllAddress(String loginId, Integer page) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
        Page<Address> addressPage = addressRepository.findAllByMemberId(pageable, member.getId());
        Page<AddressListDto> response = addressPage.map(AddressListDto::fromEntity);

        return response;
    }

    // 배송지 1개 조회 (수정 페이지)
    public AddressListDto readAddress(Long addressId) {
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        // (작성자 본인 맞는지 확인)
        return AddressListDto.fromEntity(address);
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

    @Transactional
    // 배송지 수정
    public void updateAddress(Long addressId, AddressFormDto dto) {

        Address address = addressRepository.findById(addressId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // 기본 배송지 여부 체크X => N으로 저장
        if (dto.getDefaultAddress() == null) {
            dto.setDefaultAddress('N');
        } else {
            // 기본 배송지 여부 체크O => 이미 기본 배송지로 등록된 배송지가 존재한다면 N으로 변경
            // (추후 수정)
            Optional<Address> optionalAddress = addressRepository.findDefaultAddressesByMemberId(1L);
            if (optionalAddress.isPresent()) {
                optionalAddress.get().updateDefaultAddress('N');
            }
        }

        address.updateAddress(dto);

    }

    // 배송지 삭제
    public void deleteAddress(String loginId, Long addressId) {
        Member member = addressRepository.findById(addressId).get().getMember();
        if(!member.getLoginId().equals(loginId)) {
            new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        addressRepository.deleteById(addressId);
    }
}
