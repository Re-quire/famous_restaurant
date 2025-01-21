package com.groom.yummy.oauth2.service;

import com.groom.yummy.user.facade.UserFacade;
import com.groom.yummy.oauth2.auth.LoginUser;
import com.groom.yummy.oauth2.dto.OAuth2Response;
import com.groom.yummy.oauth2.strategy.OAuth2ResFactory;
import com.groom.yummy.user.User;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomOAuth2UserServiceTest {
    @InjectMocks
    private CustomOAuth2UserService customOAuth2UserService;

    @Mock
    private OAuth2ResFactory oAuth2ResFactory;

    @Mock
    private UserFacade userFacade;

    @Mock
    private DefaultOAuth2UserService defaultOAuth2UserService;

    Map<String, Object> attributes;

    @BeforeEach
    void setUp(){
        String registrationId = "kakao";
        String email = "email@gmail.com";
        String nickname = "강형준";

        attributes = Map.of(
                "id", registrationId,
                "kakao_account", Map.of("email", email),
                "properties", Map.of("nickname", nickname)
        );
    }

    @Test
    void kakaoOAuth2loadUserTest() throws Exception {
        // given
        String registrationId = "kakao";
        String email = "email@gmail.com";
        String nickname = "강형준";
        String oauth2AccessToken = "testAccessToken";

        // 가짜 웹 서버를 통해 실제 카카오 서버와의 통신을 mocking 합니다.
        MockWebServer mockWebServer = new MockWebServer();
        mockWebServer.start();

        // 받은 엑세스 토큰을 통해, 클라이언트가 UserInfo Endpoint 요청 보내 사용자 정보를 받는 동작 Mocking
        String userInfoResponse = "{ \"id\": \"" + registrationId +
                "\", \"kakao_account\": { \"email\": \"" + email + "\" }," +
                " \"properties\": { \"nickname\": \"" + nickname + "\" } }";
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(userInfoResponse)
        );

        // Mock OAuth2AccessToken: 엑세스 토큰 Mocking
        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                oauth2AccessToken,
                Instant.now(),
                Instant.now().plusSeconds(3600)
        );

        // OAuth2 인증 및 사용자 정보 요청 과정에서 필요한 클라이언트와 요청 정보를 Mocking
        ClientRegistration clientRegistration = ClientRegistration.withRegistrationId("kakao")
                .clientId("test-client-id")
                .clientSecret("test-client-secret")
                .authorizationUri("https://kauth.kakao.com/oauth/authorize")
                .tokenUri("https://kauth.kakao.com/oauth/token")
                .userInfoUri(mockWebServer.url("/mock/url").toString())
                .userNameAttributeName("id")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                .build();
        OAuth2UserRequest userRequest = new OAuth2UserRequest(clientRegistration, accessToken);

        // 인증 이후 받은 사용자 정보 Mocking
        OAuth2User oAuth2User = Mockito.mock(OAuth2User.class);
        Mockito.when(oAuth2User.getAttributes()).thenReturn(Map.of(
                "id", registrationId,
                "kakao_account", Map.of("email", email),
                "properties", Map.of("nickname", nickname)
        ));

        // 받은 사용자 정보를 변환하는 OAuth2Response 클래스 Mocking
        OAuth2Response oAuth2Response = Mockito.mock(OAuth2Response.class);
        Mockito.when(oAuth2Response.getEmail()).thenReturn(email);
        Mockito.when(oAuth2Response.getName()).thenReturn(nickname);
        Mockito.when(oAuth2ResFactory.createOAuth2Response(registrationId, oAuth2User.getAttributes()))
                .thenReturn(oAuth2Response);

        // userFacade  클래스 목킹
        User mockUser = User.builder().email(email).nickname(nickname).build();
        Mockito.when(userFacade.findAuthUserByEmail(email)).thenReturn(Optional.empty());
        Mockito.when(userFacade.findOrCreateUser(Optional.empty(), nickname, email)).thenReturn(mockUser);

        // when
        OAuth2User result = customOAuth2UserService.loadUser(userRequest);

        // then
        assertNotNull(result);
        assertTrue(result instanceof LoginUser); // 리턴 값이 LoginUser 객체인지 확ㅇㄴ

        // OAuth2User 구현체인 LoginUser 로 캐스팅
        LoginUser loginUser = (LoginUser) result;

        assertEquals(email, loginUser.getUser().getEmail());
        assertEquals(nickname, loginUser.getUser().getNickname());

        // MockWebServer 종료
        mockWebServer.shutdown();
    }
}