package com.likelion.catdogpia.jwt;

import com.likelion.catdogpia.jwt.domain.JwtTokenResponseDto;
import com.likelion.catdogpia.jwt.domain.RefreshToken;
import com.likelion.catdogpia.jwt.exception.ForbiddenException;
import com.likelion.catdogpia.jwt.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
public class JwtTokenProvider {
    private final Key secretKey;
    private final UserDetailsManager manager;
    private final RefreshTokenRepository tokenRepository;
    private final JwtParser jwtParser;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey, UserDetailsManager manager, RefreshTokenRepository tokenRepository) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.manager = manager;
        this.tokenRepository = tokenRepository;
        this.jwtParser = Jwts.parserBuilder().setSigningKey(this.secretKey).build();
    }

    //Access Token, Refresh Token 재발급
    @Transactional
    public JwtTokenResponseDto reissue(String refreshToken) {
        String loginId = parseClaims(refreshToken).getSubject();
        if (Objects.isNull(refreshToken) || loginId == null) throw new ForbiddenException("인증 정보가 만료되었습니다.");
        UserDetails userDetails = manager.loadUserByUsername(loginId);
        Optional<RefreshToken> optionalRefreshToken = tokenRepository.findByLoginId(loginId);
        if(optionalRefreshToken.isEmpty()) throw new ForbiddenException("리프레시 토큰 확인 실패");
        RefreshToken checkRefreshToken = optionalRefreshToken.get();
        if (!refreshToken.equals(checkRefreshToken.getToken())) throw new ForbiddenException("리프레시 토큰 확인 실패");
        String newAccessToken = createToken(userDetails, 30);
        String newRefreshToken = createToken(userDetails, 3600);
        optionalRefreshToken.get().setRefreshToken(newRefreshToken);
        return new JwtTokenResponseDto(newAccessToken, newRefreshToken);
    }

    //로그인 토큰 발급
    @Transactional
    public JwtTokenResponseDto createTokensByLogin(String loginId) {
        UserDetails userDetails = manager.loadUserByUsername(loginId);
        String accessToken = createToken(userDetails, 30);
        String refreshToken = createToken(userDetails, 3600);
        Optional<RefreshToken> optionalRefreshToken = tokenRepository.findByLoginId(loginId);
        if (optionalRefreshToken.isPresent()) {
            tokenRepository.deleteByLoginId(loginId);
            RefreshToken newRefreshToken = new RefreshToken(refreshToken, Duration.ofMillis(3600), loginId);
            tokenRepository.save(newRefreshToken);
        } else {
            RefreshToken newRefreshToken = new RefreshToken(refreshToken, Duration.ofMillis(3600), loginId);
            tokenRepository.save(newRefreshToken);
        }
        return new JwtTokenResponseDto(accessToken, refreshToken);
    }

    //토큰 생성
    private String createToken(UserDetails userDetails, int validTime) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) //발행 유저 정보 저장
                .setIssuedAt(Date.from(Instant.now())) //발행 시간 저장
                .setExpiration(Date.from(Instant.now().plusSeconds(validTime))) //토큰 유효 시간 저장
                .signWith(secretKey) //키 설정
                .compact(); //생성
    }

    //Token 유효성 검증
    @Transactional
    public boolean validate(String token) {
        try {
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //토큰 회원 정보 추출
    @Transactional
    public Claims parseClaims(String token) {
        return jwtParser
                .parseClaimsJws(token)
                .getBody();
    }
}
