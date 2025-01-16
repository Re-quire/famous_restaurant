package com.groom.yummy.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groom.yummy.exception.CustomAccessDeniedHandler;
import com.groom.yummy.exception.CustomAuthenticationEntryPoint;
import com.groom.yummy.filter.JwtAuthFilter;
import com.groom.yummy.util.JwtUtil;
import com.groom.yummy.oauth2.handler.CustomSuccessHandler;
import com.groom.yummy.oauth2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        // cors 설정
        // http.cors((cors -> cors.configurationSource(configurationSource())));

        // csfr disable
        http.csrf((auth) -> auth.disable());

        // HTTP Basic 인증 방식 disable
        http.httpBasic((auth) -> auth.disable());

        //경로별 인가 작업
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers( "/login/**", "/oauth2/**", "/oauth2/authorization/**").permitAll() // OAuth2 관련 경로 허용
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll() // Swagger 관련 경로 허용
                .requestMatchers("/image").permitAll()
                .requestMatchers("/api/v1/fcm").permitAll()
                .requestMatchers("/chat/**","/ws-stomp/**","/chatroom","/create","/chatting/**").permitAll()
                .requestMatchers("/crawling/*").permitAll()
                .requestMatchers("/gemini").permitAll()
                .anyRequest().authenticated());

        //세션 설정 : STATELESS
        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //oauth2
        http.oauth2Login((oauth2) ->
                oauth2.userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                        .userService(customOAuth2UserService)).successHandler(customSuccessHandler));

        // JWTFilter 추가
        http.addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        // Exception handler 추가
        http.exceptionHandling(exceptionHandling ->
                exceptionHandling
                        .accessDeniedHandler(new CustomAccessDeniedHandler(objectMapper))
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint(objectMapper)));

        return http.build();
    }
}
