package com.likelion.catdogpia.config;

import com.likelion.catdogpia.jwt.JwtTokenFilter;
import com.likelion.catdogpia.oauth.OAuth2SuccessHandler;
import com.likelion.catdogpia.oauth.OAuth2UserServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
public class SecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2UserServiceImpl oAuth2UserService;

    public SecurityConfig(JwtTokenFilter jwtTokenFilter, OAuth2SuccessHandler oAuth2SuccessHandler, OAuth2UserServiceImpl oAuth2UserService) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.oAuth2UserService = oAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        authHttp -> authHttp
                                .requestMatchers("/authorize").authenticated()
                                .requestMatchers("/signout").authenticated()
                                .requestMatchers(HttpMethod.POST, "/community/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/mypage/profile/data").authenticated()
                                .requestMatchers(HttpMethod.GET, "/mypage/profile/update/data").authenticated()
                                .requestMatchers(HttpMethod.GET, "/mypage/order-list/data").authenticated()
                                .requestMatchers(HttpMethod.GET, "/mypage/order-list/review/{opId}/data").authenticated()
                                .requestMatchers(HttpMethod.GET, "/mypage/order-list/exchange/{opId}/data").authenticated()
                                .requestMatchers(HttpMethod.GET, "/mypage/order-list/refund/{opId}/data").authenticated()
                                .requestMatchers(HttpMethod.GET, "/mypage/order-detail/{orderId}/data").authenticated()
                                .requestMatchers(HttpMethod.GET, "/mypage/point/data").authenticated()
                                .requestMatchers(HttpMethod.GET, "/mypage/address/data").authenticated()
                                .requestMatchers(HttpMethod.GET, "/mypage/address/update/{addressId}/data").authenticated()
                                .requestMatchers(HttpMethod.GET, "/mypage/review/data").authenticated()
                                .requestMatchers(HttpMethod.GET, "/mypage/review/{reviewId}/data").authenticated()
                                .requestMatchers(HttpMethod.GET, "/mypage/article/data").authenticated()
                                .requestMatchers(HttpMethod.GET, "/cart/data").authenticated()
                                .anyRequest().permitAll()
                )
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/login")
                        .successHandler(oAuth2SuccessHandler)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(oAuth2UserService)
                        )
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
                        }))
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenFilter, AuthorizationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
