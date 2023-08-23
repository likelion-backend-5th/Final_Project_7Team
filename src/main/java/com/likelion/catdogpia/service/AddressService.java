package com.likelion.catdogpia.service;

import com.likelion.catdogpia.domain.dto.mypage.AddressFormDto;
import com.likelion.catdogpia.domain.entity.mypage.Address;
import com.likelion.catdogpia.domain.entity.user.Member;
import com.likelion.catdogpia.repository.AddressRepository;
import com.likelion.catdogpia.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final MemberRepository memberRepository;

    // 배송지 등록
    public void saveAddress(String id, AddressFormDto dto) {

        Optional<Member> optionalMember = memberRepository.findByLoginId(id);
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
