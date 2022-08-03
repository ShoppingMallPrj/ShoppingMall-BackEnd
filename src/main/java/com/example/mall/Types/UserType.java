package com.example.mall.Types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserType {
    //기본 로그인 타입
    EMAIL("EMAIL"),
    KAKAO("KAKAO"),
    GOOGLE("GOOGLE");

    private String userType;

}
