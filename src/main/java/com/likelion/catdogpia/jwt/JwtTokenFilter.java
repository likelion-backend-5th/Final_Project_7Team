package com.likelion.catdogpia.jwt;

import com.likelion.catdogpia.domain.entity.user.CustomMemberDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.split(" ")[1];
            if (jwtTokenProvider.validate(token)) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                String loginId = jwtTokenProvider.parseClaims(token).getSubject();
                log.info(loginId);

                // 사용자 인증 정보 생성
                AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(CustomMemberDetails.builder().loginId(loginId).build(), token, new ArrayList<>());
                // SecurityContext에 사용자 정보 설정
                context.setAuthentication(authenticationToken);
                // SecurityContextHolder에 SecurityContext 설정
                SecurityContextHolder.setContext(context);
                log.info("사용자 인증 성공");
                log.info(context.toString());
            } else {
                log.warn("유효하지 않은 JWT 입니다.");
            }
            log.info(authHeader);
        }
        filterChain.doFilter(request, response);
    }
}
