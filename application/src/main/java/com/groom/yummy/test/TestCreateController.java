package com.groom.yummy.test;

import com.groom.yummy.dto.ResponseDto;
import com.groom.yummy.jwt.JwtProvider;
import com.groom.yummy.test.dto.SignInDto;
import com.groom.yummy.test.dto.TestUserCreateDto;
import com.groom.yummy.test.dto.TestUserResDto;
import com.groom.yummy.user.User;
import com.groom.yummy.user.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

@Tag(name = "[TestUser] TestUser API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/test/users")
public class TestCreateController {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @Operation(summary = "회원가입", description = "회원 가입 할 유저의 정보를 입력합니다. " +
            "<br> 회원가입이 완료되면 자동으로 유저의 기본 profile 사진이 등록됩니다." +
            "<br> 이후에 유저의 Token 을 통해 profile 사진을 수정할 수 있습니다.")
    @PostMapping("/sign-up")
    public ResponseEntity<ResponseDto<TestUserResDto>> signUp(@Valid @RequestBody TestUserCreateDto testUserCreateDto){
        User user =  User.builder()
                .email(testUserCreateDto.email())
                .nickname(testUserCreateDto.nickname())
                .role("ROLE_USER")
                .groupJoinCount(0L)
                .groupAttendanceCount(0L)
                .isDeleted(false)
                .build();

        user = userRepository.save(user);
        TestUserResDto testUserResDto = TestUserResDto.from(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseDto<>(
                        testUserResDto,
                        "테스트 유저 생성 성공"));
    }


    @Operation(summary = "로그인", description = "회원 가입 한 유저의 loginId와 password를 입력합니다.")
    @PostMapping("/sign-in")
    public ResponseEntity<ResponseDto<?>> signIn(@Valid @RequestBody SignInDto signInReqDto){

        Optional<User> optionalUser = userRepository.findByEmail(signInReqDto.email());
        User user = optionalUser.get();
        String accessToken = jwtProvider.createAccessToken(user.getId(), user.getEmail(),user.getNickname(),user.getRole());
        ResponseCookie cookie = ResponseCookie.from("Authorization", accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(120)
                .sameSite("Strict")
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(SET_COOKIE, cookie.toString())
                .body(new ResponseDto<>(1, "성공하였습니당."));
    }
}
