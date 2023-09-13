package com.likelion.catdogpia.oauth;

import com.likelion.catdogpia.domain.entity.user.Member;
import com.likelion.catdogpia.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    public OAuth2UserServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String nameAttribute = "";

        Map<String, Object> attributes = new HashMap<>();

        // Naver 로직
        if (registrationId.equals("naver")) {
            attributes.put("provider", "naver");

            // 받은 사용자 데이터를 정리한다.
            Map<String, Object> responseMap = oAuth2User.getAttribute("response");
            attributes.put("id", responseMap.get("id"));
            attributes.put("name", responseMap.get("name"));
            attributes.put("email", responseMap.get("email"));
            attributes.put("nickname", responseMap.get("nickname"));
            attributes.put("mobile", responseMap.get("mobile"));
            nameAttribute = "email";
        }
        log.info(attributes.toString());

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("USER")),
                attributes,
                nameAttribute
        );
    }

    //블랙리스트 확인
    public Character isBlacklist(String loginId) {
        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
        return optionalMember.get().getBlackListYn();
    }
}
