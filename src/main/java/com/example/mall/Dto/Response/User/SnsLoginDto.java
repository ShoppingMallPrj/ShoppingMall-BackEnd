package com.example.mall.Dto.Response.User;

import com.example.mall.Types.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

//소셜로 로그인 할 경우 결과 객체
@Schema(description = "소셜 로그인 결과 dto")
@Data
public class SnsLoginDto {

    boolean isFirst; //최초 소셜 로그인 여부, 최초 소셜 로그인이면 추가 정보를 입력하게 해야한다.

    int userId;

    String token;

    String email;

    UserType userType;

}
