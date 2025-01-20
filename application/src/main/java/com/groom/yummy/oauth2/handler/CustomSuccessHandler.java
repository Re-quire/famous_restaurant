package com.groom.yummy.oauth2.handler;

import com.groom.yummy.oauth2.auth.LoginUser;
import com.groom.yummy.util.CookieUtil;
import com.groom.yummy.jwt.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtUtil;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String email = loginUser.getUsername();
        String nickname = loginUser.getName();
        Long userId = loginUser.getUserId();
        String role = loginUser.getRole();

        String accessToken = jwtUtil.createAccessToken(userId,email,nickname,role);

        response.addCookie(CookieUtil.createCookie(jwtUtil.COOKIE_NAME,accessToken,jwtUtil.VALID_TIME));
        response.sendRedirect("http://localhost:8081/swagger-ui/index.html");
    }
}