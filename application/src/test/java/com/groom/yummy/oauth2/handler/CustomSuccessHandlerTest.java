package com.groom.yummy.oauth2.handler;

import com.groom.yummy.jwt.JwtProvider;
import com.groom.yummy.oauth2.auth.LoginUser;
import com.groom.yummy.user.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomSuccessHandlerTest {

    @InjectMocks
    private CustomSuccessHandler customSuccessHandler;
    @Mock
    private JwtProvider jwtProvider;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp(){
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        jwtProvider.COOKIE_NAME = "Authorization";
        jwtProvider.VALID_TIME = 1000L;
    }

    @Test
    @DisplayName("OAuth2 성공 핸들러 테스트")
    void onAuthenticationSuccessTest() throws ServletException, IOException {
        // given
        Long userId = 1L;
        String email = "email@gmail.com";
        String nickname = "강형준";
        String role = "ROLE_USER";
        String expectedToken = "mockAccessToken";
        String cookieName = "Authorization";
        Long validTime = 1000L;

        User user = User.builder().id(userId).email(email).nickname(nickname).role(role).build();
        LoginUser loginUser = new LoginUser(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(loginUser, null);

        when(jwtProvider.createAccessToken(userId, email, nickname, role)).thenReturn(expectedToken);

        // when
        customSuccessHandler.onAuthenticationSuccess(request, response, authentication);

        // then
        Cookie cookie = response.getCookie(cookieName);
        assertNotNull(cookie);
        assertEquals(expectedToken, cookie.getValue());
        assertEquals(validTime, cookie.getMaxAge());

        assertEquals("http://localhost:8081/swagger-ui/index.html", response.getRedirectedUrl());

        verify(jwtProvider, times(1))
                .createAccessToken(userId, email, nickname,role);
    }
}