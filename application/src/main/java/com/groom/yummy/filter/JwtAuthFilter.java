package com.groom.yummy.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groom.yummy.dto.ResponseDto;
import com.groom.yummy.exception.CustomException;
import com.groom.yummy.exception.ErrorCode;
import com.groom.yummy.exception.JwtErrorCode;
import com.groom.yummy.oauth2.auth.LoginUser;
import com.groom.yummy.user.User;
import com.groom.yummy.jwt.JwtProvider;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.groom.yummy.exception.JwtErrorCode.*;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            String token = findToken(request);
            if (!verifyToken(request, token)) {
                filterChain.doFilter(request, response);
                return;
            }

            User user = getUser(token);
            setSecuritySession(user);
            filterChain.doFilter(request, response);

        }catch (CustomException e){
            JwtErrorCode jwtErrorCode = (JwtErrorCode) e.getErrorCode();
            switch (jwtErrorCode) {
                case WRONG_TYPE_TOKEN, UNSUPPORTED_TOKEN, EXPIRED_TOKEN, UNKNOWN_TOKEN_ERROR ->
                        setResponse(response, jwtErrorCode);
                default -> {
                    log.error("알 수 없는 에러 코드: {}", jwtErrorCode);
                    setResponse(response, UNKNOWN_TOKEN_ERROR); // 기본 예외 처리
                }
            }
        }
    }

    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/swagger-ui/") || path.startsWith("/v3/api-docs") || path.startsWith("/swagger-resources/");
    }

    private static void setSecuritySession(User user){
        LoginUser loginUser = new LoginUser(user);
        log.info("SessionLoginUser : {}", loginUser.getUsername());
        log.info("SessionLoginUser getAuthorities: {}", loginUser.getAuthorities());
        Authentication authToken = new UsernamePasswordAuthenticationToken(loginUser,null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private User getUser(String token){
        Long userId = jwtProvider.getUserId(token);
        String username = jwtProvider.getUsername(token);
        String name = jwtProvider.getName(token);
        String role = jwtProvider.getRole(token);

        log.info("getUser username: ", username);
        return User.builder()
                .id(userId)
                .email(username)
                .nickname(name)
                .role(role)
                .build();
    }

    private boolean verifyToken(HttpServletRequest request,String token) throws IOException, ServletException {
        Boolean isValid = (Boolean) request.getAttribute("isTokenValid");
        if(isValid != null) return isValid;

        if (token == null || jwtProvider.validateToken(token)) {
            log.debug("token null");
            request.setAttribute("isTokenValid",false);
            return false;
        }

        request.setAttribute("isTokenValid", true);
        return true;
    }

    private static String findToken(HttpServletRequest request){
        String token = null;
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("Authorization")){
                token = cookie.getValue();
            }
        }
        return token;
    }
    private void setResponse(HttpServletResponse response, JwtErrorCode jwtErrorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(jwtErrorCode.getCode().value());
        response.getWriter().print(objectMapper.writeValueAsString(ResponseDto.of(-1,jwtErrorCode.getMessage())));
    }
}