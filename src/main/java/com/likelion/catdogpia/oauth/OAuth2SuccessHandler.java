package com.likelion.catdogpia.oauth;

import com.likelion.catdogpia.domain.entity.user.CustomMemberDetails;
import com.likelion.catdogpia.domain.entity.user.Role;
import com.likelion.catdogpia.jwt.JwtTokenProvider;
import com.likelion.catdogpia.jwt.domain.JwtTokenResponseDto;
import com.likelion.catdogpia.service.LoginService;
import com.likelion.catdogpia.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Slf4j
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsManager userDetailsManager;
    private final MemberService memberService;
    private final OAuth2UserServiceImpl oAuth2UserService;

    public OAuth2SuccessHandler(JwtTokenProvider jwtTokenProvider, UserDetailsManager userDetailsManager, MemberService memberService, OAuth2UserServiceImpl oAuth2UserService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsManager = userDetailsManager;
        this.memberService = memberService;
        this.oAuth2UserService = oAuth2UserService;
    }

    @Override
    // 인증 성공시 호출되는 메소드
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String provider = oAuth2User.getAttribute("provider");
        String email = oAuth2User.getAttribute("email");
        String loginId = String.format("{%S}%s", provider, email.split("@")[0]);
        String name = oAuth2User.getAttribute("name");
        String phone = oAuth2User.getAttribute("mobile");
        phone = phone.replace("-", "");
        String nickname = oAuth2User.getAttribute("nickname");
        String providerId = oAuth2User.getAttribute("id").toString();

        // 처음으로 소셜 로그인한 사용자를 데이터베이스에 등록
        if(!memberService.userExists(loginId)) {
            userDetailsManager.createUser(CustomMemberDetails.builder()
                    .loginId(loginId)
                    .password(providerId)
                    .name(name)
                    .phone(phone)
                    .email(String.format("{%S}%s", provider, email))
                    .nickname(nickname)
                    .role(Role.USER)
                    .socialLogin('Y')
                    .blackListYn('N')
                    .build());
        }

        if (oAuth2UserService.isBlacklist(loginId).equals('Y')) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        JwtTokenResponseDto jwt = jwtTokenProvider.createTokensByLogin(loginId);
        String refreshToken = jwt.getRefreshToken();

        //RefreshToken Cookie
        Cookie refreshTokenCookie = new Cookie("RefreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);

        String targetUrl = "http://localhost:8080";
        // 실제 Redirect 응답 생성
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
