package com.groom.yummy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.groom.yummy.config.SecurityConfig;
import com.groom.yummy.exception.CustomException;
import com.groom.yummy.exception.JwtErrorCode;

import com.groom.yummy.user.dto.request.UpdateNicknameReqDto;
import com.groom.yummy.user.dto.response.UserInfoResDto;
import com.groom.yummy.user.facade.UserFacade;
import com.groom.yummy.jwt.JwtProvider;
import com.groom.yummy.oauth2.handler.CustomSuccessHandler;
import com.groom.yummy.oauth2.service.CustomOAuth2UserService;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@Import({SecurityConfig.class})
@WebMvcTest(controllers = {UserController.class})
@ExtendWith(MockitoExtension.class) // Mockito 확장 사용
class UserControllerTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockitoBean
    protected JwtProvider jwtProvider;
    @MockitoBean
    protected UserFacade userFacade;
    @MockitoBean
    private CustomOAuth2UserService customOAuth2UserService;
    @MockitoBean
    private CustomSuccessHandler customSuccessHandler;

    @BeforeEach
    void setUp(){
        String mockJwtToken = "mockJwtToken";
        // JwtProvider Mock 설정
        when(jwtProvider.validateToken(mockJwtToken)).thenReturn(false);
        when(jwtProvider.getUserId(mockJwtToken)).thenReturn(1L);
        when(jwtProvider.getUsername(mockJwtToken)).thenReturn("email@gmail.com");
        when(jwtProvider.getName(mockJwtToken)).thenReturn("강형준");
        when(jwtProvider.getRole(mockJwtToken)).thenReturn("ROLE_USER");
    }

    @Test
    @DisplayName("토큰 기반 정보로 유저 조회 테스트")
    void getUserInfoByTokenTest() throws Exception {
        // given
        Long userId = 1L;
        String jwtToken = "mockJwtToken";
        String email = "email@gmail.com";
        String nickname = "강형준";
        UserInfoResDto mockResponse =  UserInfoResDto.builder().id(userId).email(email).nickname(nickname).build();
        when(userFacade.getUserInfo(userId)).thenReturn(mockResponse);

        // When & Then
        ResultActions actions = mockMvc.perform(get("/api/v1/users")
                        .cookie(new Cookie("Authorization", jwtToken)) // 쿠키 추가
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.id").value(userId),
                        jsonPath("$.data.email").value(email),
                        jsonPath("$.data.nickname").value(nickname),
                        jsonPath("$.msg").value("자신의 정보 조회 성공")
                );

        verify(userFacade, times(1))
                .getUserInfo(any(Long.class));
        verify(jwtProvider, times(1))
                .validateToken(any(String.class));
    }

    @Test
    @DisplayName("토큰 검증 실패 - WRONG_TYPE_TOKEN 예외 테스트")
    void getUserInfoWithWrongTypeTokenTest() throws Exception {
        // given
        String jwtToken = "mockJwtToken";
        JwtErrorCode errorCode = JwtErrorCode.WRONG_TYPE_TOKEN;
        doThrow(new CustomException(errorCode))
                .when(jwtProvider).validateToken(any(String.class));

        // When & Then
        mockMvc.perform(get("/api/v1/users")
                        .cookie(new Cookie("Authorization", jwtToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.msg").value(errorCode.getMessage())) // 에러 메시지 검증
                .andExpect(jsonPath("$.data").value(-1L));

        verify(jwtProvider, times(1)).validateToken(any(String.class));
        verify(userFacade, never()).getUserInfo(any(Long.class)); // 유저 조회는 호출되지 않아야 함
    }

    @Test
    @DisplayName("토큰 검증 실패 - EXPIRED_TOKEN 예외 테스트")
    void getUserInfoWithExpiredTokenTest() throws Exception {
        // given
        String jwtToken = "expiredJwtToken";
        JwtErrorCode errorCode = JwtErrorCode.EXPIRED_TOKEN;
        doThrow(new CustomException(errorCode))
                .when(jwtProvider).validateToken(any(String.class));

        // When & Then
        mockMvc.perform(get("/api/v1/users")
                        .cookie(new Cookie("Authorization", jwtToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.msg").value(errorCode.getMessage()))
                .andExpect(jsonPath("$.data").value(-1L));

        verify(jwtProvider, times(1)).validateToken(any(String.class));
        verify(userFacade, never()).getUserInfo(any(Long.class)); // 유저 조회는 호출되지 않아야 함
    }

    @Test
    @DisplayName("토큰 검증 실패 - UNKNOWN_TOKEN_ERROR 예외 테스트")
    void getUserInfoWithUnknownTokenErrorTest() throws Exception {
        // given
        String jwtToken = "unknownJwtToken";
        JwtErrorCode errorCode = JwtErrorCode.UNKNOWN_TOKEN_ERROR;
        doThrow(new CustomException(errorCode))
                .when(jwtProvider).validateToken(any(String.class));

        // When & Then
        mockMvc.perform(get("/api/v1/users")
                        .cookie(new Cookie("Authorization", jwtToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.msg").value(errorCode.getMessage()))
                .andExpect(jsonPath("$.data").value(-1L));

        verify(jwtProvider, times(1)).validateToken(any(String.class));
        verify(userFacade, never()).getUserInfo(any(Long.class)); // 유저 조회는 호출되지 않아야 함
    }

    @Test
    @DisplayName("userId로 유저를 조회 테스트")
    void getUserInfoTest() throws Exception{
        // given
        Long userId = 1L;
        String jwtToken = "mockJwtToken";
        String email = "email@gmail.com";
        String nickname = "홍길동";
        UserInfoResDto mockResponse =  UserInfoResDto.builder().id(userId).email(email).nickname(nickname).build();
        when(userFacade.getUserInfo(userId)).thenReturn(mockResponse);

        // When & Then
        ResultActions actions = mockMvc.perform(get("/api/v1/users/" + userId)
                        .cookie(new Cookie("Authorization", jwtToken)) // 쿠키 추가
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.id").value(userId),
                        jsonPath("$.data.email").value(email),
                        jsonPath("$.data.nickname").value(nickname),
                        jsonPath("$.msg").value("유저 정보 조회 성공")
                );

        verify(userFacade, times(1))
                .getUserInfo(any(Long.class));
        verify(jwtProvider, times(1))
                .validateToken(any(String.class));
    }

    @Test
    @DisplayName("유저 nickname 변경 테스트.")
    void updateUserNicknameTest() throws Exception{
        // given
        Long userId = 1L;
        String jwtToken = "mockJwtToken";
        String email = "email@gmail.com";
        String updateNick = "이후강형준";

        UpdateNicknameReqDto updateNicknameReqDto = UpdateNicknameReqDto.builder().nickname(updateNick).build();
        String body = objectMapper.writeValueAsString(updateNicknameReqDto);

        UserInfoResDto mockResponse =  UserInfoResDto.builder().id(userId).email(email).nickname(updateNick).build();
        when(userFacade.updateUserNickname(userId, updateNicknameReqDto)).thenReturn(mockResponse);

        // When & Then
        ResultActions actions = mockMvc.perform(patch("/api/v1/users/profile")
                        .cookie(new Cookie("Authorization", jwtToken)) // 쿠키 추가
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data.id").value(userId),
                        jsonPath("$.data.email").value(email),
                        jsonPath("$.data.nickname").value(updateNick),
                        jsonPath("$.msg").value("유저 닉네임 변경 성공")
                );

        verify(userFacade, times(1))
                .updateUserNickname(any(Long.class), any(UpdateNicknameReqDto.class));
        verify(jwtProvider, times(1))
                .validateToken(any(String.class));
    }

    @Test
    @DisplayName("유저 닉네임은 공백일 수 없습니다.")
    void updateUserNicknameTest_NonBlank() throws Exception{
        // given
        Long userId = 1L;
        String jwtToken = "mockJwtToken";
        String email = "email@gmail.com";
        String updateNick = "";

        UpdateNicknameReqDto updateNicknameReqDto = UpdateNicknameReqDto.builder().nickname(updateNick).build();
        String body = objectMapper.writeValueAsString(updateNicknameReqDto);

        // When & Then
        ResultActions actions = mockMvc.perform(patch("/api/v1/users/profile")
                        .cookie(new Cookie("Authorization", jwtToken)) // 쿠키 추가
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.data").value(-1L),
                        jsonPath("$.msg").value("must not be blank")
                );

        verify(userFacade, never())
                .updateUserNickname(any(Long.class), any(UpdateNicknameReqDto.class)); // 메서드가 호출되지 않아야 함
        verify(jwtProvider, times(1))
                .validateToken(any(String.class));
    }

    @Test
    @DisplayName("유저 삭제(논리) 테스트")
    void deleteUserByTokenTest() throws Exception{
        // given
        Long userId = 1L;
        String jwtToken = "mockJwtToken";

        when(userFacade.deleteUser(userId)).thenReturn(userId);

        // When & Then
        ResultActions actions = mockMvc.perform(delete("/api/v1/users")
                        .cookie(new Cookie("Authorization", jwtToken)) // 쿠키 추가
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data").value(userId),
                        jsonPath("$.msg").value("회원정보 삭제 성공")
                );

        verify(userFacade, times(1))
                .deleteUser(any(Long.class));
        verify(jwtProvider, times(1))
                .validateToken(any(String.class));
    }
}