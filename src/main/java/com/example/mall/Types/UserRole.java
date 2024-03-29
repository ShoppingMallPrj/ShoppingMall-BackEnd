package com.example.mall.Types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {

    NONE("NONE"),
    USER("USER"),
    ADMIN("ADMIN");

    private String userRoleType;
}
