package com.groom.yummy.controller;

import com.groom.yummy.dto.request.UpdateNicknameReqDto;
import com.groom.yummy.dto.response.UserInfoResDto;
import com.groom.yummy.facade.UserFacade;
import com.groom.yummy.dto.ResponseDto;
import com.groom.yummy.oauth2.auth.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Tag(name = "[User] User API")
@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserFacade userFacade;

    @Operation(summary = "자신의 정보 조회", description = "토큰 정보 기반 유저 정보를 조회합니다")
    @GetMapping()
    public ResponseEntity<ResponseDto<UserInfoResDto>> getUserInfoByToken(@AuthenticationPrincipal LoginUser loginUser){
        Long userId = loginUser.getUserId();
        UserInfoResDto userInfoResDto = userFacade.getUserInfo(userId);
        return ResponseEntity.ok(ResponseDto.of(userInfoResDto,"자신의 정보 조회 성공"));
    }

    @Operation(summary = "유저 정보 조회", description = "토큰 정보 기반 유저 정보를 조회합니다")
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDto<UserInfoResDto>> getUserInfo(@PathVariable("userId") Long userId){
        UserInfoResDto userInfoResDto = userFacade.getUserInfo(userId);
        return ResponseEntity.ok(ResponseDto.of(userInfoResDto,"유저 정보 조회 성공"));
    }

    @Operation(summary = "유저 닉네임 변경", description = "토큰 정보 기반 유저 닉네임을 변경합니다.")
    @PatchMapping("/profile")
    public ResponseEntity<ResponseDto<UserInfoResDto>> updateUserNickname(@RequestBody UpdateNicknameReqDto updateNicknameReqDto,
                                                                          @AuthenticationPrincipal LoginUser loginUser){
        Long userId = loginUser.getUserId();
        UserInfoResDto userInfoResDto = userFacade.updateUserNickname(userId,updateNicknameReqDto);
        return ResponseEntity.ok(ResponseDto.of(userInfoResDto,"유저 닉네임 변경 성공"));
    }

    @Operation(summary = "유저 계정 삭제", description = "토큰 정보 기반 유저를 삭제합니다.")
    @DeleteMapping()
    public ResponseEntity<ResponseDto<Long>> deleteUserByToken(@AuthenticationPrincipal LoginUser loginUser){
        Long userId = loginUser.getUserId();
        Long deleteUserId = userFacade.deleteUser(userId);
        return ResponseEntity.ok(ResponseDto.of(deleteUserId,"회원정보 삭제 성공"));
    }
}
