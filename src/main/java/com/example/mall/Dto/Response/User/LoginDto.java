package com.example.mall.Dto.Response.User;

import com.example.mall.Entity.UserEntity;
import com.example.mall.Token.TokenProvider;
import com.example.mall.Types.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

//로그인 결과들, 유저 정보가 담긴다.
@Schema(description = "로그인 성공 시 결과")
@Data
public class LoginDto {

    @Schema(description = "유저 고유 id")
    int userId;

    @Schema(description = "jwt 토큰", nullable = false)
    String token;

    @Schema(description = "jwt 토큰", nullable = false)
    String email;

    @Schema(description = "유저의 role")
    UserRole userRole;

    public static LoginDto from(UserEntity userEntity, TokenProvider tokenProvider){

        LoginDto loginDto = new LoginDto();

        loginDto.setUserId(userEntity.getUserId());
        loginDto.setEmail(userEntity.getUserEmail());
        loginDto.setUserRole(userEntity.getUserRole());
        loginDto.setToken(tokenProvider.createToken(userEntity.getUserRole(), userEntity.getUserId()));

        return loginDto;
    }

}
