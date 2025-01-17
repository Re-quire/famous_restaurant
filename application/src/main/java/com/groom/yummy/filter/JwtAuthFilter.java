package com.groom.yummy.filter;

import com.groom.yummy.oauth2.auth.LoginUser;
import com.groom.yummy.user.User;
import com.groom.yummy.jwt.JwtProvider;
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

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 요청한 URL이 swagger-ui 경로인 경우 바로 필터 통과
        if (request.getRequestURI().equals("/swagger-ui/index.html")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = findToken(request);
        log.info("token before verify: {}", token);
        if (verifyToken(request, response, filterChain, token)) return;
        log.info("token after verify: {}", token);
        User user = getUser(token);

        setSecuritySession(user);
        filterChain.doFilter(request, response);
    }

    private static void setSecuritySession(User user){
        LoginUser loginUser = new LoginUser(user);
        log.info("SessionLoginUser : {}", loginUser.getUsername());
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

    private boolean verifyToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, String token) throws IOException, ServletException {
        if (token == null) {
            log.debug("token null");
            filterChain.doFilter(request, response);
            return true;
        }
        if (jwtProvider.validateToken(token)) {
            log.debug("token expired");
            filterChain.doFilter(request, response);
            return true;
        }
        return false;
    }

    private static String findToken(HttpServletRequest request){
        String token = null;
        Cookie[] cookies = request.getCookies();
        log.info("cookies : {}", cookies);
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("Authorization")){
                token = cookie.getValue();
            }
        }
        return token;
    }
}
