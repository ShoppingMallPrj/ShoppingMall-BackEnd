package com.example.mall.Annotation;

import com.example.mall.Entity.UserEntity;
import com.example.mall.Types.UserRole;
import lombok.Data;

@Data
public class UserDetails {

    private int userId;
    private UserRole userRole;

    public static UserDetails from(UserEntity userEntity){

        UserDetails userDetails = new UserDetails();

        userDetails.setUserId(userEntity.getUserId());
        userDetails.setUserRole(userEntity.getUserRole());

        return userDetails;
    }
}
