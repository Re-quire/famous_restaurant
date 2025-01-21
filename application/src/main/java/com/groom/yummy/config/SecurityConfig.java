package com.groom.yummy.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groom.yummy.exception.security.CustomAccessDeniedHandler;
import com.groom.yummy.exception.security.CustomAuthenticationEntryPoint;
import com.groom.yummy.filter.JwtAuthFilter;
import com.groom.yummy.oauth2.handler.CustomSuccessHandler;
import com.groom.yummy.oauth2.service.CustomOAuth2UserService;
import com.groom.yummy.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JwtProvider jwtUtil;
    private final ObjectMapper objectMapper;

    @Value("${server.url}")
    private String SERVER_URL;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        //cors 설정
        http.cors((cors -> cors.configurationSource(configurationSource())));

        // csfr disable
        http.csrf((auth) -> auth.disable());

        // form 로그인 disable
        http.formLogin((auth) -> auth.disable());

        // HTTP Basic 인증 방식 disable
        http.httpBasic((auth) -> auth.disable());

        //경로별 인가 작업
        http.authorizeHttpRequests((auth) -> auth
                .requestMatchers( "/login/**", "/oauth2/**", "/oauth2/authorization/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll() // Swagger 관련 경로 허용
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

    public CorsConfigurationSource configurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedOrigin(SERVER_URL);  // 특정 도메인 허용
        configuration.addAllowedOrigin("http://13.124.191.4:8080");  // 특정 도메인 허용
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("ACCESS_TOKEN");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 주소요청에 위 설정을 넣어주겠다.
        return source;
    }
}