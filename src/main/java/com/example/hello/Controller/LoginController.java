package com.example.hello.Controller;

import com.example.hello.Dto.Response.User.LoginDto;
import com.example.hello.Service.GoogleLoginService;
import com.example.hello.Service.KakaoLoginService;
import com.example.hello.Service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "SNS Login Controller", description = "소셜 로그인 컨트롤러")
@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    KakaoLoginService kakaoLoginService;

    @Autowired
    GoogleLoginService googleLoginService;

    @Operation(summary = "일반 로그인", description = "카카오 ")
    @PostMapping("/email")
    public LoginDto emailLogin(@RequestBody com.example.hello.Dto.Request.User.LoginDto loginDto) {

        return userService.login(loginDto);
    }

    @Operation(summary = "카카오 로그인", description = "카카오 ")
    @GetMapping("/kakao")
    public ResponseEntity<LoginDto> kakaoLogin
            (@Parameter(description = "코드 값", in = ParameterIn.QUERY)
             @RequestParam String code
            ) throws JsonProcessingException {

        LoginDto loginDto = kakaoLoginService.kakaoLogin(code);
        return new ResponseEntity<LoginDto>(
                loginDto,
                HttpStatus.OK
        );
        //return loginResultDto;
    }

    @Operation(summary = "구글 로그인")
    @GetMapping("/google")
    public ResponseEntity<LoginDto> googleLogin(@Parameter(description = "토큰 값", in = ParameterIn.QUERY) @RequestParam String tokenId){

        LoginDto loginDto = googleLoginService.googleLogin(tokenId);
        return new ResponseEntity<LoginDto>(
                loginDto,
                HttpStatus.OK
        );
    }
}
